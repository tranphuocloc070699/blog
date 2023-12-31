package com.loctran.server.user.dao;

import com.loctran.server.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
