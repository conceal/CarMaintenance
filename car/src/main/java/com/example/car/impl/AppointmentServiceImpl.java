package com.example.car.impl;

import com.example.car.dao.AppointmentMapper;
import com.example.car.dao.ProgramMapper;
import com.example.car.dao.StoreMapper;
import com.example.car.dao.UserMapper;
import com.example.car.model.*;
import com.example.car.service.AppointmentService;
import com.example.car.util.AppointmentTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    AppointmentMapper appointmentMapper;
    @Autowired
    StoreMapper storeMapper;
    @Autowired
    MaintenanceInfoServiceImpl maintenanceInfoService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ProgramMapper programMapper;

    @Override
    public int addAppointment(Appointment appointment) {
        int result = appointmentMapper.addAppointment(appointment.getId(), appointment.getUser_id(),
                appointment.getProgram_id(), appointment.getStore_id(), appointment.getAppointment_time(),
                appointment.getComplete(), appointment.getEvaluate());
        return result;
    }

    @Override
    public List<Appointment> selectUserAppointment(int user_id) {
        return appointmentMapper.selectUserAppointment(user_id);
    }

    @Override
    public List<AppointmentForUser> userCompleteAppointment(int user_id, int complete) {
        return appointmentMapper.userCompleteAppointment(user_id, complete);
    }

    @Override
    public List<AppointmentForUser> selectEvaluateAppointment(int user_id, int complete, int evaluate){
        return appointmentMapper.selectEvaluateAppointment(user_id, complete, evaluate);
    };

    @Override
    public List<Appointment> selectStoreAppointment(int store_id) {
        return appointmentMapper.selectStoreAppointment(store_id);
    }

    @Override
    public List<AppointmentForStore> storeCompleteAppointment(int store_id, int complete) {
        return appointmentMapper.storeCompleteAppointment(store_id, complete);
    }

    @Override
    public int completedAppointment(int id) {
        //处理预约
        int result = appointmentMapper.completedAppointment(id);
        if (result == 1) {
            Appointment appointment = appointmentMapper.selectAppointment(id);
            Program program = programMapper.selectProgram(appointment.getProgram_id());
            //更新保养信息
            maintenanceInfoService.updateMaintenanceTime(appointment.getUser_id(),
                    program.getProgram_name(), appointment.getAppointment_time());
            //更新保养项目销量
            programMapper.updateProgramTradingVolume(appointment.getProgram_id());
            //更新保养店销量
            return storeMapper.updateStoreTradingVolume(appointment.getStore_id());
        } else {
            return 0;
        }
    }

    @Override
    public int updateAppointmentTime(int id, String time) {
        return appointmentMapper.updateAppointmentTime(id, time);
    }

    @Override
    public int deleteAppointment(int id) {
        return appointmentMapper.deleteAppointment(id);
    }

    @Override
    public Appointment selectAppointment(int id) {
        return null;
    }

    @Override
    public List<String> selectProgramAppointmentTime(int program_id, String date, String business_time) {
        //查询该保养店所有预约时间段
        List<String> appointingList = AppointmentTimeUtil.calculateTime(business_time);
        //查询保养店所有已经被预约的时间段
        List<String> appointedList = appointmentMapper.selectProgramAppointmentTime(program_id, date);
        //计算保养店所有可预约时间段
        for (int i = 0; i < appointedList.size(); i++) {
            appointingList.remove(appointedList.get(i).substring(11));
        }
        for (int i = 0; i < appointingList.size(); i++) {
            System.out.println(appointingList.get(i));
        }
        return appointingList;
    }

    @Override
    public int evaluateAppointment(int evaluate) {
        return 0;
    }


}
