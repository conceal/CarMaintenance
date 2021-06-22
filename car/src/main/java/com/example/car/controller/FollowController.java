package com.example.car.controller;

import com.example.car.impl.FollowServiceImpl;
import com.example.car.model.Follow;
import com.example.car.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    FollowServiceImpl followService;

    @RequestMapping("/insertFollow")
    @ResponseBody
    int insertFollow(Follow follow) {
        return followService.insertFollow(follow);
    };

    @RequestMapping("/selectFollow")
    @ResponseBody
    int selectFollow(int user_id, int store_id) {
        return followService.selectFollow(user_id, store_id);
    };

    @RequestMapping("/deleteFollow")
    @ResponseBody
    int deleteFollow(int id) {
        return followService.deleteFollow(id);
    }

    @RequestMapping("selectFollowStores")
    @ResponseBody
    List<Store> selectFollowStore(int user_id) {
        return followService.selectFollowStores(user_id);
    }
}
