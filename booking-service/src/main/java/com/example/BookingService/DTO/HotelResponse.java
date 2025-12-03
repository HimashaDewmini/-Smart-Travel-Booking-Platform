package com.example.BookingService.DTO;


import lombok.Data;

@Data
public class HotelResponse {
    private Long id;
    private Double price;
    private Integer availableRooms;
    private String hotelName;
    private String location;
}
