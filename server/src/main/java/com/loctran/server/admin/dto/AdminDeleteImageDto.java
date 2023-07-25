package com.loctran.server.admin.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AdminDeleteImageDto {
  @NotEmpty(message = "The url is required.")
  private String url;
}
