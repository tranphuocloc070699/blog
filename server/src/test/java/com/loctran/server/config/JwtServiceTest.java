package com.loctran.server.config;

import com.loctran.server.exception.custom.ForbiddenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class JwtServiceTest {
  @Mock
  private UserDetails userDetails;
  
  private JwtService jwtService;
  
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    
    String secretKey = "PJC7HnliwcxXw4FM8Ep3sX9NIL3R5CZnDvp8IyyCSlg=";
    long jwtExpiration = 3600;
    long refreshExpiration = 86400;
    jwtService = new JwtService(secretKey, jwtExpiration, refreshExpiration);
  }
  
  @Test
  void testExtractUsername() {
    String token = generateTokenWithSubject("testUser");
    String username = jwtService.extractUsername(token);
    assertEquals("testUser", username);
  }
  
  @Test
  void testExtractClaim() {
    String token = generateTokenWithSubject("testUser");
    String subject = jwtService.extractClaim(token, Claims::getSubject);
    assertEquals("testUser", subject);
  }
  
  @Test
  void testGenerateToken() {
    when(userDetails.getUsername()).thenReturn("testUser");
    String token = jwtService.generateToken(userDetails);
    assertNotNull(token);
  }
  
  @Test
  void testGenerateTokenWithExtractClaims() {
    when(userDetails.getUsername()).thenReturn("testUser");
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("key1", "value1");
    String token = jwtService.generateToken(extraClaims, userDetails);
    assertNotNull(token);
  }
  
  @Test
  void testGenerateRefreshToken() {
    when(userDetails.getUsername()).thenReturn("testUser");
    String refreshToken = jwtService.generateRefreshToken(userDetails);
    assertNotNull(refreshToken);
  }
  
  @Test
  void testIsTokenValidWithValidToken() {
    String token = generateTokenWithSubject("testUser");
    when(userDetails.getUsername()).thenReturn("testUser");
  
    boolean isValid = jwtService.isTokenValid(token, userDetails);
    assertTrue(isValid);
  }
  
  @Test
  void testIsTokenValidWithInvalidToken() {
    String token = generateTokenWithSubject("testUser");
    when(userDetails.getUsername()).thenReturn("otherUser");
    boolean isValid = jwtService.isTokenValid(token, userDetails);
    assertFalse(isValid);
  }
  
  @Test
  void testIsTokenValidWithExpiredToken() {
    String token = generateExpiredTokenWithSubject("testUser");
 
    when(userDetails.getUsername()).thenReturn("testUser");
    assertThrows(ExpiredJwtException.class,() ->{
      jwtService.isTokenValid(token, userDetails);
    });
  }
  
  // Helper method to generate a JWT token with a given subject and current time
  private String generateTokenWithSubject(String subject) {
    return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 3600) )
            .signWith(getSignInKey())
            .compact();
  }
  
  // Helper method to generate an expired JWT token with a given subject and current time
  private String generateExpiredTokenWithSubject(String subject) {
    long expiration = -3600; // negative expiration to represent an expired token
    return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
            .signWith(getSignInKey())
            .compact();
  }
  
  // Helper method to access the private getSignInKey() method using ReflectionTestUtils
  private Key getSignInKey() {
    return (Key) ReflectionTestUtils.invokeMethod(jwtService, "getSignInKey");
  }

}