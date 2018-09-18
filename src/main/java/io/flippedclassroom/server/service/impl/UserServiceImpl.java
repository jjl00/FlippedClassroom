package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.repository.UserRepository;
import io.flippedclassroom.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    @Transactional
    public User findUserByUsername(String username) {
        return userRepository.findByUser_name(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> save(Iterable<User> iterable) {
        return userRepository.save(iterable);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.delete(id);
    }

    @Override
    public void delete(Iterable<User> iterable) {
        userRepository.delete(iterable);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }


}
