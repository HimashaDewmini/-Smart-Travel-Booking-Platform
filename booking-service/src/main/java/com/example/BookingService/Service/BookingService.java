package com.example.BookingService.Service;

import com.example.BookingService.DTO.BookingRequest;
import com.example.BookingService.DTO.BookingResponse;
import com.example.BookingService.Entity.Booking;
import com.example.BookingService.client.FlightClient;
import com.example.BookingService.client.HotelClient;
import com.example.BookingService.DTO.FlightResponse;
import com.example.BookingService.DTO.HotelResponse;
import com.example.BookingService.Repository.BookingRepository;
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

    public BookingService(BookingRepository bookingRepo, FlightClient flightClient, HotelClient hotelClient) {
        this.bookingRepo = bookingRepo;
        this.flightClient = flightClient;
        this.hotelClient = hotelClient;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest req) {
        FlightResponse flight;
        HotelResponse hotel;

        // 1️⃣ Check flight existence
        try {
            flight = flightClient.getFlight(req.getFlightId());
        } catch (FeignException.NotFound e) {
            return new BookingResponse(null, "FAILED", 0.0, "Flight not found");
        }

        // 2️⃣ Check hotel existence
        try {
            hotel = hotelClient.getHotel(req.getHotelId());
        } catch (FeignException.NotFound e) {
            return new BookingResponse(null, "FAILED", 0.0, "Hotel not found");
        }

        // 3️⃣ Calculate total cost
        double totalCost = flight.getPrice() + hotel.getPrice();

        // 4️⃣ Save booking
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

    @Transactional
    public String updateBookingStatus(Long bookingId, String status) {
        Optional<Booking> opt = bookingRepo.findById(bookingId);
        if (opt.isEmpty()) return "NOT_FOUND";

        Booking booking = opt.get();
        booking.setStatus(status);
        bookingRepo.save(booking);

        return "OK";
    }

    @Transactional(readOnly = true)
    public Optional<BookingResponse> getBookingById(Long id) {
        return bookingRepo.findById(id).map(b -> new BookingResponse(
                b.getId(),
                b.getStatus(),
                b.getTotalCost(),
                "Booking fetched successfully"
        ));
    }

    // ✅ Get all bookings
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
