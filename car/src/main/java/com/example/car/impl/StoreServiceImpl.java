package com.example.car.impl;

import com.example.car.dao.StoreMapper;
import com.example.car.model.Store;
import com.example.car.service.StoreService;
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
public class StoreServiceImpl implements StoreService {

    @Autowired
    StoreMapper storeMapper;

    @Override
    public int register(HttpServletRequest servletRequest) {
        String program_name = servletRequest.getParameter("program_name");
        int id = Integer.parseInt(servletRequest.getParameter("id"));
        String account = servletRequest.getParameter("account");
        String password = servletRequest.getParameter("password");
        String phone_number = servletRequest.getParameter("phone_number");
        String store_name = servletRequest.getParameter("store_name");
        String store_address = servletRequest.getParameter("store_address");
        String register_time = servletRequest.getParameter("register_time");
        Double score = Double.parseDouble(servletRequest.getParameter("score"));
        int trading_volume = Integer.parseInt(servletRequest.getParameter("trading_volume"));
        String location = servletRequest.getParameter("location");
        String store_url = null;
        MultipartFile storeFile = null;
        if (servletRequest instanceof StandardMultipartHttpServletRequest) {
            MultiValueMap<String, MultipartFile> fileMap = ((StandardMultipartHttpServletRequest) servletRequest).getMultiFileMap();
            storeFile = fileMap.get("store_url").get(0);
            store_url = saveImg(storeFile);
        }
        if (store_url == null) return 0;
        return storeMapper.register(id, account, password,
                phone_number, store_name, store_address, store_url, register_time, score, trading_volume, location);
    }

    @Override
    public Store login(String account) {
        return storeMapper.login(account);
    }

    @Override
    public Store selectStore(int id) {
        System.out.println(id);
        Store store = storeMapper.selectStore(id);
        System.out.println(store.getStore_name());
        return store;
    }

    @Override
    public List<Store> selectAllStore() {
        return storeMapper.selectAllStore();
    }

    @Override
    public int updateScore(int id, double score) {
        System.out.println(String.valueOf(id));
        double storeScore = storeMapper.selectStoreScore(id);
        if (storeScore == score) {
            return 1;
        } else {
            return storeMapper.updateScore(id, score);
        }
    }

    @Override
    public double selectStoreScore(int id) {
        return storeMapper.selectStoreScore(id);
    }

    @Override
    public int updateStoreTradingVolume(int id) {
        return storeMapper.updateStoreTradingVolume(id);
    }

    @Override
    public int deleteStore(int id) {
        return 0;
    }

    @Override
    public String selectStoreBusinessTime(int id) {
        return storeMapper.selectStoreBusinessTime(id);
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
