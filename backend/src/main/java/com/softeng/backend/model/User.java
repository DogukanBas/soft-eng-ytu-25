package com.softeng.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "personalNo", unique = true, nullable = false)
    private String personalNo;

    @Column(nullable = false)
    private String passwordHash;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    public enum UserType {
        TEAM_MEMBER("team-member"),
        MANAGER("manager"),
        ACCOUNTANT("accountant"),
        ADMIN("admin");

        private final String value;

        UserType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}