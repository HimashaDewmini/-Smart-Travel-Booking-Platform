package com.example.BookingService.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long flightId;
    private Long hotelId;
    private String travelDate;
    private Double totalCost;
    private String status; // PENDING, PAID, CONFIRMED, FAILED
}

