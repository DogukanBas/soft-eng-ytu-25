package com.softeng.backend.service.impl;

import com.softeng.backend.exception.ResourceNotFoundException;
import com.softeng.backend.model.User;
import com.softeng.backend.repository.UserRepository;
import com.softeng.backend.service.UserService;
import com.softeng.backend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
    }

    @Override
    @Transactional
    public void addUser(User user, String plainPassword) {

        try {
            String hashedPassword = passwordUtil.hashPassword(plainPassword);
            user.setPasswordHash(hashedPassword);
            userRepository.save(user);
            System.out.println("User added successfully: " + user.getPersonalNo() + ", " + user.getEmail() + ", " + user.getUserType());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while adding user: " + e.getMessage());
        }
    }

    @Override
    public boolean authenticateUser(String personalNoOrEmail, String plainPassword) {
        Optional<User> user = userRepository.findByPersonalNo(personalNoOrEmail)
                .or(() -> userRepository.findByEmail(personalNoOrEmail));

        return user.map(u -> passwordUtil.verifyPassword(plainPassword, u.getPasswordHash()))
                .orElse(false);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public User getUserByPersonalNo(String personalNo) {
        return userRepository.findByPersonalNo(personalNo)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with personal number: " + personalNo));
    }

    @Override
    public User getUserByPersonalNoOrEmail(String personalNoOrEmail) {
        return userRepository.findByPersonalNo(personalNoOrEmail)
                .or(() -> userRepository.findByEmail(personalNoOrEmail))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with personal number or email: " + personalNoOrEmail));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByPersonalNo(String personalNo) {
        return userRepository.existsByPersonalNo(personalNo);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}