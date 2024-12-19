// package declaration for the service test class
package com.example.blackholeapp.service;

// importing necessary dependencies
import com.example.blackholeapp.model.Criminal; // criminal model for testing
import com.example.blackholeapp.repository.CriminalRepository; // repository layer for criminal data
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays; // utility for creating lists
import java.util.List; // list interface for result handling

// importing static methods for assertions and mocking
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CriminalServiceTest {

    // mock for simulating criminal repository behavior
    @Mock
    private CriminalRepository criminalRepository;

    // inject mocks into the criminal service being tested
    @InjectMocks
    private CriminalService criminalService;

    // initialize mocks before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // utility method to log test descriptions
    private void logTest(String description) {
        System.out.println("\n==========================");
        System.out.println(description);
        System.out.println("==========================");
    }

    // test case to verify adding a new criminal
    @Test
    void testAddCriminal() {
        logTest("Test: Adding a new Criminal - Ensures the addCriminal method works correctly.");

        // arrange
        Criminal criminal = new Criminal();
        criminal.setId(1L);
        criminal.setName("John Doe");
        criminal.setDescription("A known offender");

        when(criminalRepository.save(criminal)).thenReturn(criminal);

        // act
        Criminal savedCriminal = criminalService.addCriminal(criminal);

        // assert
        assertNotNull(savedCriminal, "The saved criminal should not be null.");
        assertEquals("John Doe", savedCriminal.getName(), "The criminal's name should match.");
        verify(criminalRepository, times(1)).save(criminal);

        System.out.println("SUCCESS: Criminal added successfully.");
    }

    // test case to verify fetching all criminals
    @Test
    void testGetAllCriminals() {
        logTest("Test: Fetching all Criminals - Ensures the getAllCriminals method retrieves all records.");

        // arrange
        Criminal criminal1 = new Criminal();
        criminal1.setId(1L);
        criminal1.setName("John Doe");

        Criminal criminal2 = new Criminal();
        criminal2.setId(2L);
        criminal2.setName("Jane Doe");

        List<Criminal> criminals = Arrays.asList(criminal1, criminal2);

        when(criminalRepository.findAll()).thenReturn(criminals);

        // act
        List<Criminal> result = criminalService.getAllCriminals();

        // assert
        assertNotNull(result, "The result should not be null.");
        assertEquals(2, result.size(), "The size of the list should be 2.");
        verify(criminalRepository, times(1)).findAll();

        System.out.println("SUCCESS: All criminals fetched successfully. Total criminals: " + result.size());
    }

    // test case to verify searching criminals
    @Test
    void testSearchCriminals() {
        logTest("Test: Searching for Criminals - Ensures the searchCriminals method filters records correctly.");

        // arrange
        String query = "John";

        Criminal criminal = new Criminal();
        criminal.setId(1L);
        criminal.setName("John Doe");

        List<Criminal> criminals = Arrays.asList(criminal);

        when(criminalRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)).thenReturn(criminals);

        // act
        List<Criminal> result = criminalService.searchCriminals(query);

        // assert
        assertNotNull(result, "The result should not be null.");
        assertEquals(1, result.size(), "The size of the list should be 1.");
        assertEquals("John Doe", result.get(0).getName(), "The name of the criminal should match.");
        verify(criminalRepository, times(1)).findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);

        System.out.println("SUCCESS: Criminals searched and found successfully. Total results: " + result.size());
    }
}

/*
references:
1. official spring boot documentation: https://spring.io/guides
2. spring data jpa guide: https://spring.io/guides/gs/accessing-data-jpa
3. junit and mockito integration: https://junit.org/junit5/docs/current/user-guide
4. mockito usage examples: https://site.mockito.org
*/
