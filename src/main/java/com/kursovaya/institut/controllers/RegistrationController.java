package com.kursovaya.institut.controllers;

import com.kursovaya.institut.models.User;
import com.kursovaya.institut.models.Role;
import com.kursovaya.institut.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDb = userRepository.findUserByUsername(user.getUsername());
        if (userFromDb != null) {
            model.addAttribute("message", "Такой пользователь уже существует");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Set.of(Role.USER));

        userRepository.save(user);
        return "redirect:/";
    }
}
