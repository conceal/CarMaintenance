package com.example.car.controller;

import com.example.car.impl.EvaluationServiceImpl;
import com.example.car.model.Evaluation;
import com.example.car.model.EvaluationForProgram;
import com.example.car.model.EvaluationForUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController {

    @Autowired
    EvaluationServiceImpl evaluationService;

    /**
     * 添加一条评论
     * @param evaluation
     * @return
     */
    @RequestMapping("/addEvaluation")
    @ResponseBody
    public int addEvaluation(Evaluation evaluation) {
        return evaluationService.addEvaluation(evaluation);
    }

    /**
     * 查询用户评论
     * @param user_id
     * @return
     */
    @RequestMapping("selectUserEvaluation")
    @ResponseBody
    public List<EvaluationForUser> selectUserEvaluation(int user_id) {
        return evaluationService.selectUserEvaluation(user_id);
    }

    /**
     * 查看保养服务评论
     * @param program_id*
     * @return
     */
    @RequestMapping("/selectProgramEvaluation")
    @ResponseBody
    public List<EvaluationForProgram> selectProgramEvaluation(int program_id) {
        return evaluationService.selectProgramEvaluation(program_id);
    }

    @RequestMapping("selectEvaluationByStoreId")
    @ResponseBody
    public List<EvaluationForUser> selectEvaluationByStoreId(int store_id) {
        return evaluationService.selectEvaluationByStoreId(store_id);
    }

    @RequestMapping("/calculateScore")
    @ResponseBody
    public double calculateScore(int store_id) {
        return evaluationService.calculateScore(store_id);
    }

}
