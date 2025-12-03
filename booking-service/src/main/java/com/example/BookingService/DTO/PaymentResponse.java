package com.example.BookingService.DTO;

import lombok.Data;

@Data
public class PaymentResponse {
    private Long paymentId;
    private String status; // SUCCESS / FAILED
    private String message;
}

