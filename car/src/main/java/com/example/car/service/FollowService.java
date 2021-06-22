package com.example.car.service;

import com.example.car.model.Follow;
import com.example.car.model.Store;

import java.util.ArrayList;
import java.util.List;

public interface FollowService {
    int insertFollow(Follow follow);
    int selectFollow(int user_id, int store_id);
    int deleteFollow(int id);
    List<Store> selectFollowStores(int user_id);
}
