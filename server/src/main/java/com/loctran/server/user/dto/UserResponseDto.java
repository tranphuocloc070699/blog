package com.loctran.server.user.dto;

import com.loctran.server.story.model.Comment;
import com.loctran.server.story.model.Story;
import com.loctran.server.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
  private Integer id;
  private String email;
  private String role;
  private String name;
  private List<Integer> storyIds;
  private List<Integer> commentIds;
  
  public static UserResponseDto fromUser(User user) {
    return UserResponseDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .role(user.getRole().name())
            .name(user.getName())
            .build();
  }
}