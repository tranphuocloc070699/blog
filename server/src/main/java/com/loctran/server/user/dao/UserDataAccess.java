package com.loctran.server.user.dao;

import com.loctran.server.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository()
@RequiredArgsConstructor
public class UserDataAccess implements UserDAO {
  
  private final UserRepository userRepository;
  
  @Override
  public Optional<User> save(User requestUser) {
    User response = this.userRepository.save(requestUser);
    
    return Optional.of(response);
  }
  
  @Override
  public Optional<User> findByEmail(String email) {
    return this.userRepository.findByEmail(email);
  }
  
  @Override
  public Boolean existsByEmail(String email) {
    return this.userRepository.existsByEmail(email);
  }
  
}
