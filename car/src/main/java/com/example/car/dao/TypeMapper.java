package com.example.car.dao;

import com.example.car.model.Type;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TypeMapper {
    List<Type> selectAllType();
}
