package com.jayakulan.urbannest.controller;

import com.jayakulan.urbannest.entity.Booking;
import com.jayakulan.urbannest.entity.Property;
import com.jayakulan.urbannest.repository.BookingRepository;
import com.jayakulan.urbannest.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        if (booking.getStatus() == null) {
            booking.setStatus("PENDING");
        }
        Booking savedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(savedBooking);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Booking>> getBookingsByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(bookingRepository.findByTenantId(tenantId));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Booking>> getBookingsByProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(bookingRepository.findByPropertyId(propertyId));
    }

    // Admin: get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Booking> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    String newStatus = status.toUpperCase();
                    booking.setStatus(newStatus);
                    Booking saved = bookingRepository.save(booking);

                    // Auto-update property availability based on booking status
                    propertyRepository.findById(booking.getPropertyId()).ifPresent(property -> {
                        if ("APPROVED".equals(newStatus)) {
                            property.setAvailabilityStatus("RENTED");
                        } else if ("REJECTED".equals(newStatus) || "COMPLETED".equals(newStatus)) {
                            property.setAvailabilityStatus("Available");
                        }
                        propertyRepository.save(property);
                    });

                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

