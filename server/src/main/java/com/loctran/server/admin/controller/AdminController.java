package com.loctran.server.admin.controller;

import com.loctran.server.admin.dto.AdminDeleteImageDto;
import com.loctran.server.admin.service.AdminService;
import com.loctran.server.admin.service.FileService;
import com.loctran.server.shared.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
  
  private final FileService fileService;
  private final AdminService adminService;
 
  
  @GetMapping(path = "hello")
  public String hello() {
    return "GET:: Hello Admin";
  }
  
  @PostMapping("/story")
  public ResponseEntity<ResponseObject> createStory(
          @RequestPart(value = "title") String title,
          @RequestPart(value = "slug") String slug,
          @RequestPart(value = "pre_content") String preContent,
          @RequestPart(value = "content") String content,
          @RequestPart(value = "toc", required = false) String toc,
          @RequestPart(name = "thumbnail") MultipartFile thumbnail
  ) {
    return adminService.createStory(title, slug, preContent, content, toc, thumbnail);
  }
  
  @PutMapping(value = "/story")
  public ResponseEntity<ResponseObject> updateStory(
          @RequestPart(value = "slug") String slug,
          @RequestPart(value = "model") String model,
          @RequestPart(name = "new_thumbnail", required = false) MultipartFile thumbnail
  ) {
    return adminService.updateStory(slug, model, thumbnail);
  }
  
  @PutMapping("/story/comment")
  public ResponseEntity<ResponseObject> updateComment(
          @RequestPart(value = "comment_id") String commentId,
          @RequestPart(value = "email", required = false) String email,
          @RequestPart(value = "name", required = false) String name,
          @RequestPart(value = "content", required = false) String content
  
  ) {
    return adminService.updateCommentByAdmin(Integer.parseInt(commentId), email, name, content);
  }
  
  
  @PostMapping("/story/upload-image")
  public ResponseEntity<ResponseObject> uploadFile(@RequestParam(value = "file") MultipartFile file) {
    return adminService.uploadImage(file);
  }
  
  @DeleteMapping("/story/delete-image")
  public ResponseEntity<ResponseObject> deleteFile(@RequestBody AdminDeleteImageDto dto) {
    return adminService.deleteImage(dto.getUrl());
  }
  
  @DeleteMapping("/story/{slug}")
  public ResponseEntity<ResponseObject> deleteStory(@PathVariable("slug") String slug) {
    return adminService.deleteStory(slug);
  }
  
  @DeleteMapping("/story/comment/{comment_id}")
  public ResponseEntity<ResponseObject> deleteUserComment(@PathVariable("comment_id") Integer commentId) {
    return adminService.deleteCommentByAdmin(commentId);
  }

}

