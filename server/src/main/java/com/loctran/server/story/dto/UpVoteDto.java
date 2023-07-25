package com.loctran.server.story.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpVoteDto {
  
//  @NotNull(message = "Story id is required.")
  @JsonProperty("story_id")
  private Integer storyId;
  
//  @NotEmpty(message = "Id is required.")
  private Long id;
 
  
}