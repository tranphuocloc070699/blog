package com.loctran.server.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdminCreateStoryDto {
  
  @NotEmpty(message = "The thumbnail file is required.")
  private MultipartFile thumbnail;
  
  private String[] toc;
  
  @NotEmpty(message = "The content is required.")
  private String content;
  
}