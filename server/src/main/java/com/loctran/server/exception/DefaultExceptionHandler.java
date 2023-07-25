package com.loctran.server.exception;


import com.loctran.server.exception.custom.ConflictException;
import com.loctran.server.exception.custom.ForbiddenException;
import com.loctran.server.exception.custom.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {
  
  /* === InvalidRequestException === */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatusCode status,
                                                                WebRequest request) {
    List<String> errors = new ArrayList<String>();
    //path base: uri=...
    String path = request.getDescription(false);
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
  
    ApiError apiError = ApiError.builder()
            .timestamp(new Date())
            .status(HttpStatus.BAD_REQUEST)
            .errors(errors)
            .message("Validate fail")
            .path(path.substring(4))
            .build();
  
    return new ResponseEntity<>(apiError,headers,status);
    
  }
  
  /* === NotFoundException === */
  @ExceptionHandler({ NotFoundException.class })
  @ResponseBody
  public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex) {
    ApiError apiError = ApiError.builder()
            .timestamp(new Date())
            .status(HttpStatus.NOT_FOUND)
            .errors(List.of(ex.getLocalizedMessage()))
            .message(ex.getLocalizedMessage())
            .path(ex.getPath())
            .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
  }
  
  /* === ConflictException === */
  @ExceptionHandler({ ConflictException.class })
  @ResponseBody
  public ResponseEntity<ApiError> handleConflictException(ConflictException ex) {
    ApiError apiError = ApiError.builder()
            .timestamp(new Date())
            .status(HttpStatus.CONFLICT)
            .errors(List.of(ex.getLocalizedMessage()))
            .message(ex.getLocalizedMessage())
            .path(ex.getPath())
            .build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
  }
  
  /* === AuthenticationException === */
  @ExceptionHandler({ AuthenticationException.class })
  @ResponseBody
  public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
    System.out.println("AuthenticationException");
    ApiError apiError = ApiError.builder()
            .timestamp(new Date())
            .status(HttpStatus.UNAUTHORIZED)
            .errors(List.of(ex.getLocalizedMessage()))
            .message("Spring security authentication fail")
            .path("")
            .build();
    
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
  }
  
  /* === ForbiddenException === */
  @ExceptionHandler({ ForbiddenException.class })
  @ResponseBody
  public ResponseEntity<ApiError> handleForbiddenException(ForbiddenException ex) {
    ApiError apiError = ApiError.builder()
            .timestamp(new Date())
            .status(HttpStatus.FORBIDDEN)
            .errors(List.of(ex.getLocalizedMessage()))
            .message(ex.getMessage())
            .path("")
            .build();
    
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
  }
}