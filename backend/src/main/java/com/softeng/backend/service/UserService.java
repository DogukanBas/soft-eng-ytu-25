package com.softeng.backend.service;

import com.softeng.backend.model.User;
import java.util.List;

public interface UserService {
    void addUser(User user, String plainPassword);
    boolean authenticateUser(String personalNoOrEmail, String plainPassword);
    User getUserById(Long userId);
    User getUserByPersonalNo(String personalNo);
    User getUserByPersonalNoOrEmail(String personalNoOrEmail);
    List<User> getAllUsers();
    boolean existsByPersonalNo(String personalNo);
    boolean existsByEmail(String email);

}