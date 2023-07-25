package com.loctran.server.user.controller;

import com.loctran.server.auth.AuthService;
import com.loctran.server.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user/")
public class UserController {

    private final UserService userService;
    private final AuthService authenticationService;
    
    @GetMapping(path="hello")
    public String hello() {
        return "GET:: Hello User";
    }
    
   
    
}
