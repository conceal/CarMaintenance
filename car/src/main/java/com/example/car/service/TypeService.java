package com.example.car.service;

import com.example.car.model.Type;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TypeService {
    List<Type> selectAllType();
}
