package com.example.FlightService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightAvailabilityDTO {
    private Boolean available;
    private Double price;
}
