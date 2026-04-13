package com.jayakulan.urbannest.dto;

public class ReviewDTO {
    private Long    id;
    private Long    tenantId;
    private String  tenantName;
    private Long    propertyId;
    private String  propertyName;
    private String  propertyPhoto;
    private Integer rating;
    private String  content;
    private String  reviewDate;
    private String  ownerReply;

    public ReviewDTO() {}

    public Long    getId()           { return id; }
    public Long    getTenantId()     { return tenantId; }
    public String  getTenantName()   { return tenantName; }
    public Long    getPropertyId()   { return propertyId; }
    public String  getPropertyName() { return propertyName; }
    public String  getPropertyPhoto(){ return propertyPhoto; }
    public Integer getRating()       { return rating; }
    public String  getContent()      { return content; }
    public String  getReviewDate()   { return reviewDate; }
    public String  getOwnerReply()   { return ownerReply; }

    public void setId(Long v)             { this.id = v; }
    public void setTenantId(Long v)       { this.tenantId = v; }
    public void setTenantName(String v)   { this.tenantName = v; }
    public void setPropertyId(Long v)     { this.propertyId = v; }
    public void setPropertyName(String v) { this.propertyName = v; }
    public void setPropertyPhoto(String v){ this.propertyPhoto = v; }
    public void setRating(Integer v)      { this.rating = v; }
    public void setContent(String v)      { this.content = v; }
    public void setReviewDate(String v)   { this.reviewDate = v; }
    public void setOwnerReply(String v)   { this.ownerReply = v; }
}
