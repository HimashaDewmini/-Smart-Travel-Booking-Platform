package com.example.FlightService.Controller;

import com.example.FlightService.DTO.FlightAvailabilityDTO;
import com.example.FlightService.DTO.FlightDTO;
import com.example.FlightService.Entity.Flight;
import com.example.FlightService.Service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    // GET all flights
    @GetMapping
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        return ResponseEntity.ok(service.getAllFlights());
    }

    // GET flight by ID
    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlight(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFlightById(id));
    }

    // GET flight availability
    @GetMapping("/{id}/availability")
    public ResponseEntity<FlightAvailabilityDTO> checkAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(service.checkAvailability(id));
    }

    // CREATE flight
    @PostMapping
    public ResponseEntity<Flight> createFlight(@Valid @RequestBody Flight flight) {
        Flight saved = service.createFlight(flight);
        return ResponseEntity.ok(saved);
    }

    // UPDATE flight
    @PutMapping("/{id}")
    public ResponseEntity<FlightDTO> updateFlight(
            @PathVariable Long id,
            @Valid @RequestBody FlightDTO flightDTO
    ) {
        FlightDTO updated = service.updateFlight(id, flightDTO);
        return ResponseEntity.ok(updated);
    }

    // DELETE flight
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        service.deleteFlight(id);
        return ResponseEntity.ok("Flight deleted successfully.");
    }
}
