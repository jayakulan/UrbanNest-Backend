package com.jayakulan.urbannest.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId;

    @Column(nullable = false)
    private Long propertyId;

    private String tenantName;
    private String propertyName;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String reviewDate;

    // Owner's reply to the review
    @Column(columnDefinition = "TEXT")
    private String ownerReply;

    // Photo URL from property — used for display in review history
    private String propertyPhoto;

    public Review() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getPropertyName() { return propertyName; }
    public void setPropertyName(String propertyName) { this.propertyName = propertyName; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getReviewDate() { return reviewDate; }
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }

    public String getOwnerReply() { return ownerReply; }
    public void setOwnerReply(String ownerReply) { this.ownerReply = ownerReply; }

    public String getPropertyPhoto() { return propertyPhoto; }
    public void setPropertyPhoto(String propertyPhoto) { this.propertyPhoto = propertyPhoto; }
}
