package com.softeng.backend.dto;

import com.softeng.backend.model.User;

public class AuthDTOs {
    public static class RegistrationRequest {
        private String personalNo;
        private String email;
        private String password;
        private User.UserType userType;

        public String getPersonalNo() { return personalNo; }
        public void setPersonalNo(String personalNo) { this.personalNo = personalNo; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public User.UserType getUserType() { return userType; }
        public void setUserType(User.UserType userType) { this.userType = userType; }
    }

    public static class LoginRequest {
        private String personalNoOrEmail;
        private String password;

        public String getPersonalNoOrEmail() { return personalNoOrEmail; }
        public void setPersonalNoOrEmail(String personalNoOrEmail) { this.personalNoOrEmail = personalNoOrEmail; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
        private String personalNo;
        private String email;
        private User.UserType userType;
        private String accessToken;

        public LoginResponse(String personalNo, String email,
                             User.UserType userType, String accessToken) {
            this.personalNo = personalNo;
            this.email = email;
            this.userType = userType;
            this.accessToken = accessToken;
        }

        public String getPersonalNo() { return personalNo; }
        public String getEmail() { return email; }
        public User.UserType getUserType() { return userType; }

        public String getAccessToken() { return accessToken; }
    }
}
