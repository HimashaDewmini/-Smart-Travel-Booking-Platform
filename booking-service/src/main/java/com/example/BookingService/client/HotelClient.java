package com.example.BookingService.client;

import com.example.BookingService.DTO.HotelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hotel-service", url = "${hotel.service.url}")
public interface HotelClient {
    @GetMapping("/hotels/{id}")
    HotelResponse getHotel(@PathVariable("id") Long id);
}
