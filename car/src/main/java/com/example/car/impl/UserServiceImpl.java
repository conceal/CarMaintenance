package com.example.car.impl;

import com.example.car.dao.UserMapper;
import com.example.car.model.Type;
import com.example.car.model.User;
import com.example.car.service.MaintenanceInfoService;
import com.example.car.service.UserService;
import com.example.car.util.TimeUtil;
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
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    TypeServiceImpl typeService;
    @Autowired
    MaintenanceInfoServiceImpl maintenanceInfoService;

    public int register(HttpServletRequest servletRequest){
        int id = Integer.parseInt(servletRequest.getParameter("id"));
        String account = servletRequest.getParameter("account");
        String password = servletRequest.getParameter("password");
        String nickname = servletRequest.getParameter("nickname");
        String phone_number = servletRequest.getParameter("phone_number");
        String car_brand = servletRequest.getParameter("car_brand");
        String register_time = servletRequest.getParameter("register_time");
        String head_url = null;
        MultipartFile storeFile = null;
        if (servletRequest instanceof StandardMultipartHttpServletRequest) {
            MultiValueMap<String, MultipartFile> fileMap = ((StandardMultipartHttpServletRequest) servletRequest).getMultiFileMap();
            storeFile = fileMap.get("head_url").get(0);
            head_url = saveImg(storeFile);
        }

        User user1 = userMapper.selectUser(account);
        if (user1 == null) {
            int result =  userMapper.register(id, account, password, nickname, head_url, phone_number, car_brand, register_time);
            User resultUser = userMapper.selectUser(account);
            List<Type> list = typeService.selectAllType();
            String next_time = TimeUtil.getNextTime(register_time);
            for (int i = 0; i < list.size(); i++) {
                maintenanceInfoService.insertMaintenanceInfo(resultUser.getId(), list.get(i).getType(), register_time, next_time);
            }
            return result;
        }
        else if (account.equals(user1.getAccount()))
            return 0;
        else if (nickname.equals(user1.getNickname()))
            return 2;
        else
            return 0;
    }

    public User selectUser(String account){
        return userMapper.selectUser(account);
    }

    public User selectUserById(int id) {
        return userMapper.selectUserById(id);
    }
    public List<User> selectAllUser(){
        return userMapper.selectAllUser();
    }
    public int deleteUser(int id){
        return userMapper.deleteUser(id);
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
