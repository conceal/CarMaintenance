package com.example.car.service;

import com.example.car.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    int register(HttpServletRequest servletRequest);
    User selectUser(String account);
    User selectUserById(int id);
    List<User> selectAllUser();
    int deleteUser(int id);
}
