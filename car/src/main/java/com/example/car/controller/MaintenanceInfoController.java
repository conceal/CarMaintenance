package com.example.car.controller;

import com.example.car.impl.MaintenanceInfoServiceImpl;
import com.example.car.model.MaintenanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/maintenanceInfo")
public class MaintenanceInfoController {

    @Autowired
    MaintenanceInfoServiceImpl MaintenanceInfoService;

    @RequestMapping("/selectMaintenanceInfo")
    @ResponseBody
    public MaintenanceInfo selectMaintenanceInfo(@RequestParam("userId")int userId, @RequestParam("maintenanceName")String maintenanceName) {
        return MaintenanceInfoService.selectMaintenanceInfo(userId, maintenanceName);
    }
}
