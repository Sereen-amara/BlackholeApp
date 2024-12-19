package com.example.blackholeapp.service;

import com.example.blackholeapp.model.Role;
import com.example.blackholeapp.model.User;
import com.example.blackholeapp.repository.RoleRepository;
import com.example.blackholeapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * this test class validates the functionality of the UserService
 *  reference on Spring Boot Testin code is from :
 * https://spring.io/guides/gs/testing-web
 */
class UserServiceTest {

    // mocking  repositories and encoder required by UserService
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Initializes mocks before each test. learned from Mockito documentation:
        // https://site.mockito.org/
        MockitoAnnotations.openMocks(this);
    }

    /**
     *  method to organize test outputs
     * inspired by logging practices described at:
     * https://www.w3schools.com/java/java_files_create.asp
     */
    private void logTest(String description) {
        System.out.println("\n==========================");
        System.out.println(description);
        System.out.println("==========================");
    }

    @Test
    void testLoadUserByUsername_Success() {
        logTest("Testing: Successfully loading a user by username.");

        // prepare  mock user object
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");
        Role role = new Role();
        role.setName("ROLE_USER");
        user.setRoles(Collections.singleton(role));

        // Mock repository behavior learnt from
        // https://spring.io/projects/spring-data-jpa
        when(userRepository.findByUsername("john_doe")).thenReturn(user);

        // Act: Load the user by username
        var userDetails = userService.loadUserByUsername("john_doe");

        // validate response
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("john_doe");
        assertThat(userDetails.getAuthorities()).hasSize(1);

        System.out.println("SUCCESS: User 'john_doe' loaded successfully.");
        verify(userRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        logTest("Testing: Handling missing user by username.");

        //  Mock the repository to return null
        when(userRepository.findByUsername("unknown_user")).thenReturn(null);

        // for  UsernameNotFoundException is thrown
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("unknown_user"));
        assertThat(exception.getMessage()).isEqualTo("User not found with username: unknown_user");

        System.out.println("SUCCESS: Exception correctly thrown for missing user.");
        verify(userRepository, times(1)).findByUsername("unknown_user");
    }

    @Test
    void testRegisterUser_Success() {
        logTest("Testing: Successfully registering a new user.");

        // Arrange: Prepare the user and role objects
        User user = new User();
        user.setUsername("jane_doe");
        user.setPassword("plainpassword");

        Role reviewerRole = new Role();
        reviewerRole.setName("ROLE_REVIEWER");

        // Mock behavior for encoding and repository save operations
        when(passwordEncoder.encode("plainpassword")).thenReturn("encodedpassword");
        when(roleRepository.findByName("ROLE_REVIEWER")).thenReturn(reviewerRole);
        when(userRepository.save(user)).thenReturn(user);

        // Register the user
        User registeredUser = userService.registerUser(user);

        //  user is saved with the correct role and encoded password
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getUsername()).isEqualTo("jane_doe");
        assertThat(registeredUser.getRoles()).hasSize(1);

        System.out.println("SUCCESS: User registered with default role.");
        verify(passwordEncoder, times(1)).encode("plainpassword");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_DefaultRoleNotFound() {
        logTest("Testing: Registering a user when the default role is missing.");

        // Mock repository to return null for the default role
        User user = new User();
        user.setUsername("new_user");
        user.setPassword("securepassword");

        when(roleRepository.findByName("ROLE_REVIEWER")).thenReturn(null);

        //  RuntimeException for missing default role
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(user));
        assertThat(exception.getMessage()).isEqualTo("Default role not found! Please ensure the role 'ROLE_REVIEWER' is present in the database.");

        System.out.println("SUCCESS: Correctly handled missing role scenario.");
        verify(roleRepository, times(1)).findByName("ROLE_REVIEWER");
        verify(userRepository, never()).save(user);
    }

    @Test
    void testAssignRoleToUser_UserNotFound() {
        logTest("Testing: Assigning a role to a non-existent user.");

        // Mock repository to return empty optional
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        //  RuntimeException for missing user
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.assignRoleToUser(99L, "ROLE_ADMIN"));
        assertThat(exception.getMessage()).isEqualTo("User not found");

        System.out.println("SUCCESS: Exception handled correctly for missing user.");
        verify(userRepository, times(1)).findById(99L);
        verify(roleRepository, never()).findByName(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetAllUsers() {
        logTest("Testing: Retrieving all users from the database.");

        //  repository to return a list of users
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // get all users
        List<User> users = userService.getAllUsers();

        //  correct number of users is retrieved
        assertThat(users).hasSize(2);

        System.out.println("SUCCESS: Retrieved all users successfully.");
        verify(userRepository, times(1)).findAll();
    }
}
/*
references:
1. official spring boot documentation: https://spring.io/guides
2. spring data jpa guide: https://spring.io/guides/gs/accessing-data-jpa
3. junit and mockito integration: https://junit.org/junit5/docs/current/user-guide
4. mockito usage examples: https://site.mockito.org
*/