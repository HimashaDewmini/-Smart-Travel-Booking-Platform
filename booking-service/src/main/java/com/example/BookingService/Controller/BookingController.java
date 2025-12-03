package com.example.BookingService.Controller;

import com.example.BookingService.DTO.BookingRequest;
import com.example.BookingService.DTO.BookingResponse;
import com.example.BookingService.Service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    // 1️⃣ Create booking
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest req) {
        BookingResponse resp = service.createBooking(req);
        if ("FAILED".equalsIgnoreCase(resp.getStatus())) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    // 2️⃣ Update booking status (callback from Payment Service)
    @PostMapping("/update-status")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> payload) {
        Long bookingId = Long.valueOf(String.valueOf(payload.get("bookingId")));
        String status = String.valueOf(payload.get("status"));
        String res = service.updateBookingStatus(bookingId, status);
        if ("NOT_FOUND".equals(res)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("OK");
    }

    // 3️⃣ Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        Optional<BookingResponse> resp = service.getBookingById(id);
        return resp.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
