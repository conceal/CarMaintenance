package com.example.car.service;

import com.example.car.model.Evaluation;
import com.example.car.model.EvaluationForProgram;
import com.example.car.model.EvaluationForUser;

import java.util.List;

public interface EvaluationService {
    //添加一条评论
    int addEvaluation(Evaluation evaluation);
    //查看预约评论
    List<EvaluationForUser> selectUserEvaluation(int user_id);
    //查看保养项目的评论
    List<EvaluationForProgram> selectProgramEvaluation(int program_id);
    List<EvaluationForUser> selectEvaluationByStoreId(int store_id);
    //计算平均分
    double calculateScore(int store_id);
}
