package com.landscape.server.service;

import com.landscape.server.exception.ResourceNotFoundException;
import com.landscape.server.model.dto.auth.UserMeResponse;
import com.landscape.server.model.dto.auth.UserProfileRequestDto;
import com.landscape.server.repository.UserRepository;
import com.landscape.server.model.User;
import com.landscape.server.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // create User
    public User create(String email, String passwordHash, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setRole(role);

        return userRepository.save(user);
    }

    // registration for users
    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(this.passwordEncoder.encode(password));
        user.setRole("ROLE_ADMIN");

        return userRepository.save(user);
    }

    // getAll
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // update logged in user
    public UserMeResponse update(Integer userId, UserProfileRequestDto dto) {

        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getEmail() != null) {
            existing.setEmail(dto.getEmail());
        }

        User saved = userRepository.save(existing);

        UserMeResponse userMeResponse = new UserMeResponse();
        userMeResponse.setId(saved.getId());
        userMeResponse.setEmail(saved.getEmail());

        return userMeResponse;
    }
}

