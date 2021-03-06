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
        System.out.println("????????????????????????" + path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            System.out.println("??????????????????????????????:" + filePath);
            filePath.mkdir();
            System.out.println("????????????"+ filePath);
        }

        //????????????????????????(????????????)
        String originalFileName = file.getOriginalFilename();
        originalFileName=originalFileName.replaceAll(",|&|=", "");
        System.out.println("?????????????????????" + originalFileName);
        //????????????????????????????????????`.`?????????
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        System.out.println("???????????????" + type);
        //???????????????????????????????????????
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        //?????????????????????: ????????????+?????????????????????????????????
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String date = sdf.format(d);
        //String fileName ="a"+date + name + "." + type;
        String fileName =date + "." + type;
        System.out.println("??????????????????" + fileName);
        //????????????????????????????????????
        File targetFile = new File(path, fileName);
        if (targetFile.exists()) {
            return saveImg(file);
        }
        //???????????????????????????????????????
        try {
            file.transferTo(targetFile);
            System.out.println("????????????");
            return fileName;
            //??????????????????????????????????????????
        } catch (IOException e) {
            System.out.println("????????????");
            e.printStackTrace();
            return " ";
        }
    }
}
