package com.mps.demo.repository;

import com.mps.demo.entity.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends MongoRepository<Flight, Long> {
    Optional<Flight> findTopByOrderByNumberDesc();

}
