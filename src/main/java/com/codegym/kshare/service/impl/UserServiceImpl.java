package com.codegym.kshare.service.impl;

import com.codegym.kshare.model.User;
import com.codegym.kshare.repository.UserRepository;
import com.codegym.kshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User getById(int id) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        System.out.println(username);
        System.out.println(userRepository.findUserByUsername(username).toString());
        return userRepository.findUserByUsername(username);
    }
}
