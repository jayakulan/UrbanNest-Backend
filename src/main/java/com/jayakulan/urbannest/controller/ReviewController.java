package com.jayakulan.urbannest.controller;

import com.jayakulan.urbannest.entity.Review;
import com.jayakulan.urbannest.repository.ReviewRepository;
import com.jayakulan.urbannest.repository.PropertyRepository;
import com.jayakulan.urbannest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    // Submit a review (tenant)
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        // Auto-fill names from DB if not provided
        if (review.getReviewDate() == null) {
            review.setReviewDate(LocalDate.now().toString());
        }
        if (review.getPropertyName() == null && review.getPropertyId() != null) {
            propertyRepository.findById(review.getPropertyId()).ifPresent(p ->
                review.setPropertyName(p.getTitle())
            );
        }
        if (review.getTenantName() == null && review.getTenantId() != null) {
            userRepository.findById(review.getTenantId()).ifPresent(u ->
                review.setTenantName(u.getFullName())
            );
        }
        // Attach property photo for the history card
        if (review.getPropertyPhoto() == null && review.getPropertyId() != null) {
            propertyRepository.findById(review.getPropertyId()).ifPresent(p ->
                review.setPropertyPhoto(p.getPhotos())
            );
        }
        return ResponseEntity.ok(reviewRepository.save(review));
    }

    // Get all reviews submitted by a tenant (for tenant review history)
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Review>> getByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(reviewRepository.findByTenantId(tenantId));
    }

    // Get reviews for all properties owned by an owner (owner reviews page)
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Review>> getByOwner(@PathVariable Long ownerId) {
        // Collect all propertyIds for this owner, then fetch reviews for each
        List<Long> propertyIds = propertyRepository.findByOwnerId(ownerId)
                .stream()
                .map(p -> p.getId())
                .toList();

        List<Review> ownerReviews = propertyIds.stream()
                .flatMap(pid -> reviewRepository.findByPropertyId(pid).stream())
                .toList();

        return ResponseEntity.ok(ownerReviews);
    }

    // Delete a review (tenant)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Owner replies to a review
    @PatchMapping("/{id}/reply")
    public ResponseEntity<Review> replyToReview(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setOwnerReply(body.get("reply"));
                    return ResponseEntity.ok(reviewRepository.save(review));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
