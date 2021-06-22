package com.example.car.controller;

import com.example.car.impl.AppointmentServiceImpl;
import com.example.car.model.Appointment;
import com.example.car.model.AppointmentForUser;
import com.example.car.model.AppointmentForStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    AppointmentServiceImpl appointmentService;

    /**
     * 添加一条预约
     * @param appointment
     * @return
     */
    @RequestMapping("/addAppointment")
    @ResponseBody
    public int addAppointment(Appointment appointment) {
        return appointmentService.addAppointment(appointment);
    }

    /**
     * 车主查询预约
     * @param user_id
     * @return
     */
    @RequestMapping("/selectUserAppointment")
    @ResponseBody
    public List<Appointment> selectUserAppointment(int user_id) {
        return appointmentService.selectUserAppointment(user_id);
    }

    /**
     * 车主查询完车预约
     * @param user_id
     * @param complete
     * @return
     */
    @RequestMapping("/userCompleteAppointment")
    @ResponseBody
    public List<AppointmentForUser> userCompleteAppointment(int user_id, int complete) {
        return appointmentService.userCompleteAppointment(user_id, complete);
    }

    @RequestMapping("selectEvaluateAppointment")
    @ResponseBody
    public List<AppointmentForUser> selectEvaluateAppointment(int user_id, int complete, int evaluate) {
        return appointmentService.selectEvaluateAppointment(user_id, complete, evaluate);
    }

    /**
     * 保养店主查询预约
     * @param store_id
     * @return
     */
    @RequestMapping("/selectStoreAppointment")
    @ResponseBody
    public List<Appointment> selectStoreAppointment(int store_id) {
        return appointmentService.selectStoreAppointment(store_id);
    }

    /**
     * 保养店主查询完成预约
     * @param store_id
     * @param complete
     * @return
     */
    @RequestMapping("/storeCompleteAppointment")
    @ResponseBody
    public List<AppointmentForStore> storeCompleteAppointment(int store_id, int complete) {
        return appointmentService.storeCompleteAppointment(store_id, complete);
    }

    /**
     * 设置预约为已完成状态
     * @param id
     * @return
     */
    @RequestMapping("/completedAppointment")
    @ResponseBody
    public int completedAppointment(int id) {
        return appointmentService.completedAppointment(id);
    }

    /**
     * 车主修改预约时间
     * @param id
     * @param time
     * @return
     */
    @RequestMapping("/updateAppointmentTime")
    @ResponseBody
    public int updateAppointmentTime(int id, String time) {
        return appointmentService.updateAppointmentTime(id, time);
    }

    /**
     * 车主删除预约
     * @param id
     * @return
     */
    @RequestMapping("/deleteAppointment")
    @ResponseBody
    public int deleteAppoint(int id) {
        return appointmentService.deleteAppointment(id);
    }

    @RequestMapping("/selectProgramTime")
    @ResponseBody
    public List<String> selectProgramTime(int program_id, String date, String business_time) {
        return appointmentService.selectProgramAppointmentTime(program_id, date, business_time);
    }
}
