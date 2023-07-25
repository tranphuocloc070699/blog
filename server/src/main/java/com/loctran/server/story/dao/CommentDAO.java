package com.loctran.server.story.dao;

import com.loctran.server.story.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDAO {
  Comment save(Comment comment);
  
  Optional<Comment> findById(Integer id);
  
  void delete(Comment comment);
  
  void deleteAllByStoryId(Integer id);
  
  List<Comment> findByStoryId(Integer storyId);
  
}
