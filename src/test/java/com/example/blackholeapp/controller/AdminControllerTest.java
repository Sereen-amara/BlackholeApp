// package declaration for the controller class
package com.example.blackholeapp.controller;

// importing necessary dependencies
import com.example.blackholeapp.model.User; // user model for managing user data
import com.example.blackholeapp.service.UserService; // service layer for user operations
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays; // utility for creating lists
import java.util.Collections; // utility for empty collections

// importing static methods for mocking and asserting requests
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// annotation to define the test class as a spring boot test
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    // auto-wired mockmvc to perform http requests
    @Autowired
    private MockMvc mockMvc;

    // mockbean for simulating user service behavior
    @MockBean
    private UserService userService;

    // reset mock objects before each test
    @BeforeEach
    void setUp() {
        Mockito.reset(userService);
    }

    // utility method to log test descriptions
    private void logTest(String description) {
        System.out.println("\n==========================");
        System.out.println(description);
        System.out.println("==========================");
    }

    // test case to verify the assign roles page is displayed for admin users
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // mock user with admin role
    void showAssignRolesPage() throws Exception {
        logTest("Testing: showAssignRolesPage - Ensures the assign roles page is displayed for admins.");

        // mock the response for fetching all users
        when(userService.getAllUsers()).thenReturn(
                Arrays.asList(
                        new User(1L, "user1", "user1@example.com", "password"), // mock user 1
                        new User(2L, "user2", "user2@example.com", "password")  // mock user 2
                )
        );

        // perform get request and assert the response
        mockMvc.perform(get("/admin/assign-roles")) // perform get request
                .andExpect(status().isOk()) // expect http 200 status
                .andExpect(view().name("assign-roles")) // expect assign-roles view
                .andExpect(model().attributeExists("users")); // expect users attribute in model

        System.out.println("SUCCESS: Assign Roles page displayed with user list.");
    }
}

/*
references:
1. official spring boot documentation: https://spring.io/guides
2. spring mvc testing guide: https://spring.io/guides/gs/testing-web
3. junit and mockito integration: https://junit.org/junit5/docs/current/user-guide
4. mockmvc testing in spring: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
*/
