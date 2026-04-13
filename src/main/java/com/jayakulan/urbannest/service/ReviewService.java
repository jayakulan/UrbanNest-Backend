package com.jayakulan.urbannest.service;

import com.jayakulan.urbannest.dto.ReviewDTO;
import com.jayakulan.urbannest.entity.Review;
import com.jayakulan.urbannest.repository.PropertyRepository;
import com.jayakulan.urbannest.repository.ReviewRepository;
import com.jayakulan.urbannest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired private ReviewRepository   reviewRepository;
    @Autowired private PropertyRepository propertyRepository;
    @Autowired private UserRepository     userRepository;

    // ── Mapper ────────────────────────────────────────────────────────────────
    public ReviewDTO toDTO(Review r) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(r.getId());
        dto.setTenantId(r.getTenantId());
        dto.setTenantName(r.getTenantName());
        dto.setPropertyId(r.getPropertyId());
        dto.setPropertyName(r.getPropertyName());
        dto.setPropertyPhoto(r.getPropertyPhoto());
        dto.setRating(r.getRating());
        dto.setContent(r.getContent());
        dto.setReviewDate(r.getReviewDate());
        dto.setOwnerReply(r.getOwnerReply());
        return dto;
    }

    // ── Business Logic ────────────────────────────────────────────────────────

    public ReviewDTO createReview(Review review) {
        // Auto-fill review date
        if (review.getReviewDate() == null) {
            review.setReviewDate(LocalDate.now().toString());
        }
        // Auto-fill property name + photo
        if (review.getPropertyId() != null) {
            propertyRepository.findById(review.getPropertyId()).ifPresent(p -> {
                if (review.getPropertyName() == null) review.setPropertyName(p.getTitle());
                if (review.getPropertyPhoto() == null) review.setPropertyPhoto(p.getPhotos());
            });
        }
        // Auto-fill tenant name
        if (review.getTenantId() != null && review.getTenantName() == null) {
            userRepository.findById(review.getTenantId())
                .ifPresent(u -> review.setTenantName(u.getFullName()));
        }
        return toDTO(reviewRepository.save(review));
    }

    public List<ReviewDTO> getByTenant(Long tenantId) {
        return reviewRepository.findByTenantId(tenantId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getByOwner(Long ownerId) {
        List<Long> propertyIds = propertyRepository.findByOwnerId(ownerId)
                .stream().map(p -> p.getId()).collect(Collectors.toList());
        return propertyIds.stream()
                .flatMap(pid -> reviewRepository.findByPropertyId(pid).stream())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public boolean deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) return false;
        reviewRepository.deleteById(id);
        return true;
    }

    public Optional<ReviewDTO> replyToReview(Long id, String reply) {
        return reviewRepository.findById(id).map(review -> {
            review.setOwnerReply(reply);
            return toDTO(reviewRepository.save(review));
        });
    }
}
