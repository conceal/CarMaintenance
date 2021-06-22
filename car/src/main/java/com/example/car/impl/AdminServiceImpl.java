package com.example.car.impl;

import com.example.car.dao.AdminMapper;
import com.example.car.model.Admin;
import com.example.car.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin selectAdmin(String username) {
        return adminMapper.selectAdmin(username);
    }

    @Override
    public Admin login(String account, String password) {
        boolean result = false;
        Admin administrator = adminMapper.selectAdmin(account);
        if (administrator.getSession_id() == 0) {
            result = password.equals(administrator.getPassword());
        }
        if(result)
            return administrator;
        else
            return null;
    }

    @Override
    public List<Admin> selectAllAdmin() {
        return adminMapper.selectAllAdmin();
    }

    @Override
    public int register(Admin admin) {
        return adminMapper.register(admin.getId(), admin.getAccount(), admin.getPassword());
    }
}
