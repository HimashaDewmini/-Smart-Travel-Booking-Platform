package com.example.BookingService.DTO;
import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId;
    private Double amount;

    public PaymentRequest(Long id, Double totalCost) {
    }
}

