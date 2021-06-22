package com.example.car.service;

import com.example.car.model.Admin;

import java.util.List;

public interface AdminService {
    Admin selectAdmin(String username);
    Admin login(String username, String password);
    List<Admin> selectAllAdmin();
    int register(Admin admin);
}
