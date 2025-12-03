package com.example.FlightService.Controller;

import com.example.FlightService.DTO.FlightDTO;
import com.example.FlightService.Entity.Flight;
import com.example.FlightService.Service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlight(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFlightById(id));
    }

    @PostMapping
    public ResponseEntity<Flight> createFlight(@Valid @RequestBody Flight flight) {
        Flight saved = service.createFlight(flight);
        return ResponseEntity.ok(saved);
    }
}
