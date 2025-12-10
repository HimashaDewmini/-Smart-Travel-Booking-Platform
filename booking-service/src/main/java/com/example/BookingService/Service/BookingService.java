package com.example.BookingService.Service;

import com.example.BookingService.DTO.BookingRequest;
import com.example.BookingService.DTO.BookingResponse;
import com.example.BookingService.Entity.Booking;
import com.example.BookingService.Repository.BookingRepository;
import com.example.BookingService.client.FlightClient;
import com.example.BookingService.client.HotelClient;
import com.example.BookingService.client.NotificationClient;
import com.example.BookingService.DTO.FlightResponse;
import com.example.BookingService.DTO.HotelResponse;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final FlightClient flightClient;
    private final HotelClient hotelClient;
    private final NotificationClient notificationClient;

    public BookingService(BookingRepository bookingRepo, FlightClient flightClient,
                          HotelClient hotelClient, NotificationClient notificationClient) {
        this.bookingRepo = bookingRepo;
        this.flightClient = flightClient;
        this.hotelClient = hotelClient;
        this.notificationClient = notificationClient;
    }

    /**
     * Create a new booking with initial status "PENDING"
     */
    @Transactional
    public BookingResponse createBooking(BookingRequest req) {
        FlightResponse flight;
        HotelResponse hotel;

        // 1️⃣ Check flight availability
        try {
            flight = flightClient.getFlight(req.getFlightId());
        } catch (FeignException.NotFound e) {
            return new BookingResponse(null, "FAILED", 0.0, "Flight not found");
        }

        // 2️⃣ Check hotel availability
        try {
            hotel = hotelClient.getHotel(req.getHotelId());
        } catch (FeignException.NotFound e) {
            return new BookingResponse(null, "FAILED", 0.0, "Hotel not found");
        }

        // 3️⃣ Calculate total cost
        double totalCost = flight.getPrice() + hotel.getPrice();

        // 4️⃣ Save booking as PENDING
        Booking booking = new Booking();
        booking.setUserId(req.getUserId());
        booking.setFlightId(req.getFlightId());
        booking.setHotelId(req.getHotelId());
        booking.setTravelDate(req.getTravelDate());
        booking.setTotalCost(totalCost);
        booking.setStatus("PENDING");
        booking = bookingRepo.save(booking);

        return new BookingResponse(booking.getId(), "PENDING", totalCost, "Booking created successfully");
    }

    /**
     * Update booking status (called by PaymentService after payment)
     * Automatically sends notification if status is CONFIRMED
     */
    @Transactional
    public String updateBookingStatus(Long bookingId, String status) {
        try {
            Optional<Booking> opt = bookingRepo.findById(bookingId);
            if (opt.isEmpty()) return "NOT_FOUND";

            Booking booking = opt.get();
            booking.setStatus(status);
            bookingRepo.save(booking);

            // ✅ Automatically send notification if booking is CONFIRMED
            if ("CONFIRMED".equalsIgnoreCase(status)) {
                notificationClient.sendBookingConfirmation(
                        booking.getUserId(),
                        "Your booking (ID: " + booking.getId() + ") has been confirmed!"
                );
            }

            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    /**
     * Fetch a booking by ID
     */
    @Transactional(readOnly = true)
    public Optional<BookingResponse> getBookingById(Long id) {
        return bookingRepo.findById(id)
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getStatus(),
                        b.getTotalCost(),
                        "Booking fetched successfully"
                ));
    }

    /**
     * Fetch all bookings
     */
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepo.findAll().stream()
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getStatus(),
                        b.getTotalCost(),
                        "Booking fetched successfully"
                ))
                .toList();
    }
}
