package com.jayakulan.urbannest.service;

import com.jayakulan.urbannest.dto.BookingDTO;
import com.jayakulan.urbannest.entity.Booking;
import com.jayakulan.urbannest.repository.BookingRepository;
import com.jayakulan.urbannest.repository.PropertyRepository;
import com.jayakulan.urbannest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired private BookingRepository  bookingRepository;
    @Autowired private PropertyRepository propertyRepository;
    @Autowired private UserRepository     userRepository;

    // ── Mapper ────────────────────────────────────────────────────────────────
    public BookingDTO toDTO(Booking b) {
        BookingDTO dto = new BookingDTO();
        dto.setId(b.getId());
        dto.setTenantId(b.getTenantId());
        dto.setPropertyId(b.getPropertyId());
        dto.setMoveInDate(b.getMoveInDate());
        dto.setRentalDuration(b.getRentalDuration());
        dto.setOccupants(b.getOccupants());
        dto.setMessage(b.getMessage());
        dto.setMonthlyRent(b.getMonthlyRent());
        dto.setServiceFee(b.getServiceFee());
        dto.setTotalAmount(b.getTotalAmount());
        dto.setStatus(b.getStatus());

        // Enrich tenant name
        if (b.getTenantId() != null) {
            userRepository.findById(b.getTenantId())
                .ifPresent(t -> dto.setTenantName(t.getFullName()));
        }

        // Enrich property name, photo, owner
        if (b.getPropertyId() != null) {
            propertyRepository.findById(b.getPropertyId()).ifPresent(p -> {
                dto.setPropertyName(p.getTitle());
                dto.setPropertyPhoto(p.getPhotos());
                dto.setOwnerId(p.getOwnerId());
                if (p.getOwnerId() != null) {
                    userRepository.findById(p.getOwnerId())
                        .ifPresent(o -> dto.setOwnerName(o.getFullName()));
                }
            });
        }
        return dto;
    }

    // ── Business Logic ────────────────────────────────────────────────────────

    public BookingDTO create(Booking booking) {
        if (booking.getStatus() == null) booking.setStatus("PENDING");
        return toDTO(bookingRepository.save(booking));
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getByTenant(Long tenantId) {
        return bookingRepository.findByTenantId(tenantId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getByProperty(Long propertyId) {
        return bookingRepository.findByPropertyId(propertyId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<BookingDTO> updateStatus(Long id, String status) {
        return bookingRepository.findById(id).map(booking -> {
            String newStatus = status.toUpperCase();
            booking.setStatus(newStatus);
            bookingRepository.save(booking);

            // Auto-update property availability
            propertyRepository.findById(booking.getPropertyId()).ifPresent(property -> {
                if ("APPROVED".equals(newStatus)) {
                    property.setAvailabilityStatus("RENTED");
                } else if ("REJECTED".equals(newStatus) || "COMPLETED".equals(newStatus)) {
                    property.setAvailabilityStatus("Available");
                }
                propertyRepository.save(property);
            });

            return toDTO(booking);
        });
    }

    public List<BookingDTO> getOwnerEarnings(Long ownerId) {
        return propertyRepository.findByOwnerId(ownerId).stream()
                .flatMap(prop -> bookingRepository.findByPropertyId(prop.getId()).stream())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
