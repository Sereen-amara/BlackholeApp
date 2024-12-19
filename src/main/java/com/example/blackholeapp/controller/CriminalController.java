package com.example.blackholeapp.controller;

import com.example.blackholeapp.model.Criminal;
import com.example.blackholeapp.service.CriminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/criminals")
public class CriminalController {

    @Autowired
    private CriminalService criminalService;

    // Display the dashboard without any criminals initially
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login"; // Redirect to login if not authenticated
        }

        // Initially, no criminals should be loaded
        model.addAttribute("criminals", null);
        return "dashboard"; // Render the dashboard.html template
    }


    // Search for criminals
    @GetMapping("/search")
    public String searchCriminals(@RequestParam(value = "query", required = false) String query, Model model) {
        if (query != null) {
            model.addAttribute("error", query); // Reflected XSS vulnerability
        }
        return "dashboard";
    }

    // Display Add Criminal Form (GET request)
    @GetMapping("/add")
    public String showAddCriminalForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login"; // Redirect to login if not authenticated
        }

        // Check if user has the "ROLE_ADMIN" authority
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("criminal", new Criminal()); // Initialize a new Criminal object for the form
            return "add-criminal"; // Render add-criminal.html template
        } else {
            return "redirect:/criminals/dashboard"; // Redirect if user is not admin
        }
    }

    // Add a new criminal (POST request)
    @PostMapping("/add")
    public String addCriminal(@ModelAttribute Criminal criminal, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login"; // Redirect to login if not authenticated
        }

        // Add the criminal to the database
        criminalService.addCriminal(criminal);

        // Redirect to the dashboard
        return "redirect:/criminals/dashboard";


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
