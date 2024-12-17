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
    public String searchCriminals(@RequestParam(value = "query", required = false) String query,
                                  Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login"; // Redirect to login if not authenticated
        }

        if (query == null || query.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a search query.");
            model.addAttribute("criminals", null);
            return "dashboard"; // Reload dashboard with an error message
        }

        // Perform search and add results to the model
        List<Criminal> criminals = criminalService.searchCriminals(query);
        if (criminals.isEmpty()) {
            model.addAttribute("error", "No results found for: " + query);
        }
        model.addAttribute("criminals", criminals);
        return "dashboard"; // Render the dashboard.html with search results
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
