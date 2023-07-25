package com.loctran.server.story.service;

import com.loctran.server.admin.service.AdminService;
import com.loctran.server.exception.custom.ConflictException;
import com.loctran.server.exception.custom.ForbiddenException;
import com.loctran.server.exception.custom.NotFoundException;
import com.loctran.server.shared.ResponseObject;
import com.loctran.server.story.dao.CommentDataAccess;
import com.loctran.server.story.dao.StoryDataAccess;
import com.loctran.server.story.dto.StoryPaginationResponseDto;
import com.loctran.server.story.model.Comment;
import com.loctran.server.story.model.Story;
import com.loctran.server.user.dto.UserResponseDto;
import com.loctran.server.user.model.User;
import com.loctran.server.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StoryService {
  private final StoryDataAccess storyDataAccess;
  private final CommentDataAccess commentDataAccess;
  private final AdminService adminService;
  private final HttpServletRequest httpServletRequest;
  private final UserService userService;
  
  //=== get story ===
  public ResponseEntity<ResponseObject> getStory(String slug){
    try{
      Optional<Story> storyExisting = storyDataAccess.findBySlug(slug);

      if(storyExisting.isEmpty())
        throw new NotFoundException("story with slug ["+slug+"] not found", httpServletRequest.getServletPath());
      Map<String,Story> data = new HashMap<>();
      data.put("story",storyExisting.get());

      ResponseObject responseObject = ResponseObject.builder()
              .path(httpServletRequest.getServletPath())
              .timestamp(new Date())
              .status(HttpStatus.OK)
              .message("get story successfully!")
              .data(data)
              .build();
      return ResponseEntity.ok(responseObject);
    }catch (RuntimeException exception){
      throw  new ForbiddenException(exception.getMessage());
    }
  }
  
  //=== get story paginating ===
  public ResponseEntity<ResponseObject> getStoryPaginating(int currentPage,int itemsPerPage,String createdAt){
    
    Sort.Direction createdAtSort= createdAt.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    
    Pageable sortByCreatedAt = PageRequest.of(currentPage,
            itemsPerPage,
            Sort.by(createdAtSort,"createdAt"));
    
    
    Page<Story> stories = storyDataAccess.getStoryPaginating(sortByCreatedAt);
//    Page<StoryPaginationResponseDto> storiesDto = stories.map(StoryPaginationResponseDto::fromStory);
    
    
  
    ResponseObject responseObject = ResponseObject.builder()
            .path(httpServletRequest.getServletPath())
            .timestamp(new Date())
            .status(HttpStatus.OK)
            .message("get story successfully!")
            .data(stories)
            .build();
    
    return ResponseEntity.ok(responseObject);
  }

  //=== upvote ===
  public ResponseEntity<ResponseObject> upVoteStory(int storyId, long id){
    Optional<Story> storyOptional = storyDataAccess.findById(storyId);
    if(storyOptional.isEmpty()){
      throw new NotFoundException("story with id ["+storyId+"] not found", httpServletRequest.getServletPath());
    }
    User user = userService.getOptionalUserByToken();
    List<Long> upVotes = new ArrayList<>();
    if(storyOptional.get().getUpvote()!=null  && storyOptional.get().getUpvote().size()>0){
      upVotes =storyOptional.get().getUpvote();
    }
    
    if(user!=null){
      boolean isUpVote = upVotes.contains(Long.valueOf(user.getId()));
      if(isUpVote){
        throw new ConflictException("story already upVoted by user ["+user.getName()+"]", httpServletRequest.getServletPath());
      }
      upVotes.add((long) storyId);
      storyOptional.get().setUpvote(upVotes);
    }
    boolean isUpVote = upVotes.contains(id);
    if(isUpVote){
      throw new ConflictException("story already upVoted by id ["+id+"]", httpServletRequest.getServletPath());
    }
    upVotes.add(id);
    storyOptional.get().setUpvote(upVotes);
    
     storyDataAccess.save(storyOptional.get());
  
  
    ResponseObject responseObject = ResponseObject.builder()
            .path(httpServletRequest.getServletPath())
            .timestamp(new Date())
            .status(HttpStatus.OK)
            .message("upvote story successfully!")
            .data(null)
            .build();
  
    return ResponseEntity.ok(responseObject);
  }
}
