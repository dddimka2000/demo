package com.mps.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AirplaneCharacteristics {
    private double maxSpeed;
    private double maxAcceleration;
    private double altitudeChangeSpeed;
    private double courseChangeSpeed;
}
