package com.devsuperior.demo.config.customgrant;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

class CustomPasswordAuthenticationConverterTests {
  private final CustomPasswordAuthenticationConverter converter = new CustomPasswordAuthenticationConverter();

  @AfterEach
  void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void ignoresRequestsForAnotherGrantType() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setParameter(OAuth2ParameterNames.GRANT_TYPE, "client_credentials");
    assertNull(converter.convert(request));
  }

  @Test
  void rejectsPasswordGrantWithoutUsername() {
    MockHttpServletRequest request = request("", "secret");
    assertThrows(OAuth2AuthenticationException.class, () -> converter.convert(request));
  }

  @SuppressWarnings("null")
  @Test
  void convertsCredentialsAndScopes() {
    MockHttpServletRequest request = request("user@example.com", "secret");
    request.setParameter(OAuth2ParameterNames.SCOPE, "read write");
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("client", "secret"));
    var result = (CustomPasswordAuthenticationToken) converter.convert(request);
    assertEquals("user@example.com", result.getUsername());
    assertEquals("secret", result.getPassword());
    assertEquals(java.util.Set.of("read", "write"), result.getScopes());
  }

  private MockHttpServletRequest request(String username, String password) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setParameter(OAuth2ParameterNames.GRANT_TYPE, "password");
    if (!username.isEmpty())
      request.setParameter(OAuth2ParameterNames.USERNAME, username);
    request.setParameter(OAuth2ParameterNames.PASSWORD, password);
    return request;
  }
}
