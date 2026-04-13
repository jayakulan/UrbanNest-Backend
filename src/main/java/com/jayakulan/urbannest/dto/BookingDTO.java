package com.jayakulan.urbannest.dto;

public class BookingDTO {
    private Long    id;
    private Long    tenantId;
    private String  tenantName;       // enriched
    private Long    propertyId;
    private String  propertyName;     // enriched
    private String  propertyPhoto;    // enriched
    private Long    ownerId;          // enriched
    private String  ownerName;        // enriched
    private String  moveInDate;
    private String  moveOutDate;
    private String  rentalDuration;
    private Integer occupants;
    private String  message;
    private Double  monthlyRent;
    private Double  serviceFee;
    private Double  totalAmount;
    private String  status;

    public BookingDTO() {}

    public Long    getId()            { return id; }
    public Long    getTenantId()      { return tenantId; }
    public String  getTenantName()    { return tenantName; }
    public Long    getPropertyId()    { return propertyId; }
    public String  getPropertyName()  { return propertyName; }
    public String  getPropertyPhoto() { return propertyPhoto; }
    public Long    getOwnerId()       { return ownerId; }
    public String  getOwnerName()     { return ownerName; }
    public String  getMoveInDate()    { return moveInDate; }
    public String  getMoveOutDate()   { return moveOutDate; }
    public String  getRentalDuration(){ return rentalDuration; }
    public Integer getOccupants()     { return occupants; }
    public String  getMessage()       { return message; }
    public Double  getMonthlyRent()   { return monthlyRent; }
    public Double  getServiceFee()    { return serviceFee; }
    public Double  getTotalAmount()   { return totalAmount; }
    public String  getStatus()        { return status; }

    public void setId(Long v)              { this.id = v; }
    public void setTenantId(Long v)        { this.tenantId = v; }
    public void setTenantName(String v)    { this.tenantName = v; }
    public void setPropertyId(Long v)      { this.propertyId = v; }
    public void setPropertyName(String v)  { this.propertyName = v; }
    public void setPropertyPhoto(String v) { this.propertyPhoto = v; }
    public void setOwnerId(Long v)         { this.ownerId = v; }
    public void setOwnerName(String v)     { this.ownerName = v; }
    public void setMoveInDate(String v)    { this.moveInDate = v; }
    public void setMoveOutDate(String v)   { this.moveOutDate = v; }
    public void setRentalDuration(String v){ this.rentalDuration = v; }
    public void setOccupants(Integer v)    { this.occupants = v; }
    public void setMessage(String v)       { this.message = v; }
    public void setMonthlyRent(Double v)   { this.monthlyRent = v; }
    public void setServiceFee(Double v)    { this.serviceFee = v; }
    public void setTotalAmount(Double v)   { this.totalAmount = v; }
    public void setStatus(String v)        { this.status = v; }
}
