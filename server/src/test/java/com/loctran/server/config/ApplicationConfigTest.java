package com.loctran.server.config;

import com.loctran.server.user.dao.UserDataAccess;
import com.loctran.server.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ApplicationConfigTest {
  @Mock
  private UserDataAccess userDataAccess;
  
  private ApplicationConfig applicationConfig;
  
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    applicationConfig = new ApplicationConfig(userDataAccess);
  }
  
  @Test
  void testUserDetailsServiceBean() {
 
    when(userDataAccess.findByEmail("test@example.com"))
            .thenReturn(Optional.of(User.builder()
                    .email( "test@example.com")
                    .password( "$2a$10$dk1PkbaDVtvHiN4aaiPvUu2frEVp1DTzBVcvDqoJYK4Z1t7hSwBXG")
                    .build()
            ));
    UserDetailsService userDetailsService = applicationConfig.userDetailsService();
    UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");
    assertNotNull(userDetails);
    assertEquals("test@example.com", userDetails.getUsername());
  }
  
  @Test
  void testAuthenticationProviderBean() {
    UserDetailsService userDetailsServiceMock = mock(UserDetailsService.class);
    PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);

    AuthenticationProvider authenticationProvider = applicationConfig.authenticationProvider();

    assertNotNull(authenticationProvider);

  }
  
  @Test
  void testAuthenticationManagerBean() throws Exception {
    AuthenticationConfiguration authenticationConfigurationMock = mock(AuthenticationConfiguration.class);
    AuthenticationManager authenticationManagerMock = mock(AuthenticationManager.class);

    when(authenticationConfigurationMock.getAuthenticationManager()).thenReturn(authenticationManagerMock);

    AuthenticationManager authenticationManager = applicationConfig.authenticationManager(authenticationConfigurationMock);

    assertNotNull(authenticationManager);
    assertSame(authenticationManagerMock, authenticationManager);
  }
  
  @Test
  void testPasswordEncoderBean() {
    PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();

    assertNotNull(passwordEncoder);
    assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
  }
}
