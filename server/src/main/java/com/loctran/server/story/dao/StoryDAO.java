package com.loctran.server.story.dao;

import com.loctran.server.story.model.Story;
import com.loctran.server.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StoryDAO {
  Optional<Story> save(Story request);
  Optional<Story> findBySlug(String slug);
  Optional<Story> findById(int id);
  Page<Story> getStoryPaginating(Pageable pageable);
  void delete(Story story);

  
 
}
