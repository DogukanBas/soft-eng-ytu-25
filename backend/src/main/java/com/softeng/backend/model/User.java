package com.softeng.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

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
        @JsonProperty("team-member")
        team_member("team-member"),
        @JsonProperty("employee")
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
}