package com.example.blackholeapp.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(nullable = false, unique = true) // Enforce unique usernames
    private String username;

    @Column(nullable = false) // Non-nullable email
    private String email;

    @Column(nullable = false) // Non-nullable password
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
// spring boot official documentation
// https://spring.io/projects/spring-boot
// provides comprehensive information on setting up and configuring spring boot applications

// baeldung spring boot tutorials
// https://www.baeldung.com/spring-boot
// step-by-step guides for project setup, dependency management, and application configuration

// spring data jpa reference documentation
// https://spring.io/projects/spring-data-jpa
// detailed instructions for repository creation, query methods, and database integrations

// baeldung spring data jpa
// https://www.baeldung.com/spring-data-jpa-query
// guides on how to use spring data jpa for custom queries and advanced features

// spring security official documentation
// https://spring.io/projects/spring-security
// explains authentication, authorization, and securing applications in detail

// baeldung spring security tutorials
// https://www.baeldung.com/spring-security
// provides practical examples for role-based access control and securing endpoints

// thymeleaf official documentation
// https://www.thymeleaf.org/documentation.html
// explains how to use thymeleaf for dynamic web page creation in spring boot applications

// baeldung thymeleaf integration
// https://www.baeldung.com/thymeleaf-in-spring-mvc
// guides for using thymeleaf with spring boot and creating web pages with dynamic data

// baeldung spring security roles and privileges
// https://www.baeldung.com/spring-security-roles-and-privileges
// explains role-based access control, assigning roles, and managing user permissions

// spring boot testing documentation
// https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing
// detailed examples for writing and running tests in spring boot applications
