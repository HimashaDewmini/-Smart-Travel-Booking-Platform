package com.example.PaymentService.Service;

import com.example.PaymentService.DTO.PaymentRequest;
import com.example.PaymentService.DTO.PaymentResponse;
import com.example.PaymentService.Entity.Payment;
import com.example.PaymentService.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository repo;
    private final WebClient webClient;

    @Value("${booking.service.url}") // Example: http://localhost:8084/bookings/update-status
    private String bookingCallbackUrl;

    public PaymentService(PaymentRepository repo, WebClient.Builder builder) {
        this.repo = repo;
        this.webClient = builder.build();
    }

    public PaymentResponse processPayment(PaymentRequest req) {

        // 1️⃣ Dummy business logic
        String status = "SUCCESS";

        // 2️⃣ Save payment to DB
        Payment payment = new Payment(
                null,
                req.getBookingId(),
                req.getAmount(),
                status,
                LocalDateTime.now()
        );
        repo.save(payment);

        // 3️⃣ Notify Booking Service
        try {
            webClient.put() // Use PUT to match Booking Service
                    .uri(bookingCallbackUrl)
                    .bodyValue(Map.of(
                            "bookingId", req.getBookingId(),
                            "status", "PAID"
                    ))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            System.out.println("Booking Service notified successfully");
        } catch (WebClientResponseException e) {
            System.err.println("Booking Service responded with error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Error notifying Booking Service:");
            e.printStackTrace();
        }

        // 4️⃣ Return response
        return new PaymentResponse(
                req.getBookingId(),
                status,
                "Payment processed successfully"
        );
    }
}
