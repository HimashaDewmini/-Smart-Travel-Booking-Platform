package com.example.FlightService.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Price required")
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

    @NotNull(message = "availableSeats required")
    @Min(value = 0, message = "availableSeats must be >= 0")
    private Integer availableSeats;

    // optional fields
    private String airline;
    private String origin;
    private String destination;
}
