package com.codegym.kshare.formatter;

import com.codegym.kshare.model.User;
import com.codegym.kshare.service.UserService;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class UserFormatter implements Formatter<User> {

    private UserService userService;

    public UserFormatter(UserService userService){
        this.userService = userService;
    }

    @Override
    public User parse(String text, Locale locale) throws ParseException {
        int id = Integer.parseInt(text);
        return userService.getById(id);
    }

    @Override
    public String print(User object, Locale locale) {
        return object.getName();
    }
}
