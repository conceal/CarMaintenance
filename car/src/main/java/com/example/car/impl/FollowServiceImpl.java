package com.example.car.impl;

import com.example.car.dao.FollowMapper;
import com.example.car.dao.StoreMapper;
import com.example.car.model.Follow;
import com.example.car.model.Store;
import com.example.car.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    FollowMapper followMapper;
    @Autowired
    StoreMapper storeMapper;

    public int insertFollow(Follow follow){
        if (selectFollow(follow.getUser_id(), follow.getStore_id()) > 0 ) return 0;
        int result = followMapper.insertFollow(follow.getId(), follow.getUser_id(), follow.getStore_id());
        return result == 1 ? selectFollow(follow.getUser_id(), follow.getStore_id()) : -1;
    };

    public int selectFollow(int user_id, int store_id){
        Follow follow = followMapper.selectFollow(user_id, store_id);
        if (follow != null) return follow.getId();
        return 0;
    };

    public int deleteFollow(int id){
        return followMapper.deleteFollow(id);
    };

    public List<Store> selectFollowStores(int user_id) {
        return followMapper.selectFollowStores(user_id);
    };
}
