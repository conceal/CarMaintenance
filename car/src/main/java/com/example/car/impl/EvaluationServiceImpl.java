package com.example.car.impl;

import com.example.car.dao.AppointmentMapper;
import com.example.car.dao.EvaluationMapper;
import com.example.car.dao.ProgramMapper;
import com.example.car.dao.UserMapper;
import com.example.car.model.*;
import com.example.car.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService {
    @Autowired
    EvaluationMapper evaluationMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    StoreServiceImpl storeService;
    @Autowired
    ProgramMapper programMapper;

    @Override
    public int addEvaluation(Evaluation evaluation) {
        Appointment appointment = appointmentMapper.selectAppointment(evaluation.getAppointment_id());
        int result = 0;
        //发表评论
        result = evaluationMapper.addEvaluation(evaluation.getId(), evaluation.getUser_id(),
                evaluation.getAppointment_id(), evaluation.getProgram_id(), evaluation.getStore_id(),
                evaluation.getScore(), evaluation.getEvaluation(), evaluation.getEvaluate_time());
        if (result == 1) {
            int evaluated = appointmentMapper.evaluateAppointment(evaluation.getAppointment_id());
            if (evaluated == 1) {
                //更新保养项目平均评分
                programMapper.updateProgramScore(evaluation.getProgram_id());

                //更新保养店平均评分
                double score = evaluationMapper.calculateScore(evaluation.getStore_id());
                return storeService.updateScore(evaluation.getStore_id(), score);
            }
        }
        return result;
    }

    @Override
    public List<EvaluationForUser> selectEvaluationByStoreId(int store_id) {
        return evaluationMapper.selectEvaluationByStoreId(store_id);
    }

    @Override
    public List<EvaluationForProgram> selectProgramEvaluation(int program_id) {
        return evaluationMapper.selectProgramEvaluation(program_id);
    }

    @Override
    public List<EvaluationForUser> selectUserEvaluation(int user_id) {
        return evaluationMapper.selectUserEvaluation(user_id);
    }

    @Override
    public double calculateScore(int store_id) {
        return evaluationMapper.calculateScore(store_id);
    }
}
