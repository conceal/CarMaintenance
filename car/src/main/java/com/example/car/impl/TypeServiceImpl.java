package com.example.car.impl;

import com.example.car.dao.TypeMapper;
import com.example.car.model.Type;
import com.example.car.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    TypeMapper typeMapper;

    @Override
    public List<Type> selectAllType() {
        return typeMapper.selectAllType();
    }
}
