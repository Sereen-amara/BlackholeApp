package com.example.blackholeapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Criminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String connectedTo;

    // No-arg constructor (required for JPA/Hibernate)
    public Criminal() {}

    // All-args constructor for convenience
    public Criminal(Long id, String name, int age, LocalDate dob, String description, String connectedTo) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.dob = dob;
        this.description = description;
        this.connectedTo = connectedTo;
    }

    public Criminal(long id, String johnDoe, int age, String date, String aCriminal, String none) {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConnectedTo() {
        return connectedTo;
    }

    public void setConnectedTo(String connectedTo) {
        this.connectedTo = connectedTo;
    }
}