package com.example.BookingService.DTO;

import lombok.Data;

@Data
public class FlightResponse {
    private Long id;
    private Double price;
    private Integer availableSeats;
    private String airline;
    private String origin;
    private String destination;
}

