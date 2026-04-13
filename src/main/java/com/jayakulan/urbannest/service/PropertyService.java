package com.jayakulan.urbannest.service;

import com.jayakulan.urbannest.dto.PropertyDTO;
import com.jayakulan.urbannest.entity.Property;
import com.jayakulan.urbannest.repository.PropertyRepository;
import com.jayakulan.urbannest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    @Autowired private PropertyRepository propertyRepository;
    @Autowired private UserRepository     userRepository;

    // ── Mapper ────────────────────────────────────────────────────────────────
    public PropertyDTO toDTO(Property p) {
        PropertyDTO dto = new PropertyDTO();
        dto.setId(p.getId());
        dto.setOwnerId(p.getOwnerId());
        dto.setTitle(p.getTitle());
        dto.setPropertyType(p.getPropertyType());
        dto.setCategory(p.getCategory());
        dto.setMonthlyRent(p.getMonthlyRent());
        dto.setAvailabilityStatus(p.getAvailabilityStatus());
        dto.setAddress(p.getAddress());
        dto.setCity(p.getCity());
        dto.setDistrict(p.getDistrict());
        dto.setZipCode(p.getZipCode());
        dto.setDescription(p.getDescription());
        dto.setBedrooms(p.getBedrooms());
        dto.setBathrooms(p.getBathrooms());
        dto.setAreaSize(p.getAreaSize());
        dto.setAmenities(p.getAmenities());
        dto.setPhotos(p.getPhotos());

        // Enrich with owner's name
        if (p.getOwnerId() != null) {
            userRepository.findById(p.getOwnerId())
                .ifPresent(owner -> dto.setOwnerName(owner.getFullName()));
        }
        return dto;
    }

    // ── Business Logic ────────────────────────────────────────────────────────

    public List<PropertyDTO> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<PropertyDTO> getPropertyById(Long id) {
        return propertyRepository.findById(id).map(this::toDTO);
    }

    public List<PropertyDTO> getPropertiesByOwner(Long ownerId) {
        return propertyRepository.findByOwnerId(ownerId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PropertyDTO saveProperty(Property property) {
        return toDTO(propertyRepository.save(property));
    }

    public Optional<PropertyDTO> updateStatus(Long id, String status) {
        return propertyRepository.findById(id).map(p -> {
            p.setAvailabilityStatus(status);
            return toDTO(propertyRepository.save(p));
        });
    }

    public boolean deleteProperty(Long id) {
        if (!propertyRepository.existsById(id)) return false;
        propertyRepository.deleteById(id);
        return true;
    }
}
