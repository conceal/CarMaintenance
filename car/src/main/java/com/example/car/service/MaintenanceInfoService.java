package com.example.car.service;

import com.example.car.model.MaintenanceInfo;

public interface MaintenanceInfoService {
    int insertMaintenanceInfo(int user_id, String maintenance_name, String last_time, String next_time);
    MaintenanceInfo selectMaintenanceInfo(int user_id, String Maintenance_name);
    int updateMaintenanceTime(int user_id, String program, String last_time);
}
