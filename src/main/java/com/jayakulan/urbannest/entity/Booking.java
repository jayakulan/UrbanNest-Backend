package com.jayakulan.urbannest.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId;

    @Column(nullable = false)
    private Long propertyId;

    private String moveInDate;
    private String rentalDuration;
    private Integer occupants;
    
    @Column(length = 2000)
    private String message;
    
    private Double monthlyRent;
    private Double serviceFee;
    private Double totalAmount;
    
    private String status;

    public Booking() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public String getMoveInDate() { return moveInDate; }
    public void setMoveInDate(String moveInDate) { this.moveInDate = moveInDate; }

    public String getRentalDuration() { return rentalDuration; }
    public void setRentalDuration(String rentalDuration) { this.rentalDuration = rentalDuration; }

    public Integer getOccupants() { return occupants; }
    public void setOccupants(Integer occupants) { this.occupants = occupants; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Double getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(Double monthlyRent) { this.monthlyRent = monthlyRent; }

    public Double getServiceFee() { return serviceFee; }
    public void setServiceFee(Double serviceFee) { this.serviceFee = serviceFee; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
