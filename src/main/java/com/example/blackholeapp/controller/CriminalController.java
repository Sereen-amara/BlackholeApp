package com.example.blackholeapp.controller;

import com.example.blackholeapp.model.Criminal;
import com.example.blackholeapp.service.CriminalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/criminals") // Base mapping for criminal-related endpoints
public class CriminalController {

    private static final Logger logger = LoggerFactory.getLogger(CriminalController.class);

    @Autowired
    private CriminalService criminalService;

    // Display the dashboard without any criminals initially
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized access attempt to the dashboard.");
            return "redirect:/users/login"; // Redirect to login if not authenticated
        }

        logger.info("Displaying dashboard to authenticated user: {}", authentication.getName());
        model.addAttribute("criminals", null);
        return "dashboard"; // Render the dashboard.html template
    }

    // Search for criminals
    @GetMapping("/search")
    public String searchCriminals(@RequestParam(value = "query", required = false) String query,
                                  Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized search attempt.");
            return "redirect:/users/login";
        }

        if (query == null || query.trim().isEmpty()) {
            logger.info("Empty search query submitted.");
            model.addAttribute("error", "Please enter a valid search query.");
            model.addAttribute("criminals", null);
            return "dashboard";
        }

        logger.info("Searching for criminals using query: {}", query);
        List<Criminal> criminals = criminalService.searchCriminals(query);

        if (criminals.isEmpty()) {
            logger.info("No results found for query: {}", query);
            model.addAttribute("error", "No results found for: " + query);
        } else {
            logger.info("Search returned {} result(s).", criminals.size());
        }

        model.addAttribute("criminals", criminals);
        return "dashboard";
    }

    // Display Add Criminal Form (GET request)
    @GetMapping("/add")
    public String showAddCriminalForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized access attempt to add-criminal form.");
            return "redirect:/users/login";
        }

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            logger.info("Admin user accessed add-criminal form.");
            model.addAttribute("criminal", new Criminal());
            return "add-criminal";
        } else {
            logger.warn("Non-admin user attempted to access add-criminal form.");
            return "redirect:/criminals/dashboard";
        }
    }

    // Add a new criminal (POST request)
    @PostMapping("/add")
    public String addCriminal(@ModelAttribute Criminal criminal, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized attempt to add a criminal.");
            return "redirect:/users/login";
        }

        logger.info("Admin user {} is adding a new criminal.", authentication.getName());
        criminalService.addCriminal(criminal);
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
