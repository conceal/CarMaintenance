package com.example.car.service;

import com.example.car.model.Appointment;
import com.example.car.model.AppointmentForUser;
import com.example.car.model.AppointmentForStore;

import java.util.List;

public interface AppointmentService {
    int addAppointment(Appointment appointment);
    Appointment selectAppointment(int id);
    List<Appointment> selectUserAppointment(int user_id);
    List<AppointmentForUser> userCompleteAppointment(int user_id, int complete);
    List<AppointmentForUser> selectEvaluateAppointment(int user_id, int complete, int evaluate);
    List<Appointment> selectStoreAppointment(int store_id);
    List<AppointmentForStore> storeCompleteAppointment(int store_id, int complete);
    int completedAppointment(int id);
    int updateAppointmentTime(int id, String appointment_time);
    int deleteAppointment(int id);
    int evaluateAppointment(int evaluate);
    List<String> selectProgramAppointmentTime(int program_id, String date, String business_time);
}
