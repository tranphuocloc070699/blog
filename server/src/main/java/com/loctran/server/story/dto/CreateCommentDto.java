package com.loctran.server.story.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCommentDto {
  
  @NotEmpty(message = "The email is required.")
  @Email(message = "The email address is invalid.", flags = { Pattern.Flag.CASE_INSENSITIVE })
  private String email;
  
  @NotEmpty(message = "Content page is required.")
  private String content;
  
  @NotNull(message = "Story id is required.")
  @JsonProperty("story_id")
  private Integer storyId;

  @JsonProperty("paren_comment_id")
  private Integer parentCommentId;
  
  private String name;
  
}