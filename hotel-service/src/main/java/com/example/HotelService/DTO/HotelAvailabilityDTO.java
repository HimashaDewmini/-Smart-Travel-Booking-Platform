package com.example.HotelService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelAvailabilityDTO {
    private Double price;
    private Integer availableRooms;
}
