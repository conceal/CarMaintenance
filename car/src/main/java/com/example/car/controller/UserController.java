package com.example.car.controller;

import com.example.car.impl.UserServiceImpl;
import com.example.car.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @RequestMapping("/register")
    @ResponseBody
    public int register(HttpServletRequest httpServletRequest) {
        return userService.register(httpServletRequest);
    }

    @RequestMapping("/login")
    @ResponseBody
    public User login(String account) {
        return userService.selectUser(account);
    }

    @RequestMapping("/selectUserById")
    @ResponseBody
    public User selectUserById(int id) {
        return userService.selectUserById(id);
    }

    @RequestMapping("/selectAllUser")
    @ResponseBody
    public List<User> selectAllUser(){
        return userService.selectAllUser();
    }

    @RequestMapping("/deleteUser")
    @ResponseBody
    public int deleteUser(int id){
        return userService.deleteUser(id);
    }



}
