package com.loctran.server.filter;

import com.loctran.server.config.JwtService;
import com.loctran.server.exception.custom.ForbiddenException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
private final JwtService jwtService;
private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException, JwtException {
          try{
              final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
              final String jwt;
              final String userEmail;
              if(authHeader==null || !authHeader.startsWith("Bearer ")){
                  filterChain.doFilter(request,response);
                  return;
              }
              jwt = authHeader.substring(7);
              userEmail = jwtService.extractUsername(jwt);
              if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                  UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                  if(jwtService.isTokenValid(jwt,userDetails)){
                      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                              userDetails,
                              null,
                              userDetails.getAuthorities()
                      );
                      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                      SecurityContextHolder.getContext().setAuthentication(authToken);
                  }
              }
          }catch (RuntimeException exception){
              throw new ForbiddenException(exception.getMessage());
          }
           filterChain.doFilter(request,response);
       
    }
}
