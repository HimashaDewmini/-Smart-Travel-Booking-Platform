package com.example.FlightService.Repository;

import com.example.FlightService.Entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> { }
