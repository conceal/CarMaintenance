package com.example.car.dao;

import com.example.car.model.Program;
import com.example.car.model.ProgramDetails;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ProgramMapper {
    int releaseProgram(int id, String program_name, String program_url, int store_id, int price, int trading_volume, String details, int score);
    Program selectProgram(int id);
    ProgramDetails selectProgramDetails(int id);
    List<Program> searchProgram(String program_name);
    List<Program> selectProgramByStoreId(int store_id);
    int deleteProgram(int id);
    int updateProgramTradingVolume(int id);
    int updateProgramScore(int id);
}
