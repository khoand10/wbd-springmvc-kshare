package com.codegym.kshare.repository;

import com.codegym.kshare.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    User findUserByUsername(String username);

}
