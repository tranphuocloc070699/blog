package com.loctran.server.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    private final String path;
    
    public ConflictException(String message, String path) {
        super(message);
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}