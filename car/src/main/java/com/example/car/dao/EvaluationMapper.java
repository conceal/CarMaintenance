package com.example.car.dao;

import com.example.car.model.EvaluationForProgram;
import com.example.car.model.EvaluationForUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface EvaluationMapper {
    //添加一条评论
    int addEvaluation(int id, int user_id, int appointment_id, int program_id, int store_id, int score, String evaluation, String evaluate_time);
    //查看预约评论
    List<EvaluationForUser> selectUserEvaluation(int user_id);
    //查看保养项目的评论
    List<EvaluationForProgram> selectProgramEvaluation(int program_id);
    List<EvaluationForUser> selectEvaluationByStoreId(int store_id);
    //计算平均分
    double calculateScore(int store_id);
    double calculateProgram(int store_id, int program_id);
}
