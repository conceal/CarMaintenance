package com.example.car.dao;

import com.example.car.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {
    int register(int id, String account, String password, String nickname, String head_url, String phone_number, String car_brand, String register_time);
    User selectUser(String account);
    User selectUserById(int id);
    List<User> selectAllUser();
    int deleteUser(int id);
}
