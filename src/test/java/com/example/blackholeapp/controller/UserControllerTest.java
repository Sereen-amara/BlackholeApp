// package declaration for the controller class
package com.example.blackholeapp.controller;

// importing necessary dependencies
import com.example.blackholeapp.model.User; // user model used for testing
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

// importing static methods for request and response assertions
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// annotation to define the test class as a spring boot test
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

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

    // test case to verify the registration form is displayed
    @Test
    void showRegisterForm() throws Exception {
        logTest("Testing: showRegisterForm - Ensures the registration form is displayed.");

        mockMvc.perform(get("/users/register")) // perform get request
                .andExpect(status().isOk()) // expect http 200 status
                .andExpect(view().name("register")) // expect register view
                .andExpect(model().attributeExists("user")); // expect user attribute in model

        System.out.println("SUCCESS: Registration form displayed successfully.");
    }

    // test case to verify user registration functionality
    @Test
    void registerUser() throws Exception {
        logTest("Testing: registerUser - Ensures user registration is processed.");

        mockMvc.perform(post("/users/register") // perform post request
                        .param("username", "testUser") // set request parameters
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk()) // expect http 200 status
                .andExpect(view().name("login")) // expect login view
                .andExpect(model().attributeExists("message")); // expect message attribute

        verify(userService).registerUser(any(User.class)); // verify service method call
        System.out.println("SUCCESS: User registration processed successfully.");
    }

    // test case to verify login form is displayed
    @Test
    void showLoginForm() throws Exception {
        logTest("Testing: showLoginForm - Ensures the login form is displayed.");

        mockMvc.perform(get("/users/login")) // perform get request
                .andExpect(status().isOk()) // expect http 200 status
                .andExpect(view().name("login")); // expect login view

        System.out.println("SUCCESS: Login form displayed successfully.");
    }

    // test case to verify dashboard is displayed for authorized users
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // mock user with admin role
    void showDashboard() throws Exception {
        logTest("Testing: showDashboard - Ensures the dashboard is displayed with username.");

        mockMvc.perform(get("/users/dashboard")) // perform get request
                .andExpect(status().isOk()) // expect http 200 status
                .andExpect(view().name("dashboard")) // expect dashboard view
                .andExpect(model().attributeExists("username")); // expect username attribute

        System.out.println("SUCCESS: Dashboard displayed successfully with username.");
    }
}

/*
references:
1. official spring boot documentation: https://spring.io/guides
2. spring mvc testing guide: https://spring.io/guides/gs/testing-web
3. junit and mockito integration: https://junit.org/junit5/docs/current/user-guide
4. mockmvc testing in spring: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
*/
