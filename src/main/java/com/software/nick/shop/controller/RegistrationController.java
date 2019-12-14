package com.software.nick.shop.controller;

import com.software.nick.shop.model.Role;
import com.software.nick.shop.model.User;
import com.software.nick.shop.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "registration";
        }

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        user.setActive(true);
        user.setRoles(roles);
        userRepo.save(user);

        return "redirect:/login";
    }
}