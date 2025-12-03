package com.example.BookingService.Service;

import com.example.BookingService.client.FlightClient;
import com.example.BookingService.client.HotelClient;
import com.example.BookingService.DTO.*;
import com.example.BookingService.Entity.Booking;
import com.example.BookingService.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightClient flightClient;
    private final HotelClient hotelClient;
    private final WebClient webClient;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    public BookingService(BookingRepository bookingRepository,
                          FlightClient flightClient,
                          HotelClient hotelClient,
                          WebClient webClient) {
        this.bookingRepository = bookingRepository;
        this.flightClient = flightClient;
        this.hotelClient = hotelClient;
        this.webClient = webClient;
    }

    // Create booking flow
    public BookingResponse createBooking(BookingRequest req) {
        // 1. Validate user via WebClient
        String userUrl = userServiceUrl + "/users/" + req.getUserId();
        Object user;
        try {
            user = webClient.get()
                    .uri(userUrl)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (Exception e) {
            return new BookingResponse(null, "FAILED", 0.0, "User validation failed: " + e.getMessage());
        }
        if (user == null) {
            return new BookingResponse(null, "FAILED", 0.0, "User not found");
        }

        // 2. Check flight via Feign
        FlightResponse flight = flightClient.getFlight(req.getFlightId());
        if (flight == null || flight.getAvailableSeats() == null || flight.getAvailableSeats() <= 0) {
            return new BookingResponse(null, "FAILED", 0.0, "Flight unavailable");
        }

        // 3. Check hotel via Feign
        HotelResponse hotel = hotelClient.getHotel(req.getHotelId());
        if (hotel == null || hotel.getAvailableRooms() == null || hotel.getAvailableRooms() <= 0) {
            return new BookingResponse(null, "FAILED", 0.0, "Hotel unavailable");
        }

        // 4. Calculate total cost
        double total = (flight.getPrice() != null ? flight.getPrice() : 0.0)
                + (hotel.getPrice() != null ? hotel.getPrice() : 0.0);

        // 5. Save booking as PENDING
        Booking booking = new Booking();
        booking.setUserId(req.getUserId());
        booking.setFlightId(req.getFlightId());
        booking.setHotelId(req.getHotelId());
        booking.setTravelDate(req.getTravelDate());
        booking.setTotalCost(total);
        booking.setStatus("PENDING");

        Booking saved = bookingRepository.save(booking);

        // 6. Call Payment Service
        String paymentUrl = paymentServiceUrl + "/payments";
        PaymentRequest payReq = new PaymentRequest(saved.getId(), saved.getTotalCost());

        try {
            PaymentResponse payResp = webClient.post()
                    .uri(paymentUrl)
                    .bodyValue(payReq)
                    .retrieve()
                    .bodyToMono(PaymentResponse.class)
                    .block();

            if (payResp != null && "SUCCESS".equalsIgnoreCase(payResp.getStatus())) {
                saved.setStatus("PAID");
                bookingRepository.save(saved);
            } else {
                saved.setStatus("FAILED");
                bookingRepository.save(saved);
                return new BookingResponse(saved.getId(), "FAILED", saved.getTotalCost(), "Payment failed");
            }
        } catch (Exception e) {
            saved.setStatus("FAILED");
            bookingRepository.save(saved);
            return new BookingResponse(saved.getId(), "FAILED", saved.getTotalCost(), "Payment service error: " + e.getMessage());
        }

        // 7. Call Notification Service
        String notifyUrl = notificationServiceUrl + "/notify";
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", saved.getUserId());
            payload.put("message", "Your booking (id: " + saved.getId() + ") is confirmed.");
            payload.put("type", "EMAIL");

            webClient.post()
                    .uri(notifyUrl)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Notification failed: " + e.getMessage());
        }

        // 8. Update booking to CONFIRMED
        saved.setStatus("CONFIRMED");
        bookingRepository.save(saved);

        return new BookingResponse(saved.getId(), "CONFIRMED", saved.getTotalCost(), "Booking confirmed");
    }

    // Update booking status (callback from Payment Service)
    public String updateBookingStatus(Long bookingId, String status) {
        return bookingRepository.findById(bookingId).map(b -> {
            b.setStatus(status);
            bookingRepository.save(b);
            return "OK";
        }).orElse("NOT_FOUND");
    }

    // Get booking by ID
    public Optional<BookingResponse> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getStatus(),
                        b.getTotalCost(),
                        "Booking retrieved successfully"
                ));
    }
}
