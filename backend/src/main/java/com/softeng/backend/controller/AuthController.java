package com.softeng.backend.controller;

import com.softeng.backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.softeng.backend.dto.AuthDTOs;
import com.softeng.backend.model.User;
import com.softeng.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> registerUser(@RequestBody AuthDTOs.RegistrationRequest request, Authentication authentication) {
        logger.debug("Registering user with personalNo: {}", request.getPersonalNo());
        String personaNo = authentication.getName();
        logger.debug("Current user: {}", personaNo);
        User currentUser = userService.getUserByPersonalNo(personaNo);

        if (currentUser.getUserType() != User.UserType.admin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can register new users");
        }

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
            String refreshToken = jwtUtil.generateRefreshToken(user);

            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 days
                    .secure(false)
                    .build();
            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

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

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken.isEmpty() || !jwtUtil.validateToken(refreshToken) || jwtUtil.isExpired(refreshToken) ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
        }

        String personalNo = jwtUtil.extractSubject(refreshToken);
        User user = userService.getUserByPersonalNo(personalNo);

        String newAccessToken = jwtUtil.generateAccessToken(user);
        return ResponseEntity.ok(new AuthDTOs.LoginResponse(
                user.getPersonalNo(),
                user.getEmail(),
                user.getUserType(),
                newAccessToken
        ));
    }
}

