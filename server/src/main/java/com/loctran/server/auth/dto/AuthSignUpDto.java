package com.loctran.server.auth.dto;

import com.loctran.server.user.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthSignUpDto {

    @NotEmpty(message = "The email is required.")
    @Email(message = "The email address is invalid.", flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;

    @NotEmpty(message = "The password is required.")
    private String password;

    @NotEmpty(message = "The name is required.")
    private String name;
    
    
    private Role role;
    
}