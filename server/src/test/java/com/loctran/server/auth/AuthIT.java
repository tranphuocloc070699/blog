package com.loctran.server.auth;

import com.loctran.server.config.JwtService;
import com.loctran.server.user.dao.UserRepository;
import com.loctran.server.user.dto.UserSignUpDto;
import com.loctran.server.user.model.User;
import com.loctran.server.user.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthIT {
  
  private static final String USER_PATH = "/api/v1/auth/";
  private static final String COOKIE_NAME = "refreshToken";
  @Autowired
  WebTestClient webTestClient;
  @Autowired
  JwtService jwtService;
  @Autowired
  UserRepository userRepository;
  
  
  
  @BeforeEach
  void clearDatabase(@Autowired UserRepository userRepository) {
    userRepository.deleteAll();
    
  }
  
  //=== SignUp With  Invalid Email ===
  @Test
  void cannotSignUpWithEmailInvalid() {
    User requestCustomer = User.builder()
            .name("testCustomer")
            .email("testCustomer")
            .password("password")
            .build();
    
    webTestClient.post()
            .uri(USER_PATH + "signup")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(requestCustomer), User.class)
            .exchange()
            .expectStatus()
            .isBadRequest();
    
  }
  
  //=== SignUp With Null Name ===
  @Test
  void cannotSignUpWithNullName() {
    User requestCustomer = User.builder()
            
            .email("testCustomer@example.com")
            .password("password")
            .build();
    
    webTestClient.post()
            .uri(USER_PATH + "signup")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(requestCustomer), User.class)
            .exchange()
            .expectStatus()
            .isBadRequest();
  }
  
  // === SignUp With Valid Arguments ===
  @Test
  void SignUpWithValidArguments() {
    UserSignUpDto requestCustomer = UserSignUpDto.builder()
            .name("testCustomer")
            .email("testCustomer@example.com")
            .password("password")
            .build();
  
  
  
    EntityExchangeResult<AuthResponse> exchangeResult = webTestClient.post()
            .uri(USER_PATH + "signup")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(requestCustomer), User.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<AuthResponse>() {
            })
            .returnResult();
    //check data not null
    Map<String, Object> user = (Map<String, Object>) exchangeResult.getResponseBody().getData();
    Object userObject = user.get("user");
    assertThat(userObject).isInstanceOf(LinkedHashMap.class);
    
    // check cookie and accessToken is not null and value valid
    MultiValueMap<String, ResponseCookie> cookies = exchangeResult.getResponseCookies();
    ResponseCookie cookie = cookies.getFirst(COOKIE_NAME);
    assertNotNull(cookie);
    String emailFromRefreshToken = jwtService.extractUsername(cookie.getValue());
    String emailFromAccessToken = jwtService.extractUsername(exchangeResult.getResponseBody().getAccessToken());
    assertEquals(requestCustomer.getEmail(), emailFromAccessToken, emailFromRefreshToken);
  }
  
  // === SignIn With Invalid Email (Password same so not necessary) ===
  @Test
  void SignInWithInvalidEmail() {
    User signUpCustomer = User.builder()
            .name("testCustomer")
            .email("testCustomer@example.com")
            .password("password")
            .role(Role.USER)
            .build();
    
    User savedCustomer = userRepository.save(signUpCustomer);
    assertNotNull(savedCustomer);
    
    User invalIdEmailUser = User.builder()
            .name("testCustomer")
            .email("invalid@example.com")
            .password("password")
            .build();
    webTestClient.post()
            .uri(USER_PATH + "login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(invalIdEmailUser), User.class)
            .exchange()
            .expectStatus().isUnauthorized();
  }
  
  
  //=== SignIn With Valid Arguments ===
  @Test
  void SignInWithValidArguments() {
    User requestCustomer = User.builder()
            .name("testCustomer")
            .email("testCustomer@example.com")
            .password("password")
            .build();
    
   
    
    webTestClient.post()
            .uri(USER_PATH + "signup")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(requestCustomer), User.class)
            .exchange()
            .expectStatus().isOk();
  
    EntityExchangeResult<AuthResponse> exchangeResult = webTestClient.post()
            .uri(USER_PATH + "login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(requestCustomer), User.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<AuthResponse>() {
            })
            .returnResult();
         
    //check data not null
    Map<String, Object> user = (Map<String, Object>) exchangeResult.getResponseBody().getData();
    Object userObject = user.get("user");
    assertThat(userObject).isInstanceOf(LinkedHashMap.class);
    
    // check cookie and accessToken is not null and value valid
    MultiValueMap<String, ResponseCookie> cookies = exchangeResult.getResponseCookies();
    ResponseCookie cookie = cookies.getFirst(COOKIE_NAME);
    assertNotNull(cookie);
    String emailFromRefreshToken = jwtService.extractUsername(cookie.getValue());
    String emailFromAccessToken = jwtService.extractUsername(exchangeResult.getResponseBody().getAccessToken());
    assertEquals(requestCustomer.getEmail(), emailFromAccessToken, emailFromRefreshToken);
  }
  
  //=== Authenticate ===
  @Test
  void authenticate() {
    User requestCustomer = User.builder()
            .name("testCustomer")
            .email("testCustomer@example.com")
            .password("password")
            .build();
  
    EntityExchangeResult<AuthResponse> exchangeResult = webTestClient.post()
            .uri(USER_PATH + "signup")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(requestCustomer), User.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<AuthResponse>() {
            })
            .returnResult();
    //check data not null
    Map<String, Object> user = (Map<String, Object>) exchangeResult.getResponseBody().getData();
    Object userObject = user.get("user");
    assertThat(userObject).isInstanceOf(LinkedHashMap.class);
  
    // check cookie and accessToken is not null and value valid
    MultiValueMap<String, ResponseCookie> cookies = exchangeResult.getResponseCookies();
    ResponseCookie cookie = cookies.getFirst(COOKIE_NAME);
    assertNotNull(cookie);
    String emailFromRefreshToken = jwtService.extractUsername(cookie.getValue());
    String emailFromAccessToken = jwtService.extractUsername(exchangeResult.getResponseBody().getAccessToken());
    assertEquals(requestCustomer.getEmail(), emailFromAccessToken, emailFromRefreshToken);
  
    webTestClient.get()
            .uri(USER_PATH + "authenticate")
            .accept(MediaType.APPLICATION_JSON)
            .cookie(cookie.getName(),cookie.getValue())
            .exchange()
            .expectStatus().isOk();
  }
}