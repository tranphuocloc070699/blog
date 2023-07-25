package com.loctran.server.auth;

import com.loctran.server.shared.ResponseObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AuthResponse extends ResponseObject {
  private String accessToken;
  
 
  public AuthResponse(Date timestamp, HttpStatus status, Object data, String message, String path, String accessToken) {
    super(timestamp, status, data, message, path);
    this.accessToken = accessToken;
  }
}
