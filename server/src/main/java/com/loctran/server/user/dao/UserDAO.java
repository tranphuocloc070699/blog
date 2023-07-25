package com.loctran.server.user.dao;

import com.loctran.server.user.model.User;

import java.util.Optional;

public interface UserDAO {

    Optional<User> save(User requestUser);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

}
