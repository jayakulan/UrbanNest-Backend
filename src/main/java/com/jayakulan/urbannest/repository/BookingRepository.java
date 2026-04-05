package com.jayakulan.urbannest.repository;

import com.jayakulan.urbannest.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTenantId(Long tenantId);
    List<Booking> findByPropertyId(Long propertyId);
}
