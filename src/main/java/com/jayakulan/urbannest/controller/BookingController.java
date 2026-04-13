package com.jayakulan.urbannest.controller;

import com.jayakulan.urbannest.dto.BookingDTO;
import com.jayakulan.urbannest.entity.Booking;
import com.jayakulan.urbannest.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.create(booking));
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<BookingDTO>> getByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(bookingService.getByTenant(tenantId));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<BookingDTO>> getByProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(bookingService.getByProperty(propertyId));
    }

    @GetMapping("/owner/{ownerId}/earnings")
    public ResponseEntity<List<BookingDTO>> getOwnerEarnings(@PathVariable Long ownerId) {
        return ResponseEntity.ok(bookingService.getOwnerEarnings(ownerId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return bookingService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
