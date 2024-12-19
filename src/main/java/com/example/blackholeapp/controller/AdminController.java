package com.example.blackholeapp.controller;

import com.example.blackholeapp.model.User;
import com.example.blackholeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // Display the list of users for role assignment
    @GetMapping("/assign-roles")
    public String showAssignRolesPage(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || !authentication.getAuthorities().toString().contains("ROLE_ADMIN")) {
            return "redirect:/users/login";
        }

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "assign-roles";
    }

    // Assign a role to a user
    @PostMapping("/assign-role")
    public String assignRole(@RequestParam Long userId, @RequestParam String roleName, Model model) {
        try {
            userService.assignRoleToUser(userId, roleName);
            model.addAttribute("message", "Role assigned successfully");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/assign-roles";
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
