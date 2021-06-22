package com.example.car.controller;

import com.example.car.impl.ProgramServiceImpl;
import com.example.car.model.Program;
import com.example.car.model.ProgramDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/program")
public class ProgramController {

    @Autowired
    ProgramServiceImpl programService;

    /**
     * 发布一个保养项目
     *
     * @param servletRequest
     * @return
     */
    @RequestMapping("/releaseProgram")
    @ResponseBody
    public int releaseProgram(HttpServletRequest servletRequest) {
        return programService.releaseProgram(servletRequest);
    }

    /**
     * 查询保养店的所有保养项目
     *
     * @param store_id
     * @return
     */
    @RequestMapping("/selectProgramByStoreId")
    @ResponseBody
    public List<Program> selectProgramByStoreId(int store_id) {
        return programService.selectProgramByStoreId(store_id);
    }

    /**
     * 查询一个保养项目
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectProgram")
    @ResponseBody
    public ProgramDetails selectProgram(int id) {
        return programService.selectProgramDetails(id);
    }

    /**
     * 搜索保养项目
     *
     * @param program_name
     * @return
     */
    @RequestMapping("/searchProgram")
    @ResponseBody
    public List<Program> searchProgram(@RequestParam("program_name") String program_name) {
        return programService.searchProgram(program_name);
    }

    /**
     * 删除一个保养项目
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteProgram")
    @ResponseBody
    public int deleteProgram(int id) {
        return programService.deleteProgram(id);
    }
}