package com.example.FlightService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
    private Long id;
    private Double price;
    private Integer availableSeats;
    private String airline;
    private String origin;
    private String destination;
}
