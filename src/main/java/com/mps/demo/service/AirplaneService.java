package com.mps.demo.service;

import com.mps.demo.entity.Airplane;
import com.mps.demo.repository.AirplaneRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Log4j2
public class AirplaneService {

    private final AirplaneRepository airplaneRepository;


    public List<Airplane> getAllAirplanes() {
        return airplaneRepository.findAll();
    }

    public Airplane save(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }
}