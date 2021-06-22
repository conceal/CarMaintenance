package com.example.car.service;

import com.example.car.model.Store;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StoreService {
    int register(HttpServletRequest servletRequest);
    Store login(String account);
    Store selectStore(int id);
    List<Store> selectAllStore();
    int updateScore(int id, double score);
    double selectStoreScore(int id);
    int updateStoreTradingVolume(int id);
    int deleteStore(int id);
    String selectStoreBusinessTime(int id);
}
