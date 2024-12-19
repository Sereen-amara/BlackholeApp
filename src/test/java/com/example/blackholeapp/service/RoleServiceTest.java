// testing the role service for its functionality

package com.example.blackholeapp.service;

import com.example.blackholeapp.model.Role;
import com.example.blackholeapp.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    // mocking the role repository
    @Mock
    private RoleRepository roleRepository;

    // injecting mocks into role service
    @InjectMocks
    private RoleService roleService;

    // initializing mocks before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // source: https://site.mockito.org/
    }

    // helper method to log test descriptions
    private void logTest(String description) {
        System.out.println("\n==========================");
        System.out.println(description);
        System.out.println("==========================");
    }

    @Test
    void testGetAllRoles() {
        logTest("testing fetching all roles");

        // mock roles to simulate database data
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_REVIEWER");

        // mock repository response
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2)); // source: https://spring.io/

        // calling service method
        List<Role> roles = roleService.getAllRoles();

        // assertions to verify functionality
        assertThat(roles).hasSize(2);
        assertThat(roles.get(0).getName()).isEqualTo("ROLE_ADMIN");
        assertThat(roles.get(1).getName()).isEqualTo("ROLE_REVIEWER");

        System.out.println("success fetched all roles");
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testFindRoleByName() {
        logTest("testing finding role by name");

        // mock role to simulate database data
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_ADMIN");

        // mock repository response
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(role); // source: https://spring.io/

        // calling service method
        Role result = roleService.findRoleByName("ROLE_ADMIN");

        // assertions to verify functionality
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("ROLE_ADMIN");

        System.out.println("success role ROLE_ADMIN found");
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
    }

    @Test
    void testCreateRole_Success() {
        logTest("testing creating new role");

        // creating a new role
        Role role = new Role();
        role.setName("ROLE_NEW");

        // mock repository behavior
        when(roleRepository.findByName("ROLE_NEW")).thenReturn(null);
        when(roleRepository.save(role)).thenReturn(role); // source: https://spring.io/

        // calling service method
        Role createdRole = roleService.createRole(role);

        // assertions to verify functionality
        assertThat(createdRole).isNotNull();
        assertThat(createdRole.getName()).isEqualTo("ROLE_NEW");

        System.out.println("success role ROLE_NEW created");
        verify(roleRepository, times(1)).findByName("ROLE_NEW");
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testCreateRole_AlreadyExists() {
        logTest("testing creating role that already exists");

        // creating an existing role
        Role existingRole = new Role();
        existingRole.setName("ROLE_ADMIN");

        // mock repository response
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(existingRole); // source: https://site.mockito.org/

        // calling service method and checking exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.createRole(existingRole));
        assertThat(exception.getMessage()).isEqualTo("Role already exists: ROLE_ADMIN");

        System.out.println("success exception for existing role ROLE_ADMIN");
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
        verify(roleRepository, never()).save(existingRole);
    }

    @Test
    void testDeleteRole_Success() {
        logTest("testing deleting role");

        // creating a mock role
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_ADMIN");

        // mock repository behavior
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role)); // source: https://spring.io/

        // calling service method
        roleService.deleteRole(1L);

        // verify delete method was called
        System.out.println("success role ROLE_ADMIN deleted");
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    void testDeleteRole_NotFound() {
        logTest("testing deleting role that does not exist");

        // mock repository behavior for no role found
        when(roleRepository.findById(1L)).thenReturn(Optional.empty()); // source: https://site.mockito.org/

        // calling service method and checking exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.deleteRole(1L));
        assertThat(exception.getMessage()).isEqualTo("Role not found with ID: 1");

        System.out.println("success exception for non existent role");
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, never()).delete(any());
    }
}
/*
references:
1. official spring boot documentation: https://spring.io/guides
2. spring mvc testing guide: https://spring.io/guides/gs/testing-web
3. junit and mockito integration: https://junit.org/junit5/docs/current/user-guide
4. mockmvc testing in spring: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
*/
