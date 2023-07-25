package com.loctran.server.story.dao;

import com.loctran.server.story.model.Story;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository()
@RequiredArgsConstructor
public class StoryDataAccess implements StoryDAO{
  private final StoryRepository storyRepository;
  @Override
  public Optional<Story> save(Story request) {
    Story story = storyRepository.save(request);
    return Optional.of(story);
  }
  
  @Override
  public Optional<Story> findBySlug(String slug) {
    return storyRepository.findStoryWithCommentsBySlug(slug);
  }
  
  @Override
  public Optional<Story> findById(int id) {
    return storyRepository.findById(id);
  }
  
  @Override
  public Page<Story> getStoryPaginating(Pageable pageable) {
    return storyRepository.findAll(pageable);
  }
  
  @Override
  public void delete(Story story) {
     storyRepository.delete(story);
  }



}
