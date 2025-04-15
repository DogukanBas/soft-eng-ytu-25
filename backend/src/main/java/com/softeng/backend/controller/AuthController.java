package com.softeng.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softeng.backend.dto.AuthDTOs;
import com.softeng.backend.model.User;
import com.softeng.backend.service.UserService;
import com.softeng.backend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthDTOs.RegistrationRequest request) {
        try {
            User user = new User();
            user.setPersonalNo(request.getPersonalNo());
            user.setEmail(request.getEmail());
            user.setUserType(request.getUserType());

            User registeredUser = userService.registerUser(user, request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthDTOs.LoginRequest request) {
        System.out.println("Login attempt with personalNoOrEmail: " + request.getPersonalNoOrEmail());
        logger.debug("Login attempt with personalNoOrEmail: {} and password: [PROTECTED]",
                request.getPersonalNoOrEmail());
        PasswordUtil pu = new PasswordUtil();
        logger.debug("Password hash for {}: {}",
                request.getPersonalNoOrEmail(),
                pu.hashPassword(request.getPassword()));

        boolean isAuthenticated = userService.authenticateUser(
                request.getPersonalNoOrEmail(),
                request.getPassword()
        );

        if (isAuthenticated) {
            User user = userService.getUserByPersonalNoOrEmail(request.getPersonalNoOrEmail());

            AuthDTOs.LoginResponse response = new AuthDTOs.LoginResponse(
                    user.getPersonalNo(),
                    user.getEmail(),
                    user.getUserType()
            );
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}

