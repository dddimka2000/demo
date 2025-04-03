package com.mps.demo.service;

import com.mps.demo.entity.AirplaneCharacteristics;
import com.mps.demo.entity.WayPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class FlightGenerationService {
    double startLatitude = 48.8566;
    double startLongitude = 2.3522;
    double endLatitude = 51.5074;
    double endLongitude = -0.1278;
    private static final Random random = new Random();

    public List<WayPoint> generateRandomWayPoints(int numberOfPoints, AirplaneCharacteristics characteristics) {
        List<WayPoint> wayPoints = new ArrayList<>();

        for (int i = 0; i < numberOfPoints; i++) {
            double latitude = generateRandomLatitudeBetween(startLatitude, endLatitude);
            double longitude = generateRandomLongitudeBetween(startLongitude, endLongitude);
            int altitude = generateRandomAltitude();
            int speed = generateRandomSpeed(characteristics.getMaxSpeed());

            wayPoints.add(new WayPoint(latitude, longitude, altitude, speed));
        }
        return wayPoints;
    }

    private double generateRandomLatitudeBetween(double startLatitude, double endLatitude) {
        return startLatitude + (endLatitude - startLatitude) * random.nextDouble();
    }

    private double generateRandomLongitudeBetween(double startLongitude, double endLongitude) {
        return startLongitude + (endLongitude - startLongitude) * random.nextDouble();
    }

    private int generateRandomAltitude() {
        return 5000 + random.nextInt(10000);
    }

    private int generateRandomSpeed(double maxSpeed) {
        double minSpeed = maxSpeed * 0.5;
        double maxSpeedLimit = maxSpeed * 0.95;

        return (int) (minSpeed + random.nextDouble() * (maxSpeedLimit - minSpeed));
    }
}
