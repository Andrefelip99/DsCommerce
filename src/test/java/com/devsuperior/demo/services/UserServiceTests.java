package com.devsuperior.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import com.devsuperior.demo.entities.Role;
import com.devsuperior.demo.projections.UserDetailsProjection;
import com.devsuperior.demo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {
  @Mock
  UserRepository repository;
  @InjectMocks
  UserService service;

  @AfterEach
  void clearContext() {
    org.springframework.security.core.context.SecurityContextHolder.clearContext();
  }

  @Test
  void loadUserMapsUsernamePasswordAndDistinctRoles() {
    UserDetailsProjection client = projection("person@example.com", "hash", 1L, "ROLE_CLIENT");
    UserDetailsProjection admin = projection("person@example.com", "hash", 2L, "ROLE_ADMIN");
    when(repository.searchUserAndRolesByEmail("person@example.com")).thenReturn(List.of(client, admin));
    var user = service.loadUserByUsername("person@example.com");
    assertEquals("person@example.com", user.getUsername());
    assertEquals("hash", user.getPassword());
    assertEquals(2, user.getAuthorities().size());
  }

  @Test
  void loadUserWithNoRowsThrowsUsernameNotFound() {
    when(repository.searchUserAndRolesByEmail("none@example.com")).thenReturn(List.of());
    assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("none@example.com"));
  }

  @Test
  void getMeReturnsAuthenticatedUserDto() {
    var jwt = org.springframework.security.oauth2.jwt.Jwt.withTokenValue("token").header("alg", "none")
        .claim("username", "person@example.com").build();
    var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(jwt,
        "token");
    org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
    var user = new com.devsuperior.demo.entities.User();
    user.setId(5L);
    user.setName("Person");
    user.setEmail("person@example.com");
    user.setRoles(new java.util.HashSet<>(List.of(new Role(1L, "ROLE_CLIENT"))));
    when(repository.findByEmail("person@example.com")).thenReturn(java.util.Optional.of(user));
    var dto = service.getMe();
    assertEquals(5L, dto.getId());
    assertEquals("Person", dto.getName());
    assertEquals(List.of("ROLE_CLIENT"), dto.getRoles());
  }

  @Test
  void getMeWhenJwtClaimDoesNotMatchAUserThrowsUsernameNotFound() {
    var jwt = org.springframework.security.oauth2.jwt.Jwt.withTokenValue("token").header("alg", "none")
        .claim("username", "none@example.com").build();
    org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(
        new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(jwt, "token"));
    when(repository.findByEmail("none@example.com")).thenReturn(java.util.Optional.empty());
    assertThrows(UsernameNotFoundException.class, () -> service.getMe());
  }

  private UserDetailsProjection projection(String username, String password, Long roleId, String authority) {
    return new UserDetailsProjection() {
      public String getUsername() {
        return username;
      }

      public String getPassword() {
        return password;
      }

      public Long getRoleId() {
        return roleId;
      }

      public String getAuthority() {
        return authority;
      }
    };
  }
}
