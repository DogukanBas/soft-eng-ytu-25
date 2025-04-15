package com.softeng.backend.service;

import com.softeng.backend.model.User;
import java.util.List;

public interface UserService {
    User registerUser(User user, String plainPassword);
    boolean authenticateUser(String personalNoOrEmail, String plainPassword);
    User createUser(User user);
    User getUserById(Long userId);
    User getUserByPersonalNo(String personalNo);
    User getUserByPersonalNoOrEmail(String personalNoOrEmail);
    List<User> getAllUsers();
    User updateUser(Long userId, User userDetails);
    void deleteUser(Long userId);
    boolean existsByPersonalNo(String personalNo);
    boolean existsByEmail(String email);

}