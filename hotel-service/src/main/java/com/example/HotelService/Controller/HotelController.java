package com.example.HotelService.Controller;


import com.example.HotelService.DTO.HotelAvailabilityDTO;
import com.example.HotelService.DTO.HotelDTO;
import com.example.HotelService.Entity.Hotel;
import com.example.HotelService.Service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService service;

    public HotelController(HotelService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        return ResponseEntity.ok(service.getAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotel(@PathVariable Long id) {
        return ResponseEntity.ok(service.getHotelById(id));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<HotelAvailabilityDTO> checkAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(service.checkAvailability(id));
    }

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotel) {
        return ResponseEntity.ok(service.createHotel(hotel));
    }
}
