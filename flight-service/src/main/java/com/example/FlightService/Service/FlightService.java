package com.example.FlightService.Service;

import com.example.FlightService.DTO.FlightAvailabilityDTO;
import com.example.FlightService.DTO.FlightDTO;
import com.example.FlightService.Entity.Flight;
import com.example.FlightService.Exceptions.FlightNotFoundException;
import com.example.FlightService.Repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FlightService {

    private final FlightRepository repo;

    public FlightService(FlightRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public FlightDTO getFlightById(Long id) {
        Flight f = repo.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight with id " + id + " not found"));
        return toDTO(f);
    }

    @Transactional(readOnly = true)
    public List<FlightDTO> getAllFlights() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public FlightAvailabilityDTO checkAvailability(Long id) {
        Flight f = repo.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight with id " + id + " not found"));
        return new FlightAvailabilityDTO(f.getAvailable(), f.getPrice());
    }

    @Transactional
    public Flight createFlight(Flight flight) {
        return repo.save(flight);
    }

    // -------------------------
    // UPDATE FLIGHT
    // -------------------------
    @Transactional
    public FlightDTO updateFlight(Long id, FlightDTO dto) {
        Flight existing = repo.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight with id " + id + " not found"));

        existing.setFlightNumber(dto.getFlightNumber());
        existing.setAvailable(dto.getAvailable());
        existing.setPrice(dto.getPrice());
        existing.setAvailableSeats(dto.getAvailableSeats());
        existing.setAirline(dto.getAirline());
        existing.setOrigin(dto.getOrigin());
        existing.setDestination(dto.getDestination());

        Flight updated = repo.save(existing);
        return toDTO(updated);
    }

    // -------------------------
    // DELETE FLIGHT
    // -------------------------
    @Transactional
    public void deleteFlight(Long id) {
        Flight flight = repo.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight with id " + id + " not found"));
        repo.delete(flight);
    }

    private FlightDTO toDTO(Flight f) {
        return new FlightDTO(f.getId(), f.getFlightNumber(), f.getAvailable(), f.getPrice(),
                f.getAvailableSeats(), f.getAirline(), f.getOrigin(), f.getDestination());
    }
}
