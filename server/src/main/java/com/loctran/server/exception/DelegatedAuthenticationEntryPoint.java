package com.loctran.server.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loctran.server.exception.custom.ForbiddenException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component("delegatedAuthenticationEntryPoint")
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {
  
  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver resolver;
  
  @Override
  public void commence(HttpServletRequest request,
                       HttpServletResponse response,
                       AuthenticationException authException)
          throws IOException, ServletException {
    if(authException != null){
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      String errorMessage = authException.getLocalizedMessage();
      ApiError apiError = ApiError.builder()
              .timestamp(new Date())
              .status(HttpStatus.FORBIDDEN)
              .errors(List.of(errorMessage))
              .message("Authenticate error")
              .path(request.getServletPath())
              .build();
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonErrorResponse = objectMapper.writeValueAsString(apiError);
      response.getWriter().write(jsonErrorResponse);
    }else{
      resolver.resolveException(request, response, null, authException);
    }
    
    
  }
}