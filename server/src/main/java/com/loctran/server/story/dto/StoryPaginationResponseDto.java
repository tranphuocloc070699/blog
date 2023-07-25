package com.loctran.server.story.dto;

import com.loctran.server.story.model.Comment;
import com.loctran.server.story.model.Story;
import com.loctran.server.story.model.Toc;
import com.loctran.server.user.dto.UserResponseDto;
import com.loctran.server.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryPaginationResponseDto {
  private Integer id;
  private String thumbnail;
  private String title;
  private String slug;
  private Toc[] toc;
  private String content;
  private Date createdAt;
  private Date updatedAt;
  public static StoryPaginationResponseDto fromStory(Story story) {
    return StoryPaginationResponseDto.builder()
            .id(story.getId())
            .thumbnail(story.getThumbnail())
            .title(story.getTitle())
            .slug(story.getSlug())
            .toc(story.getToc())
            .createdAt(story.getCreatedAt())
            .updatedAt((story.getUpdatedAt()))
            .build();
  }

}