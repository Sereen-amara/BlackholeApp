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
