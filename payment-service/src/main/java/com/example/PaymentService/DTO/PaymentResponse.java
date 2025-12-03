package com.example.PaymentService.DTO;

public class PaymentResponse {
    private Long bookingId;
    private String status;
    private String message;

    public PaymentResponse() {}
    public PaymentResponse(Long bookingId, String status, String message) {
        this.bookingId = bookingId;
        this.status = status;
        this.message = message;
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
