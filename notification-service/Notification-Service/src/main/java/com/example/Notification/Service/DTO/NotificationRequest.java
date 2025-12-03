package com.example.Notification.Service.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationRequest {

    private Long userId;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    private String type; // EMAIL / SMS
}
