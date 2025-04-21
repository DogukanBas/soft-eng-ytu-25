package com.softeng.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
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
        @JsonProperty("team_member")
        team_member("team_member"),
        @JsonProperty("manager")
        manager("manager"),
        @JsonProperty("accountant")
        accountant("accountant"),
        @JsonProperty("admin")
        admin("admin");

        private final String value;

        UserType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public User() {
    }

    public User(String personalNo, String email, UserType userType) {
        this.personalNo = personalNo;
        this.email = email;
        this.userType = userType;
    }
}