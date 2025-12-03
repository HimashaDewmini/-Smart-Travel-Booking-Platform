package com.example.BookingService.client;


import com.example.BookingService.DTO.FlightResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-service", url = "${flight.service.url}")
public interface FlightClient {
    @GetMapping("/flights/{id}")
    FlightResponse getFlight(@PathVariable("id") Long id);
}

