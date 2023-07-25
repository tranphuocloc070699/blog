package com.loctran.server.story.dao;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.loctran.server.story.model.Story;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story,Integer> {

  Optional<Story> findStoryWithCommentsBySlug(String slug);

  Page<Story> findAll( Pageable pageable);
  

}
