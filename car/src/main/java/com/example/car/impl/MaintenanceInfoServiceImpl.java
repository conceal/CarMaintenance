package com.example.car.impl;

import com.example.car.dao.MaintenanceInfoMapper;
import com.example.car.model.MaintenanceInfo;
import com.example.car.service.MaintenanceInfoService;
import com.example.car.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceInfoServiceImpl implements MaintenanceInfoService {
    @Autowired
    MaintenanceInfoMapper maintenanceInfoMapper;

    @Override
    public int insertMaintenanceInfo(int user_id, String maintenance_name, String last_time, String next_time) {
        return maintenanceInfoMapper.insertMaintenanceInfo(user_id, maintenance_name, last_time, next_time);
    }

    /**
     * 查询车主的汽车保养信息
     * @param userId
     * @param maintenanceName
     * @return
     */
    @Override
    public MaintenanceInfo selectMaintenanceInfo(int userId, String maintenanceName) {
        return maintenanceInfoMapper.selectMaintenanceInfo(userId, maintenanceName);
    }

    @Override
    public int updateMaintenanceTime(int user_id, String program, String last_time) {
        String next_time = TimeUtil.getNextTime(last_time);
        return maintenanceInfoMapper.updateMaintenanceTime(user_id, program, last_time, next_time);
    }


}
