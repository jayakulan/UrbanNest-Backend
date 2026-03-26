package com.jayakulan.urbannest.dto;

public class LoginResponse {
    private String token;
    private String message;
    private String role;
    private String email;
    private Long id;

    public LoginResponse() {}

    public LoginResponse(String token, String message, String role, String email, Long id) {
        this.token = token;
        this.message = message;
        this.role = role;
        this.email = email;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}