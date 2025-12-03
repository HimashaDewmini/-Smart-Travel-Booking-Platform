package com.example.FlightService.Service;

import com.example.FlightService.DTO.FlightDTO;
import com.example.FlightService.Entity.Flight;
import com.example.FlightService.Exceptions.FlightNotFoundException;
import com.example.FlightService.Repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlightService {

    private final FlightRepository repo;

    public FlightService(FlightRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public FlightDTO getFlightById(Long id) {
        Flight f = repo.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight with id " + id + " not found"));
        return new FlightDTO(f.getId(), f.getPrice(), f.getAvailableSeats(), f.getAirline(), f.getOrigin(), f.getDestination());
    }

    @Transactional
    public Flight createFlight(Flight flight) {
        return repo.save(flight);
    }
}
