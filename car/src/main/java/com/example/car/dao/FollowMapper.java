package com.example.car.dao;

import com.example.car.model.Follow;
import com.example.car.model.Store;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface FollowMapper {
    int insertFollow(int id, int user_id, int store_id);
    Follow selectFollow(int user_id, int store_id);
    int deleteFollow(int id);
    List<Store> selectFollowStores(int user_id);
}
