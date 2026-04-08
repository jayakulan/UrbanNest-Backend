package com.jayakulan.urbannest.repository;

import com.jayakulan.urbannest.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTenantId(Long tenantId);
    List<Review> findByPropertyId(Long propertyId);
}
