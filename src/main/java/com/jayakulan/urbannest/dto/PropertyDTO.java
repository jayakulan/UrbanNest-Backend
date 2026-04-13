package com.jayakulan.urbannest.dto;

public class PropertyDTO {
    private Long    id;
    private Long    ownerId;
    private String  ownerName;        // enriched — not in entity
    private String  title;
    private String  propertyType;
    private String  category;
    private Double  monthlyRent;
    private String  availabilityStatus;
    private String  address;
    private String  city;
    private String  district;
    private String  zipCode;
    private String  description;
    private Integer bedrooms;
    private Integer bathrooms;
    private Double  areaSize;
    private String  amenities;
    private String  photos;

    public PropertyDTO() {}

    public Long    getId()                 { return id; }
    public Long    getOwnerId()            { return ownerId; }
    public String  getOwnerName()          { return ownerName; }
    public String  getTitle()              { return title; }
    public String  getPropertyType()       { return propertyType; }
    public String  getCategory()           { return category; }
    public Double  getMonthlyRent()        { return monthlyRent; }
    public String  getAvailabilityStatus() { return availabilityStatus; }
    public String  getAddress()            { return address; }
    public String  getCity()               { return city; }
    public String  getDistrict()           { return district; }
    public String  getZipCode()            { return zipCode; }
    public String  getDescription()        { return description; }
    public Integer getBedrooms()           { return bedrooms; }
    public Integer getBathrooms()          { return bathrooms; }
    public Double  getAreaSize()           { return areaSize; }
    public String  getAmenities()          { return amenities; }
    public String  getPhotos()             { return photos; }

    public void setId(Long v)                  { this.id = v; }
    public void setOwnerId(Long v)             { this.ownerId = v; }
    public void setOwnerName(String v)         { this.ownerName = v; }
    public void setTitle(String v)             { this.title = v; }
    public void setPropertyType(String v)      { this.propertyType = v; }
    public void setCategory(String v)          { this.category = v; }
    public void setMonthlyRent(Double v)       { this.monthlyRent = v; }
    public void setAvailabilityStatus(String v){ this.availabilityStatus = v; }
    public void setAddress(String v)           { this.address = v; }
    public void setCity(String v)              { this.city = v; }
    public void setDistrict(String v)          { this.district = v; }
    public void setZipCode(String v)           { this.zipCode = v; }
    public void setDescription(String v)       { this.description = v; }
    public void setBedrooms(Integer v)         { this.bedrooms = v; }
    public void setBathrooms(Integer v)        { this.bathrooms = v; }
    public void setAreaSize(Double v)          { this.areaSize = v; }
    public void setAmenities(String v)         { this.amenities = v; }
    public void setPhotos(String v)            { this.photos = v; }
}
