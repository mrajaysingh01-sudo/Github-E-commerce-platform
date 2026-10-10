package com.ecommerce.controller;

import com.ecommerce.model.Role;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        // Prevent duplicate emails
        if (userRepository.findByEmail(email).isPresent()) {
            return "redirect:/login?error";
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password); // Matches NoOpPasswordEncoder

        // CRITICAL FIXES: Forces the account to be active and assigns a buyer role
        newUser.setRole(Role.BUYER);
        newUser.setEnabled(true);

        userRepository.save(newUser);

        return "redirect:/login?registered";
    }
}