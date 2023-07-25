package com.loctran.server.auth;

import com.loctran.server.auth.dto.AuthLogInDto;
import com.loctran.server.auth.dto.AuthSignUpDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")

public class AuthController {
  private final AuthService authService;
  @PostMapping(path = "signup")
  ResponseEntity<AuthResponse> signup(@Valid @RequestBody AuthSignUpDto requestUser,
                                      HttpServletResponse response){
    return this.authService.signup(requestUser,response);
  }
  
  @PostMapping(path= "login")
  ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLogInDto requestUser,
                                     HttpServletResponse response){
    return this.authService.login(requestUser,response);
  }
  @GetMapping(path= "authenticate")
  ResponseEntity<AuthResponse> authenticate(HttpServletRequest request,
                                            HttpServletResponse response){
    return this.authService.authenticate(request,response);
  }
}
