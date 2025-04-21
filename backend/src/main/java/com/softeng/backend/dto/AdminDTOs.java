package com.softeng.backend.dto;

import com.softeng.backend.model.User;

public class AdminDTOs {
    public static class AddEmployeeRequest {
        private String personalNo;
        private String name;
        private String surname;
        private String email;
        private String password;
        private User.UserType userType;
        private String deptName;


        public String getPersonalNo() { return personalNo; }
        public void setPersonalNo(String personalNo) { this.personalNo = personalNo; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSurname() { return surname; }
        public void setSurname(String surname) { this.surname = surname; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public User.UserType getUserType() { return userType; }
        public void setUserType(User.UserType userType) { this.userType = userType; }
        public String getDeptName() { return deptName; }
        public void setDeptName(String deptName) { this.deptName = deptName; }
    }

    public enum AddEmployeeResponse {
        EMPLOYEE_ADDED("Employee added successfully"),
        EMPLOYEE_ALREADY_EXISTS("Employee already exists"),
        INVALID_AUTHENTICATION("Invalid authentication");

        private final String message;

        AddEmployeeResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class AddDepartmentRequest {
        private String deptName;

        public String getDeptName() { return deptName; }
        public void setDeptName(String deptName) { this.deptName = deptName; }
    }

    public enum AddDepartmentResponse {
        DEPARTMENT_ADDED("Department added successfully"),
        DEPARTMENT_ALREADY_EXISTS("Department already exists"),
        INVALID_AUTHENTICATION("Invalid authentication");

        private final String message;

        AddDepartmentResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum GetDepartmentResponse {
        INVALID_AUTHENTICATION("Invalid authentication");

        private final String message;

        GetDepartmentResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
