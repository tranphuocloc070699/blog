package com.loctran.server.story.dao;

import com.loctran.server.story.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentDataAccess implements CommentDAO{
  private final CommentRepository commentRepository;
  

  @Override
  public Comment save(Comment comment) {
    return commentRepository.save(comment);
  }
  
  @Override
  public Optional<Comment> findById(Integer id) {
    return commentRepository.findById(id);
  }
  
  @Override
  public void delete(Comment comment) {
    commentRepository.delete(comment);
    
  }
  
  @Override
  public void deleteAllByStoryId(Integer id) {
    commentRepository.deleteAllByStoryId(id);
  }
  
  @Override
  public List<Comment> findByStoryId(Integer storyId) {
    return commentRepository.findCommentDetailsByStoryId(storyId);
  }
  
  
}
