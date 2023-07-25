package com.loctran.server.user.dto;


import com.loctran.server.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {

  private String email;
  private String password;
  private String name;
  
}