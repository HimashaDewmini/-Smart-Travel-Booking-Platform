package com.example.BookingService.DTO;

import lombok.Data;

@Data
public class BookingRequest {
    private Long userId;
    private Long flightId;
    private Long hotelId;
    private String travelDate;
}

