package com.mps.demo.service;

import com.mps.demo.entity.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Log4j2
@AllArgsConstructor
public class PlaneCalculation {
    private final AirplaneService airplaneService;
    private final FlightService flightService;

    public List<TemporaryPoint> calculateRoute(AirplaneCharacteristics characteristics, List<WayPoint> wayPoints) {
        List<TemporaryPoint> temporaryPoints = new ArrayList<>();
        if (wayPoints == null || wayPoints.isEmpty()) {
            return temporaryPoints;
        }
        double currentLatitude = wayPoints.get(0).getLatitude();
        double currentLongitude = wayPoints.get(0).getLongitude();
        double currentAltitude = wayPoints.get(0).getAltitude();
        double currentSpeed = wayPoints.get(0).getSpeed();
        TemporaryPoint firstPoint = new TemporaryPoint(currentLatitude, currentLongitude, currentAltitude, currentSpeed, 0);
        temporaryPoints.add(firstPoint);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("position= " + firstPoint);
        for (int i = 1; i < wayPoints.size(); i++) {
            WayPoint start = wayPoints.get(i - 1);
            WayPoint end = wayPoints.get(i);
            int steps = calculateSteps(start, end, characteristics);
            double course = calculateCourse(start, end);
            for (int j = 1; j <= steps; j++) {
                double fraction = (double) j / steps;
                currentLatitude = start.getLatitude() + (end.getLatitude() - start.getLatitude()) * fraction;
                currentLongitude = start.getLongitude() + (end.getLongitude() - start.getLongitude()) * fraction;
                currentAltitude = start.getAltitude() + (end.getAltitude() - start.getAltitude()) * fraction;
                currentSpeed = Math.min(end.getSpeed(), characteristics.getMaxSpeed());
                if (currentAltitude != start.getAltitude()) {
                    double altitudeChangeTime = Math.abs(currentAltitude - start.getAltitude()) / characteristics.getAltitudeChangeSpeed();
                    currentSpeed = Math.min(currentSpeed, characteristics.getMaxSpeed() * altitudeChangeTime);
                }
                TemporaryPoint tempPoint = new TemporaryPoint(currentLatitude, currentLongitude, currentAltitude, currentSpeed, course);
                temporaryPoints.add(tempPoint);
                log.info("position= " + tempPoint);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return temporaryPoints;
    }

    private int calculateSteps(WayPoint start, WayPoint end, AirplaneCharacteristics characteristics) {
        double distance = calculateDistance(start, end);
        double timeToTravel = distance / Math.min(start.getSpeed(), characteristics.getMaxSpeed());
        return (int) (timeToTravel);
    }


    private double calculateDistance(WayPoint start, WayPoint end) {
        double deltaX = end.getLatitude() - start.getLatitude();
        double deltaY = end.getLongitude() - start.getLongitude();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    private double calculateCourse(WayPoint start, WayPoint end) {
        double deltaX = end.getLongitude() - start.getLongitude();
        double deltaY = end.getLatitude() - start.getLatitude();
        double angle = Math.atan2(deltaY, deltaX);
        return Math.toDegrees(angle);
    }

    public void startFlight(Airplane airplane, List<WayPoint> wayPoints) {
        log.info("Flight started for airplane with id={}", airplane.getId());
        int currentWayPointIndex = 0;
        while (currentWayPointIndex < wayPoints.size() - 1) {

            WayPoint start = wayPoints.get(currentWayPointIndex);
            WayPoint end = wayPoints.get(currentWayPointIndex + 1);
            List<TemporaryPoint> points = calculateRoute(airplane.getCharacteristics(), Arrays.asList(start, end));

            if (!points.isEmpty()) {
                TemporaryPoint tempPoint = points.get(0);
                airplane.setPosition(tempPoint);
                Flight flight = airplane.getFlights().get(airplane.getFlights().size() - 1);
                flight.getPassedPoints().addAll(points);
                flightService.save(flight);
                airplaneService.save(airplane);

                log.info("Airplane id=" + airplane.getId() + ", position= " + airplane.getPosition());
            }
            currentWayPointIndex++;
        }
        WayPoint lastPoint = wayPoints.get(wayPoints.size() - 1);
        TemporaryPoint finalPosition = new TemporaryPoint(
                lastPoint.getLatitude(),
                lastPoint.getLongitude(),
                lastPoint.getAltitude(),
                lastPoint.getSpeed(),
                calculateCourse(wayPoints.get(wayPoints.size() - 2), lastPoint)
        );
        airplane.setPosition(finalPosition);
        Flight flight = airplane.getFlights().get(airplane.getFlights().size() - 1);
        flight.getPassedPoints().add(finalPosition);
        flightService.save(flight);
        airplaneService.save(airplane);
        log.info("position= " + finalPosition);

        log.info("Flight ended for airplane with id={}", airplane.getId());
    }
}

