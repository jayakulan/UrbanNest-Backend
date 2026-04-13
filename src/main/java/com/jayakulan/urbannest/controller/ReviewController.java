package com.jayakulan.urbannest.controller;

import com.jayakulan.urbannest.dto.ReviewDTO;
import com.jayakulan.urbannest.entity.Review;
import com.jayakulan.urbannest.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.createReview(review));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<ReviewDTO>> getByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(reviewService.getByTenant(tenantId));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ReviewDTO>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(reviewService.getByOwner(ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id)
                ? ResponseEntity.ok().<Void>build()
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/reply")
    public ResponseEntity<ReviewDTO> replyToReview(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return reviewService.replyToReview(id, body.get("reply"))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
