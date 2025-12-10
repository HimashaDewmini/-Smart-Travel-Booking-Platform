package com.example.BookingService.client;

import com.example.BookingService.DTO.NotificationDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NotificationClient {

    private final WebClient webClient;

    public NotificationClient(WebClient webClient) {
        this.webClient = webClient;
    }

    // Method to send booking confirmation notification
    public void sendBookingConfirmation(Long userId, String message) {
        NotificationDTO notification = new NotificationDTO(userId, "EMAIL", message);

        webClient.post()
                .uri("http://localhost:8086/notify") // URL of Notification Service
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(); // async call
    }
}
