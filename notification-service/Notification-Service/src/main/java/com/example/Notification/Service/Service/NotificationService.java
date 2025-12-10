package com.example.Notification.Service.Service;

import com.example.Notification.Service.DTO.NotificationRequest;
import com.example.Notification.Service.DTO.NotificationResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async
    public NotificationResponse sendNotification(NotificationRequest req) {

        // Here you can add logic to send EMAIL or SMS based on req.getType()
        if ("EMAIL".equalsIgnoreCase(req.getType())) {
            System.out.println(">> Sending EMAIL to User " + req.getUserId() + ": " + req.getMessage());
        } else if ("SMS".equalsIgnoreCase(req.getType())) {
            System.out.println(">> Sending SMS to User " + req.getUserId() + ": " + req.getMessage());
        } else {
            System.out.println(">> Sending DEFAULT Notification to User " + req.getUserId() + ": " + req.getMessage());
        }

        return new NotificationResponse("SUCCESS", "Notification sent");
    }
}
