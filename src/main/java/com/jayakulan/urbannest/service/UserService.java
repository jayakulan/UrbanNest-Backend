package com.jayakulan.urbannest.service;

import com.jayakulan.urbannest.dto.UserDTO;
import com.jayakulan.urbannest.entity.Role;
import com.jayakulan.urbannest.entity.User;
import com.jayakulan.urbannest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ── Mapper — uses actual User entity field names ───────────────────────────
    public UserDTO toDTO(User u) {
        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setFullName(u.getFullName());
        dto.setEmail(u.getEmail());
        dto.setPhone(u.getPhoneNo());          // entity field is phoneNo
        dto.setRole(u.getRole() != null ? u.getRole().name() : null);
        // status and createdAt not in entity — leave null (safe defaults)
        return dto;
    }

    // ── Business Logic ────────────────────────────────────────────────────────

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::toDTO);
    }

    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }

    public Optional<UserDTO> changeRole(Long id, String roleName) {
        return userRepository.findById(id).map(user -> {
            try {
                user.setRole(Role.valueOf(roleName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + roleName);
            }
            return toDTO(userRepository.save(user));
        });
    }
}
