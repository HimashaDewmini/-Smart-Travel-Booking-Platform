package com.example.PaymentService.DTO;

public class PaymentRequest {
    private Long bookingId;
    private Double amount;

    public PaymentRequest() {}
    public PaymentRequest(Long bookingId, Double amount) {
        this.bookingId = bookingId;
        this.amount = amount;
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
