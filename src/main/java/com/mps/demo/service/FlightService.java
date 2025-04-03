package com.mps.demo.service;

import com.mps.demo.entity.Airplane;
import com.mps.demo.entity.Flight;
import com.mps.demo.entity.WayPoint;
import com.mps.demo.repository.FlightRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void printPastFlightInfo(Airplane airplane) {
        List<Flight> flights = airplane.getFlights();
        if (flights.isEmpty() || flights == null) {
        } else {
            long totalFlights = flights.size();
            Duration totalDuration = Duration.ZERO;
            for (Flight flight : flights) {
                totalDuration = totalDuration.plus(Duration.ofSeconds(flight.getPassedPoints().size()));
            }
            log.info("Information about airplane id=" + airplane.getId());
            log.info("Number of past flights: " + totalFlights);
            log.info("Total duration of past flights: " + totalDuration.toSeconds() + " seconds");
        }
    }

    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    public void setAutoincrementId(Flight flight) {
        Optional<Flight> topByOrderByNumberDesc = flightRepository.findTopByOrderByNumberDesc();
        if (topByOrderByNumberDesc.isPresent()) {
            flight.setNumber(topByOrderByNumberDesc.get().getNumber() + 1);
        } else {
            flight.setNumber(1l);
        }
    }

    public Flight createFlight(List<WayPoint> wayPoints) {
        Flight flight = new Flight();
        flight.setWayPoints(wayPoints);
        setAutoincrementId(flight);
        flight.setPassedPoints(new ArrayList<>());
        save(flight);
        log.info("Flight created");
        return flight;
    }
}