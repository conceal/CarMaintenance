package com.example.car.controller;

import com.example.car.impl.TypeServiceImpl;
import com.example.car.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/type")
public class TypeController {

    @Autowired
    TypeServiceImpl typeService;

    @RequestMapping("/getType")
    @ResponseBody
    public List<Type> getType() {
        return typeService.selectAllType();
    }

}
