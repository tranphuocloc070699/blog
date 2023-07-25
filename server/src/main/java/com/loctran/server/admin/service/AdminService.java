package com.loctran.server.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loctran.server.exception.custom.ForbiddenException;
import com.loctran.server.exception.custom.UnAuthenticateException;
import com.loctran.server.story.dao.CommentDataAccess;
import com.loctran.server.story.dao.StoryDataAccess;
import com.loctran.server.config.JwtService;
import com.loctran.server.exception.custom.ConflictException;
import com.loctran.server.exception.custom.NotFoundException;
import com.loctran.server.shared.ResponseObject;
import com.loctran.server.story.model.Comment;
import com.loctran.server.story.model.Story;
import com.loctran.server.story.model.Toc;
import com.loctran.server.user.dao.UserDataAccess;
import com.loctran.server.user.model.User;
import com.loctran.server.util.LoggerColor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {
  
  private final FileService fileService;
  private final HttpServletRequest httpServletRequest;
  private final JwtService jwtService;
  private final UserDataAccess userDataAccess;
  private final StoryDataAccess storyDataAccess;
  private final CommentDataAccess commentDataAccess;
  
  // === create story ===
 public ResponseEntity<ResponseObject> createStory(String tile,String slug,String preContent,String content,String toc, MultipartFile file){
    try{
      User admin = getUserByToken();
      ResponseObject object = fileService.uploadImage(file);
      if(!object.getStatus().equals(HttpStatus.OK)){
        throw new ConflictException("upload thumbnail failure", httpServletRequest.getServletPath());
      }
    
      ObjectMapper mapper = new ObjectMapper();
      
      Map<String,String> image = mapper.convertValue(object.getData(), new TypeReference<>(){});
     
      Story newStory = Story.builder()
              .title(tile)
              .slug(slug)
              .preContent(preContent)
              .content(content)
              .thumbnail(image.get("url"))
           
              .author(admin)
              .build();
      if(toc!=null){
        Toc[] tocMapped = mapper.readValue(toc, Toc[].class);
        newStory.setToc(tocMapped);
      }
  
      Optional<Story> story = storyDataAccess.save(newStory);
      if(story.isEmpty()) throw new ConflictException("request invalid", httpServletRequest.getServletPath());
      
      
      Map<String,Story> data = new HashMap<>();
      data.put("story",newStory);
      ResponseObject responseObject = ResponseObject.builder()
              .timestamp(new Date())
              .status(HttpStatus.CREATED)
              .path(httpServletRequest.getServletPath())
              .message("create story successfully!")
              .data(data)
              .build();
      return ResponseEntity.ok(responseObject);
      
    }
    catch (JsonProcessingException e) {
      LoggerColor.println(LoggerColor.Color.RED,e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
    catch (ConflictException e){
      throw new ConflictException(e.getMessage(), e.getPath());
    }
  }
  // === update story ===
  public ResponseEntity<ResponseObject> updateStory(String slug, String model, MultipartFile newThumbnail)  {

     ObjectMapper mapper = new ObjectMapper();
   getUserByToken();
      Optional<Story> storyExisting = this.storyDataAccess.findBySlug(slug);
      if(storyExisting.isEmpty()){
        throw new NotFoundException("story with slug ["+slug+"] not found", httpServletRequest.getServletPath());
      }
      
   Story story = storyExisting.get();
    Story storyRequest = null;
    try {
      storyRequest = mapper.readValue(model, Story.class);
    } catch (JsonProcessingException e) {
      throw new ConflictException(e.getLocalizedMessage(), httpServletRequest.getServletPath());
    }
      if(newThumbnail!=null){
        ResponseObject objectDelete = fileService.deleteImage(story.getThumbnail());
        if(!objectDelete.getStatus().equals(HttpStatus.OK)){
          throw new ConflictException(objectDelete.getMessage(), httpServletRequest.getServletPath());
        }
        ResponseObject objectUpload = fileService.uploadImage(newThumbnail);
        if(!objectUpload.getStatus().equals(HttpStatus.OK)){
          throw new ConflictException("upload image not successfully!", httpServletRequest.getServletPath());
        }
              Map<String,String> image = mapper.convertValue(objectUpload.getData(), new TypeReference<>(){});
              story.setThumbnail(image.get("url"));
      }
      
      if(!story.getTitle().equals(storyRequest.getTitle())){
        story.setTitle(storyRequest.getTitle());
      }
    if(!story.getPreContent().equals(storyRequest.getPreContent())){
      story.setPreContent(storyRequest.getPreContent());
    }
    if(!story.getContent().equals(storyRequest.getContent())){
      story.setContent(storyRequest.getPreContent());
    }
    if(!story.getSlug().equals(storyRequest.getSlug())){
      story.setSlug(storyRequest.getSlug());
    }
      
      Optional<Story> storyUpdated = storyDataAccess.save(story);
      if(storyUpdated.isEmpty()){
        throw new ConflictException("cannot save story", httpServletRequest.getServletPath());
      }
  
     Map<String,Story> data = new HashMap<>();
     data.put("story",storyUpdated.get());
     ResponseObject responseObject = ResponseObject.builder()
             .timestamp(new Date())
             .status(HttpStatus.CREATED)
             .path(httpServletRequest.getServletPath())
             .message("create story successfully!")
             .data(data)
             .build();
     return ResponseEntity.ok(responseObject);
  
  }
  // === delete story ===
  public ResponseEntity<ResponseObject> deleteStory(String slug){

     Optional<Story> storyExisting = storyDataAccess.findBySlug(slug);
     if(storyExisting.isEmpty()){
       throw new NotFoundException("story with slug ["+ slug +"] not found", httpServletRequest.getServletPath());
     }

    ResponseObject object = fileService.deleteImage(storyExisting.get().getThumbnail());
     if(!object.getStatus().equals(HttpStatus.OK)){
       throw new ConflictException(object.getMessage(), httpServletRequest.getServletPath());
     }
    
    commentDataAccess.deleteAllByStoryId(storyExisting.get().getId());
    
     storyDataAccess.delete(storyExisting.get());
     Optional<Story> storyExistingAfterDelete = storyDataAccess.findBySlug(slug);
     if(storyExistingAfterDelete.isPresent()){
       throw new ConflictException("story with slug ["+slug+"] cannot delete", httpServletRequest.getServletPath());
     }
      ResponseObject responseObject = ResponseObject.builder()
             .timestamp(new Date())
             .status(HttpStatus.OK)
             .path(httpServletRequest.getServletPath())
             .message("delete story with slug [" +slug+ "] successfully!")
             .data(null)
             .build();
     return ResponseEntity.ok(responseObject);
  }
  
  public ResponseEntity<ResponseObject> updateCommentByAdmin(int commentId, String email,String name,String content){
   Optional<Comment> commentOptional = commentDataAccess.findById(commentId);
   if(commentOptional.isEmpty())
     throw new NotFoundException("comment with ["+commentId+"] not found", httpServletRequest.getServletPath());
  
    if(name!=null) commentOptional.get().setName(name);
    if(email!=null) commentOptional.get().setEmail(email);
    if(content!=null) commentOptional.get().setContent(content);
    
   Comment commentUpdated = commentDataAccess.save(commentOptional.get());
  Map<String,Comment> data = new HashMap<>();
  data.put("comment",commentUpdated);
    ResponseObject responseObject = ResponseObject.builder()
            .timestamp(new Date())
            .status(HttpStatus.OK)
            .path(httpServletRequest.getServletPath())
            .message("update comment successfully!")
            .data(data)
            .build();
    
    return ResponseEntity.ok(responseObject);
  }
  
  public ResponseEntity<ResponseObject> deleteCommentByAdmin(int commentId){
    Optional<Comment> commentOptional = commentDataAccess.findById(commentId);
    if(commentOptional.isEmpty()){
      throw new NotFoundException("comment with ID ["+ commentId +"] not found", httpServletRequest.getServletPath());
    }
    
    commentDataAccess.delete(commentOptional.get());
    Optional<Comment> commentExistingAfterDelete = commentDataAccess.findById(commentId);
    if(commentExistingAfterDelete.isPresent()){
      throw new ConflictException("comment with id ["+commentId+"] cannot delete", httpServletRequest.getServletPath());
    }
    ResponseObject responseObject = ResponseObject.builder()
            .timestamp(new Date())
            .status(HttpStatus.OK)
            .path(httpServletRequest.getServletPath())
            .message("delete comment with ID [" +commentId+ "] successfully!")
            .data(null)
            .build();
    return ResponseEntity.ok(responseObject);
   
  }
  
  
 public ResponseEntity<ResponseObject> uploadImage(MultipartFile file){
    try{
      ResponseObject object = fileService.uploadImage(file);
      if(!object.getStatus().equals(HttpStatus.OK)){
        throw new RuntimeException("not successfully!");
      }
      
      return ResponseEntity.ok(object);
    }
    catch (ConflictException e){
      throw new ConflictException(e.getMessage(), e.getPath());
    }
    catch (RuntimeException e){
      throw new ForbiddenException(e.getMessage());
    }
  }
  
 public ResponseEntity<ResponseObject> deleteImage(String url){
    try{
      if(url.trim().isEmpty() || !url.startsWith("http")){
        throw new ConflictException("url invalid", httpServletRequest.getServletPath());
      }
      ResponseObject object = fileService.deleteImage(url);
      return ResponseEntity.ok(object);
    }
    catch (NotFoundException e){
      throw new NotFoundException(e.getMessage(),e.getPath());
    }
    catch (RuntimeException e){
      throw new ForbiddenException(e.getMessage());
    }
  }
  
  public User getUserByToken(){
    String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if(authHeader==null || !authHeader.startsWith("Bearer ")) return null;
    String accessToken = authHeader.substring(7);
    String email = jwtService.extractUsername(accessToken);
    if(email==null || !email.contains("@")) throw new UnAuthenticateException("email invalid");
    Optional<User> user = userDataAccess.findByEmail(email);
    if(user.isEmpty()) throw new NotFoundException("user not found with email ["+ email +"]", httpServletRequest.getServletPath());
    if(!jwtService.isTokenValid(accessToken,user.get())) throw new UnAuthenticateException("jwt invalid");
    
    return user.get();
  }
  
  public ResponseEntity<ResponseObject> getCommentsByStoryId(Integer storyId) {
    try{
      List<Comment> comments = commentDataAccess.findByStoryId(storyId);
      
      ResponseObject responseObject = ResponseObject.builder()
              .timestamp(new Date())
              .status(HttpStatus.OK)
              .path(httpServletRequest.getServletPath())
              .message("get comments with story ID [" +storyId+ "] successfully!")
              .data(comments)
              .build();
      return ResponseEntity.ok(responseObject);
    }
    catch (RuntimeException exception){
      throw new ConflictException(exception.getMessage(), httpServletRequest.getServletPath());
    }
  }
}
