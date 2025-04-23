package com.softeng.backend.controller;

import com.softeng.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.softeng.backend.dto.AuthDTOs;
import com.softeng.backend.model.User;
import com.softeng.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final JwtUtil jwtUtil;

    private final UserService userService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthDTOs.LoginRequest request, HttpServletResponse response) {
        logger.debug("Login attempt with personalNoOrEmail: {}",
                request.getPersonalNoOrEmail());

        boolean isAuthenticated = userService.authenticateUser(
                request.getPersonalNoOrEmail(),
                request.getPassword()
        );

        if (isAuthenticated) {
            User user = userService.getUserByPersonalNoOrEmail(request.getPersonalNoOrEmail());

            String accessToken = jwtUtil.generateAccessToken(user);

            AuthDTOs.LoginResponse loginResponse = new AuthDTOs.LoginResponse(
                    user.getPersonalNo(),
                    user.getEmail(),
                    user.getUserType(),
                    accessToken
            );
            return ResponseEntity.ok().body(loginResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}

