package com.example.HotelService.Service;

import com.example.HotelService.DTO.HotelAvailabilityDTO;
import com.example.HotelService.DTO.HotelDTO;
import com.example.HotelService.Entity.Hotel;
import com.example.HotelService.Exceptions.HotelNotFoundException;
import com.example.HotelService.Repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository repo;

    public HotelService(HotelRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public HotelDTO getHotelById(Long id) {
        Hotel h = repo.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel with id " + id + " not found"));

        return toDTO(h);
    }

    @Transactional(readOnly = true)
    public List<HotelDTO> getAllHotels() {
        return repo.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public HotelAvailabilityDTO checkAvailability(Long id) {
        Hotel h = repo.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel with id " + id + " not found"));
        return new HotelAvailabilityDTO(h.getPrice(), h.getAvailableRooms());
    }

    @Transactional
    public Hotel createHotel(Hotel hotel) {
        return repo.save(hotel);
    }

    // -------------------------
    // UPDATE HOTEL
    // -------------------------
    @Transactional
    public HotelDTO updateHotel(Long id, HotelDTO dto) {
        Hotel existing = repo.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel with id " + id + " not found"));

        existing.setHotelName(dto.getHotelName());
        existing.setPrice(dto.getPrice());
        existing.setAvailableRooms(dto.getAvailableRooms());
        existing.setLocation(dto.getLocation());

        Hotel updated = repo.save(existing);
        return toDTO(updated);
    }

    // -------------------------
    // DELETE HOTEL
    // -------------------------
    @Transactional
    public void deleteHotel(Long id) {
        Hotel hotel = repo.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel with id " + id + " not found"));
        repo.delete(hotel);
    }

    // Helper method to convert Entity to DTO
    private HotelDTO toDTO(Hotel h) {
        return new HotelDTO(
                h.getId(),
                h.getPrice(),
                h.getAvailableRooms(),
                h.getHotelName(),
                h.getLocation()
        );
    }
}
