package com.example.car.impl;

import com.example.car.dao.ProgramMapper;
import com.example.car.dao.StoreMapper;
import com.example.car.model.Program;
import com.example.car.model.ProgramDetails;
import com.example.car.model.Store;
import com.example.car.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ProgramServiceImpl implements ProgramService {
    @Autowired
    ProgramMapper programMapper;
    @Autowired
    StoreMapper storeMapper;

    @Override
    public ProgramDetails selectProgramDetails(int id) {
        return programMapper.selectProgramDetails(id);
    }

    @Override
    public Program selectProgram(int id) {
        return programMapper.selectProgram(id);
    }

    @Override
    public List<Program> searchProgram(String program_name) {
        return programMapper.searchProgram(program_name);
    }

    @Override
    public List<Program> selectProgramByStoreId(int store_id) {
        return programMapper.selectProgramByStoreId(store_id);
    }

    @Override
    public int deleteProgram(int id) {
        return programMapper.deleteProgram(id);
    }

    @Override
    public int updateProgramTradingVolume(int program_id) {
        return programMapper.updateProgramTradingVolume(program_id);
    }

    @Override
    public int updateProgramScore(int program_id) {
        return programMapper.updateProgramScore(program_id);
    }

    @Override
    public int releaseProgram(HttpServletRequest servletRequest) {
        String program_name = servletRequest.getParameter("program_name");
        String price = servletRequest.getParameter("price");
        String store_id = servletRequest.getParameter("store_id");
        String program_url = null;
        String details_url = null;
        System.out.println(program_name);
        MultipartFile headFile = null;
        MultipartFile detailsFile = null;

        if (servletRequest instanceof StandardMultipartHttpServletRequest) {
            MultiValueMap<String, MultipartFile> filemap = ((StandardMultipartHttpServletRequest) servletRequest).getMultiFileMap();
            headFile = filemap.get("programImg").get(0);
            program_url = saveImg(headFile);
            detailsFile = filemap.get("detailsImg").get(0);
            details_url = saveImg(detailsFile);
//            int releaseProgram(int id, String programName, String programUrl, String storeName, int price, int tradingVolume, String details, int score);
        }
        if (program_url.equals("") || details_url.equals("") || program_url.equals(details_url)) {
            return 0;
        }
        return programMapper.releaseProgram(0, program_name, program_url, Integer.parseInt(store_id), Integer.parseInt(price), 0, details_url, 0);
    }

    private String saveImg(MultipartFile file) {
        String path = "/Users/syz/img";
        File filePath = new File(path);
        System.out.println("文件的保存路径：" + path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            System.out.println("目录不存在，创建目录:" + filePath);
            filePath.mkdir();
            System.out.println("目录路径"+ filePath);
        }

        //获取原始文件名称(包含格式)
        String originalFileName = file.getOriginalFilename();
        originalFileName=originalFileName.replaceAll(",|&|=", "");
        System.out.println("原始文件名称：" + originalFileName);
        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        System.out.println("文件类型：" + type);
        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        //设置文件新名称: 当前时间+文件名称（不包含格式）
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String date = sdf.format(d);
        //String fileName ="a"+date + name + "." + type;
        String fileName =date + "." + type;
        System.out.println("新文件名称：" + fileName);
        //在指定路径下创建一个文件
        File targetFile = new File(path, fileName);
        if (targetFile.exists()) {
            return saveImg(file);
        }
        //将文件保存到服务器指定位置
        try {
            file.transferTo(targetFile);
            System.out.println("上传成功");
            return fileName;
            //将文件在服务器的存储路径返回
        } catch (IOException e) {
            System.out.println("上传失败");
            e.printStackTrace();
            return " ";
        }
    }
}
