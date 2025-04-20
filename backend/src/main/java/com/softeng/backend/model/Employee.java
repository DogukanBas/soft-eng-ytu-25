package com.softeng.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employee")
@Data
public class Employee {
    @Id
    @Column(name = "personalNo")
    private String personalNo;

    @OneToOne
    @MapsId
    @JoinColumn(name = "personalNo")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @ManyToOne
    @JoinColumn(name = "deptId", nullable = false)
    private Department department;

    public Employee() {
    }

    public Employee(User user, String name, String surname, Department department) {
        this.user = user;
        this.name = name;
        this.surname = surname;
        this.department = department;
    }
}