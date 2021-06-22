package com.example.car.service;

import com.example.car.model.Program;
import com.example.car.model.ProgramDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProgramService {
    int releaseProgram(HttpServletRequest servletRequest);
    List<Program> searchProgram(String program_name);
    int updateProgramTradingVolume(int program_id);
    int updateProgramScore(int program_id);
    ProgramDetails selectProgramDetails(int id);
    Program selectProgram(int id);
    List<Program> selectProgramByStoreId(int store_id);
    public int deleteProgram(int id);

}
