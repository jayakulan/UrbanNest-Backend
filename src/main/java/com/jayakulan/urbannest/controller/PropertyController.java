package com.jayakulan.urbannest.controller;

import com.jayakulan.urbannest.entity.Property;
import com.jayakulan.urbannest.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        Property savedProperty = propertyRepository.save(property);
        return ResponseEntity.ok(savedProperty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Property>> getPropertiesByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(propertyRepository.findByOwnerId(ownerId));
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        return ResponseEntity.ok(propertyRepository.findAll());
    }

    // Admin: update property availability/approval status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Property> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Optional<Property> opt = propertyRepository.findById(id);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        Property property = opt.get();
        property.setAvailabilityStatus(body.get("status"));
        return ResponseEntity.ok(propertyRepository.save(property));
    }

    // Admin: delete a property
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        if (!propertyRepository.existsById(id)) return ResponseEntity.notFound().build();
        propertyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
