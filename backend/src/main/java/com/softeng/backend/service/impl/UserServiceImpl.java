package com.softeng.backend.service.impl;

import com.softeng.backend.exception.ResourceNotFoundException;
import com.softeng.backend.model.User;
import com.softeng.backend.repository.UserRepository;
import com.softeng.backend.service.UserService;
import com.softeng.backend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public User registerUser(User user, String plainPassword) {
        if (userRepository.existsByPersonalNo(user.getPersonalNo())) {
            throw new IllegalArgumentException("Personal number already registered");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hashedPassword = passwordUtil.hashPassword(plainPassword);
        user.setPasswordHash(hashedPassword);

        return userRepository.save(user);
    }

    @Override
    public boolean authenticateUser(String personalNoOrEmail, String plainPassword) {
        Optional<User> user = userRepository.findByPersonalNo(personalNoOrEmail)
                .or(() -> userRepository.findByEmail(personalNoOrEmail));

        return user.map(u -> passwordUtil.verifyPassword(plainPassword, u.getPasswordHash()))
                .orElse(false);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
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
    public User updateUser(Long userId, User userDetails) {
        User user = getUserById(userId);

        user.setPersonalNo(userDetails.getPersonalNo());
        user.setEmail(userDetails.getEmail());
        user.setPasswordHash(userDetails.getPasswordHash());
        user.setUserType(userDetails.getUserType());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
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