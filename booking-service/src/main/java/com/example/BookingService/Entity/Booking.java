package com.example.BookingService.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotNull(message = "Hotel ID is required")
    private Long hotelId;

    @NotNull(message = "Travel date is required")
    private String travelDate;

    @NotNull(message = "Total cost is required")
    @Min(value = 0, message = "Total cost must be non-negative")
    private Double totalCost;

    @NotNull(message = "Status is required")
    private String status; // PENDING, PAID, CONFIRMED, FAILED
}
