package com.jayakulan.urbannest.service;

import com.jayakulan.urbannest.dto.*;
import com.jayakulan.urbannest.entity.Role;
import com.jayakulan.urbannest.entity.User;
import com.jayakulan.urbannest.repository.UserRepository;
import com.jayakulan.urbannest.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse registerUser(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("Email already exists");
        }

        if (userRepository.existsByPhoneNo(request.getPhoneNo())) {
            return new AuthResponse("Phone number already exists");
        }

        String roleValue = request.getRole() != null ? request.getRole().toUpperCase() : "";
        if (!roleValue.equals("TENANT") && !roleValue.equals("OWNER")) {
            return new AuthResponse("Invalid role. Must be TENANT or OWNER.");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNo(request.getPhoneNo());
        user.setAddress(request.getAddress());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(roleValue));

        try {
            userRepository.save(user);
        } catch (Exception e) {
            return new AuthResponse("Registration failed: " + e.getMessage());
        }

        return new AuthResponse("User registered successfully");
    }

    public LoginResponse loginUser(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse(
                token,
                "Login successful",
                user.getRole().name(),
                user.getEmail()
        );
    }
}
