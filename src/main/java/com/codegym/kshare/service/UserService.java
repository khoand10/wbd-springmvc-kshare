package com.codegym.kshare.service;

import com.codegym.kshare.model.User;

public interface UserService {

    User getById(int id);

    User getByUsername(String username);
}
