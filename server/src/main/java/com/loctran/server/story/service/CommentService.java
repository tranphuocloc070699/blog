package com.loctran.server.story.service;

import com.loctran.server.admin.service.AdminService;
import com.loctran.server.exception.custom.ForbiddenException;
import com.loctran.server.shared.ResponseObject;
import com.loctran.server.story.dao.CommentDataAccess;
import com.loctran.server.story.dao.StoryDataAccess;
import com.loctran.server.story.dto.CreateCommentDto;
import com.loctran.server.story.model.Comment;
import com.loctran.server.story.model.Story;
import com.loctran.server.user.dto.UserResponseDto;
import com.loctran.server.user.model.User;
import com.loctran.server.util.LoggerColor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final AdminService adminService;
  private final StoryDataAccess storyDataAccess;
  private final HttpServletRequest httpServletRequest;
  private final CommentDataAccess commentDataAccess;
  public ResponseEntity<ResponseObject> createComment(CreateCommentDto dto){
    Optional<Story> storyOptional =  storyDataAccess.findById(dto.getStoryId());
    if(storyOptional.isEmpty())
      throw new ForbiddenException("story with id ["+dto.getStoryId()+"] not found");
    Comment newComment = Comment.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .content(dto.getContent())
            .story(storyOptional.get())
            .build();
//    if(dto.getParentCommentId()!=null){
//      newComment.setParentCommentId(dto.getParentCommentId());
//    }
    User author = adminService.getUserByToken();

    if(author!=null){
      UserResponseDto userResponseDto = UserResponseDto.fromUser(author);
      newComment.setUser(author);
      newComment.setName(userResponseDto.getName());
    }
    
    Comment commentSaved = commentDataAccess.save(newComment);
    
    Map<String,Comment> data = new HashMap<>();
    data.put("comment",commentSaved);
    
    ResponseObject responseObject = ResponseObject.builder()
            .path(httpServletRequest.getServletPath())
            .timestamp(new Date())
            .status(HttpStatus.CREATED)
            .message("create comment successfully!")
            .data(data)
            .build();
    return ResponseEntity.ok(responseObject);
  }
}
