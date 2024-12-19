// package declaration for the controller class
package com.example.blackholeapp.controller;

// importing necessary dependencies
import com.example.blackholeapp.model.Criminal; // criminal model for managing criminal data
import com.example.blackholeapp.service.CriminalService; // service layer for criminal operations
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate; // utility for date handling
import java.util.Arrays; // utility for creating lists

// importing static methods for mocking and asserting requests
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// annotation to define the test class as a spring boot test
@SpringBootTest
@AutoConfigureMockMvc
class CriminalControllerTest {

    // auto-wired mockmvc to perform http requests
    @Autowired
    private MockMvc mockMvc;

    // mockbean for simulating criminal service behavior
    @MockBean
    private CriminalService criminalService;

    // reset mock objects before each test
    @BeforeEach
    void setUp() {
        Mockito.reset(criminalService);
    }

    // utility method to log test descriptions
    private void logTest(String description) {
        System.out.println("\n==========================");
        System.out.println(description);
        System.out.println("==========================");
    }

    // test case to verify the dashboard is displayed without a criminal list initially
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // mock user with admin role
    void showDashboard() throws Exception {
        logTest("Testing: showDashboard - Ensures dashboard is displayed without criminal list initially.");

        mockMvc.perform(get("/criminals/dashboard")) // perform get request
                .andExpect(status().isOk()) // expect http 200 status
                .andExpect(view().name("dashboard")) // expect dashboard view
                .andExpect(model().attributeDoesNotExist("criminals")); // ensure criminals attribute does not exist

        System.out.println("SUCCESS: Dashboard displayed correctly without any criminal list.");
    }

    // test case to verify the search functionality for criminals
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // mock user with admin role
    void searchCriminals() throws Exception {
        logTest("Testing: searchCriminals - Ensures search functionality returns a list of criminals.");

        // mock the response for searching criminals
        when(criminalService.searchCriminals("John")).thenReturn(
                Arrays.asList(
                        new Criminal(1L, "John Doe", 30, LocalDate.of(1993, 1, 1), "Description", "None") // mock criminal
                )
        );

        mockMvc.perform(get("/criminals/search").param("query", "John")) // perform get request with query parameter
                .andExpect(status().isOk()) // expect http 200 status
                .andExpect(view().name("dashboard")) // expect dashboard view
                .andExpect(model().attributeExists("criminals")); // ensure criminals attribute exists

        System.out.println("SUCCESS: Criminals were successfully searched and displayed.");
    }

    // test case to verify the add criminal form is displayed
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // mock user with admin role
    void showAddCriminalForm() throws Exception {
        logTest("Testing: showAddCriminalForm - Ensures the add criminal form is displayed.");

        mockMvc.perform(get("/criminals/add")) // perform get request
                .andExpect(status().isOk()) // expect http 200 status
                .andExpect(view().name("add-criminal")) // expect add-criminal view
                .andExpect(model().attributeExists("criminal")); // ensure criminal attribute exists in model

        System.out.println("SUCCESS: Add Criminal form displayed successfully.");
    }

    // test case to verify adding a new criminal
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // mock user with admin role
    void addCriminal() throws Exception {
        logTest("Testing: addCriminal - Ensures a new criminal is added successfully.");

        mockMvc.perform(post("/criminals/add") // perform post request
                        .param("name", "John Doe") // set request parameters
                        .param("age", "30")
                        .param("dob", "1993-01-01")
                        .param("description", "A description")
                        .param("connectedTo", "None"))
                .andExpect(status().is3xxRedirection()) // expect redirection status
                .andExpect(redirectedUrl("/criminals/dashboard")); // expect redirection to dashboard

        System.out.println("SUCCESS: New criminal added successfully and redirected to dashboard.");
    }
}

/*
references:
1. official spring boot documentation: https://spring.io/guides
2. spring mvc testing guide: https://spring.io/guides/gs/testing-web
3. junit and mockito integration: https://junit.org/junit5/docs/current/user-guide
4. mockmvc testing in spring: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
*/
