package com.codegym.kshare.controller;

import com.codegym.kshare.model.Login;
import com.codegym.kshare.model.User;
import com.codegym.kshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public ModelAndView loginForm(){
        ModelAndView modelAndView = new ModelAndView("/login");
        modelAndView.addObject("login", new Login());
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(Login login, HttpServletRequest request){
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        User user = null;
        System.out.println(login.toString());
        user = userService.getByUsername(login.getUsername());
        System.out.println(user.toString());
        if(user!= null && user.getPasword().equals(login.getPassword())){
            //System.out.println("Login : "+user.toString());
            session.setAttribute("user", user);
            modelAndView.setViewName("redirect:/");
        }else{
            modelAndView.setViewName("/login");
            modelAndView.addObject("message", "Login fail!");
        }
        return modelAndView;
    }

}
