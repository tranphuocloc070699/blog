package com.loctran.server.user.service;

import com.loctran.server.config.JwtService;
import com.loctran.server.exception.custom.NotFoundException;
import com.loctran.server.exception.custom.UnAuthenticateException;
import com.loctran.server.user.dao.UserDataAccess;
import com.loctran.server.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;
    private final UserDataAccess userDataAccess;


    public User getOptionalUserByToken(){
        String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader==null || !authHeader.startsWith("Bearer ")) return null;
        String accessToken = authHeader.substring(7);
        String email = jwtService.extractUsername(accessToken);
        if(email==null || !email.contains("@")) throw new UnAuthenticateException("email invalid");
        Optional<User> user = userDataAccess.findByEmail(email);
        if(user.isEmpty()) throw new NotFoundException("user not found with email ["+ email +"]", httpServletRequest.getServletPath());
        if(!jwtService.isTokenValid(accessToken,user.get())) return null;

        return user.get();
    }

}
