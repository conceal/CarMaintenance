package com.example.car.dao;

import com.example.car.model.Store;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface StoreMapper {
    int register(int id, String account, String password, String phone_number, String store_name, String store_address, String store_url, String register_time, double score, int trading_volume, String location);
    Store login(String account);
    Store selectStore(int id);
    List<Store> selectAllStore();
    int updateScore(int id, double score);
    double selectStoreScore(int id);
    int updateStoreTradingVolume(int id);
    int deleteStore(int id);
    String selectStoreBusinessTime(int id);
}
