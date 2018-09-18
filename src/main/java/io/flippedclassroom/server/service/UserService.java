package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.User;

import java.util.List;

public interface UserService extends BaseService<User> {
    User findUserByUsername(String username);
}
