package com.kursovaya.institut.controllers;

import com.kursovaya.institut.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userlist(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user_list";
    }
}
