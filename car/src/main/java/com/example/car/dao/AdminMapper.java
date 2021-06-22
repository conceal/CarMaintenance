package com.example.car.dao;

import com.example.car.model.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AdminMapper {
    Admin selectAdmin(String username);
    List<Admin> selectAllAdmin();
    int register(int id, String username, String password);
}
