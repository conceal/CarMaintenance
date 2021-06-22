package com.example.car.dao;

import com.example.car.model.MaintenanceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MaintenanceInfoMapper {
    int insertMaintenanceInfo(int user_id, String program, String last_time, String next_time);
    MaintenanceInfo selectMaintenanceInfo(int user_id, String program);
    int updateMaintenanceTime(int user_id, String program_name, String last_time, String next_time);
}
