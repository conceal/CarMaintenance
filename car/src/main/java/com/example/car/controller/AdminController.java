package com.example.car.controller;

import com.example.car.impl.AdminServiceImpl;
import com.example.car.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminServiceImpl adminService;
    /**
     * 注册
     * @param admin 管理员对象
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public int register(Admin admin){
        return adminService.register(admin);
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public Admin login(@RequestParam("account")String username, @RequestParam("password")String password) {
        return adminService.login(username, password);
    }

    /**
     * 查询一个用户
     * @param username
     * @return
     */
    @RequestMapping("/selectAdmin")
    @ResponseBody
    public Admin selectAdmin(String username) {
        return adminService.selectAdmin(username);
    }

    /**
     * 查询所有用户
     * @return
     */
    @RequestMapping(value = "/allAdmin")
    @ResponseBody
    public List<Admin> allAdmin() {
        return adminService.selectAllAdmin();
    }
}

