package com.loctran.server.story.dao;

import com.loctran.server.story.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
  
  @Transactional
  @Modifying()
  @Query("DELETE FROM Comment c WHERE c.story.id = :storyId")
  void deleteAllByStoryId(Integer storyId);
  
  @Query("SELECT new com.loctran.server.story.model.Comment(c.id, c.name, c.email, c.content, c.createdAt, c.user) FROM Comment c WHERE c.story.id = :storyId")
  List<Comment> findCommentDetailsByStoryId(@Param("storyId") Integer storyId);
}
