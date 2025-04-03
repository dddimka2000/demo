package com.mps.demo.migration;

import com.mps.demo.entity.*;
import com.mps.demo.repository.AirplaneRepository;
import com.mps.demo.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AirplaneRepository airplaneRepository;
    private final FlightRepository flightRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (airplaneRepository.count() == 0) {
            Airplane airplane1 = new Airplane();
            airplane1.setId(1l);
            airplane1.setCharacteristics(new AirplaneCharacteristics(250, 5, 10, 1));
            Airplane airplane2 = new Airplane();
            airplane2.setId(2l);
            airplane2.setCharacteristics(new AirplaneCharacteristics(300, 6, 12, 1));
            Airplane airplane3 = new Airplane();
            airplane3.setId(3l);
            airplane3.setCharacteristics(new AirplaneCharacteristics(280, 6, 11, 1));

            Flight pastFlight1 = new Flight();
            pastFlight1.setNumber(1L);
            pastFlight1.setWayPoints(Arrays.asList(
                    new WayPoint(48.8566, 2.3522, 10000, 250),
                    new WayPoint(51.5074, -0.1278, 10000, 250)
            ));
            pastFlight1.setPassedPoints(Arrays.asList(
                    new TemporaryPoint(48.8566, 2.3522, 10000, 250, 0),
                    new TemporaryPoint(50.0000, 1.0000, 10000, 250, 45),
                    new TemporaryPoint(51.5074, -0.1278, 10000, 250, 90)
            ));
            airplane1.setFlights(Arrays.asList(pastFlight1));

            Flight pastFlight2 = new Flight();
            pastFlight2.setNumber(2L);
            pastFlight2.setWayPoints(Arrays.asList(
                    new WayPoint(40.7128, -74.0060, 12000, 300),
                    new WayPoint(34.0522, -118.2437, 12000, 300)
            ));
            pastFlight2.setPassedPoints(Arrays.asList(
                    new TemporaryPoint(40.7128, -74.0060, 12000, 300, 0),
                    new TemporaryPoint(38.0000, -95.0000, 12000, 300, 270),
                    new TemporaryPoint(34.0522, -118.2437, 12000, 300, 270)
            ));
            airplane2.setFlights(Arrays.asList(pastFlight2));

            Flight pastFlight3 = new Flight();
            pastFlight3.setNumber(3L);
            pastFlight3.setWayPoints(Arrays.asList(
                    new WayPoint(35.6895, 139.6917, 9000, 200),
                    new WayPoint(37.7749, -122.4194, 9000, 200)
            ));
            pastFlight3.setPassedPoints(Arrays.asList(
                    new TemporaryPoint(35.6895, 139.6917, 9000, 200, 0),
                    new TemporaryPoint(36.5000, 150.0000, 9000, 200, 90),
                    new TemporaryPoint(37.7749, -122.4194, 9000, 200, 180)
            ));

            airplane3.setFlights(Arrays.asList(pastFlight3));
            flightRepository.saveAll(Arrays.asList(pastFlight1, pastFlight2, pastFlight3));
            airplaneRepository.saveAll(Arrays.asList(airplane1, airplane2, airplane3));
        }
    }
}