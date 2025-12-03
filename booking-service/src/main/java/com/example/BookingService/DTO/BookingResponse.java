package com.example.BookingService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String status;
    private Double totalCost;
    private String message;
}

