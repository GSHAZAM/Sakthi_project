package com.urbandrone.delivery.service;

import com.urbandrone.delivery.model.User;
import com.urbandrone.delivery.model.UserRole;
import com.urbandrone.delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email address is already registered!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(UserRole.CUSTOMER);
        }
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findCustomers() {
        return userRepository.findByRole(UserRole.CUSTOMER);
    }

    public long countTotalCustomers() {
        return userRepository.countByRole(UserRole.CUSTOMER);
    }

    public User updateUserProfile(Long userId, String name, String phone, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setName(name);
        user.setPhone(phone);
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        return userRepository.save(user);
    }
}
