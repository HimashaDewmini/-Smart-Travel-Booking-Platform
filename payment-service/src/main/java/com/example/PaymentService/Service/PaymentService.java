package com.example.PaymentService.Service;

import com.example.PaymentService.DTO.PaymentRequest;
import com.example.PaymentService.DTO.PaymentResponse;
import com.example.PaymentService.Entity.Payment;
import com.example.PaymentService.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository repo;
    private final WebClient webClient;

    @Value("${booking.service.url}") // http://localhost:8084/bookings/update-status
    private String bookingCallbackUrl;

    public PaymentService(PaymentRepository repo, WebClient.Builder builder) {
        this.repo = repo;
        this.webClient = builder.build();
    }

    public PaymentResponse processPayment(PaymentRequest req) {

        // 1️⃣ Save payment
        Payment payment = new Payment(
                null,
                req.getBookingId(),
                req.getAmount(),
                "SUCCESS",
                LocalDateTime.now()
        );
        repo.save(payment);

        // 2️⃣ Update Booking status to CONFIRMED
        try {
            webClient.post() // Must be POST to match controller
                    .uri(bookingCallbackUrl)
                    .bodyValue(Map.of(
                            "bookingId", req.getBookingId(),
                            "status", "CONFIRMED"
                    ))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block(); // block to ensure BookingService receives it
            System.out.println("Booking status updated to CONFIRMED successfully");
        } catch (Exception e) {
            System.err.println("Error updating booking status:");
            e.printStackTrace();
        }

        return new PaymentResponse(
                req.getBookingId(),
                "SUCCESS",
                "Payment processed and booking confirmed"
        );
    }
}
