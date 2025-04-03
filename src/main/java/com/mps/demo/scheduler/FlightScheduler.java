package com.mps.demo.scheduler;

import com.mps.demo.entity.Airplane;
import com.mps.demo.entity.Flight;
import com.mps.demo.entity.WayPoint;
import com.mps.demo.service.AirplaneService;
import com.mps.demo.service.FlightGenerationService;
import com.mps.demo.service.FlightService;
import com.mps.demo.service.PlaneCalculation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
public class FlightScheduler {

    private final AirplaneService airplaneService;
    private final PlaneCalculation planeCalculation;
    private final FlightService flightService;
    private final FlightGenerationService flightGenerationService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("Application is ready, initializing...");
        updateAircraftPositions();
    }

    @Scheduled(fixedRate = 100000)
    public void updateAircraftPositions() {
        List<Airplane> airplanes = airplaneService.getAllAirplanes();
        List<Thread> threads = new ArrayList<>();
        for (Airplane airplane : airplanes) {
            flightService.printPastFlightInfo(airplane);
            List<WayPoint> wayPoints = flightGenerationService.generateRandomWayPoints(9, airplane.getCharacteristics());
            Flight flight = flightService.createFlight(wayPoints);
            airplane.getFlights().add(flight);
            airplaneService.save(airplane);
            log.info("Operator set the points for the airplane with id={}", airplane.getId());
            Thread thread = new Thread(() -> planeCalculation.startFlight(airplane, wayPoints));
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Error occurred while waiting for thread to finish", e);
            }
        }
        log.info("All airplanes have completed their flights.");
    }


}