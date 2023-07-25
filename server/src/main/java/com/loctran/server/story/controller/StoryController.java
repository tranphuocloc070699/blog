package com.loctran.server.story.controller;

import com.loctran.server.shared.ResponseObject;
import com.loctran.server.story.dto.CreateCommentDto;
import com.loctran.server.story.dto.UpVoteDto;
import com.loctran.server.story.service.CommentService;
import com.loctran.server.story.service.StoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/story/")
public class StoryController {
  private final StoryService storyService;
  private final CommentService commentService;
  @GetMapping("{slug}")
  ResponseEntity<ResponseObject> getStory(@PathVariable("slug") String slug){
    return storyService.getStory(slug);
  }
  
  @GetMapping("paginating")
  ResponseEntity<ResponseObject> getStoryPaginating(
          @RequestParam(value="current_page",defaultValue = "0",required = false) int currentPage,
          @RequestParam(value="items_per_page",defaultValue = "2") int itemsPerPage,
          @RequestParam(value="created_at",defaultValue = "desc",required = false) String createdAt
  ){
    
    return storyService.getStoryPaginating(currentPage,itemsPerPage,createdAt);
  }
  
  @PostMapping("comment")
  ResponseEntity<ResponseObject> createComment(@Valid @RequestBody CreateCommentDto dto){
    return commentService.createComment(dto);
  }
  

  
  @PostMapping("upvote")
  ResponseEntity<ResponseObject> upvote( @RequestBody UpVoteDto dto) {
    return storyService.upVoteStory(dto.getStoryId(),dto.getId());
  }
}
