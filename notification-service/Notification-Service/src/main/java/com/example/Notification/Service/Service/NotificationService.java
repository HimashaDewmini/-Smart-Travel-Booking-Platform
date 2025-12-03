package com.example.Notification.Service.Service;

import com.example.Notification.Service.DTO.NotificationRequest;
import com.example.Notification.Service.DTO.NotificationResponse;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public NotificationResponse sendNotification(NotificationRequest req) {

        // Dummy message sending simulation
        System.out.println(">> Sending Notification to User "
                + req.getUserId() + ": " + req.getMessage());

        return new NotificationResponse("SUCCESS", "Notification sent");
    }
}
