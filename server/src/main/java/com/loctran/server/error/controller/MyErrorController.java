package com.loctran.server.error.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {
  
  @RequestMapping("/error")
  public ResponseEntity<String> handle404Error(HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 Not Found");
  }
}