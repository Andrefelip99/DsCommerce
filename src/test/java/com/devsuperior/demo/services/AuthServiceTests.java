package com.devsuperior.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devsuperior.demo.entities.Role;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.services.exeptions.ForbiddenException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {
    @Mock
    UserService userService;
    @InjectMocks
    AuthService service;

    @Test
    void allowsOwnResource() {
        User u = user(7L, "ROLE_CLIENT");
        when(userService.authenticated()).thenReturn(u);
        assertDoesNotThrow(() -> service.validateSelfOrAdmin(7L));
    }

    @Test
    void allowsAdminToAccessAnotherResource() {
        User u = user(7L, "ROLE_ADMIN");
        when(userService.authenticated()).thenReturn(u);
        assertDoesNotThrow(() -> service.validateSelfOrAdmin(99L));
    }

    @Test
    void deniesClientAccessToAnotherResource() {
        User u = user(7L, "ROLE_CLIENT");
        when(userService.authenticated()).thenReturn(u);
        assertThrows(ForbiddenException.class, () -> service.validateSelfOrAdmin(99L));
    }

    private User user(Long id, String role) {
        User u = new User();
        u.setId(id);
        u.addRole(new Role(1L, role));
        return u;
    }
}
