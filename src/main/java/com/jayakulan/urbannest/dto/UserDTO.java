package com.jayakulan.urbannest.dto;

/** Read-only DTO — never expose the User entity (contains password hash) */
public class UserDTO {
    private Long   id;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String status;
    private String createdAt;

    public UserDTO() {}

    public Long   getId()        { return id; }
    public String getFullName()  { return fullName; }
    public String getEmail()     { return email; }
    public String getPhone()     { return phone; }
    public String getRole()      { return role; }
    public String getStatus()    { return status; }
    public String getCreatedAt() { return createdAt; }

    public void setId(Long id)              { this.id = id; }
    public void setFullName(String v)       { this.fullName = v; }
    public void setEmail(String v)          { this.email = v; }
    public void setPhone(String v)          { this.phone = v; }
    public void setRole(String v)           { this.role = v; }
    public void setStatus(String v)         { this.status = v; }
    public void setCreatedAt(String v)      { this.createdAt = v; }
}
