package com.example.HotelService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {
    private Long id;
    private Double price;
    private Integer availableRooms;
    private String hotelName;
    private String location;
}

