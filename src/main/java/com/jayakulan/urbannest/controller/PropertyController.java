package com.jayakulan.urbannest.controller;

import com.jayakulan.urbannest.dto.PropertyDTO;
import com.jayakulan.urbannest.entity.Property;
import com.jayakulan.urbannest.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getPropertyById(@PathVariable Long id) {
        return propertyService.getPropertyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<PropertyDTO>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(propertyService.getPropertiesByOwner(ownerId));
    }

    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(@RequestBody Property property) {
        return ResponseEntity.ok(propertyService.saveProperty(property));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(
            @PathVariable Long id,
            @RequestBody Property property) {
        property.setId(id);
        return ResponseEntity.ok(propertyService.saveProperty(property));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PropertyDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return propertyService.updateStatus(id, body.get("status"))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        return propertyService.deleteProperty(id)
                ? ResponseEntity.ok().<Void>build()
                : ResponseEntity.notFound().build();
    }
}
