package com.planora.backend.controller;

import com.planora.backend.dto.LoginRequest;
import com.planora.backend.dto.RegisterRequest;
import com.planora.backend.model.User;
import com.planora.backend.repository.UserRepository;
import com.planora.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        if(userRepo.findByEmail(req.email).isPresent()){
            return "Email already exists";
        }
        User user = new User();
        user.setName(req.name);
        user.setEmail(req.email);
        user.setPassword(encoder.encode(req.password));
        userRepo.save(user);
        return "User registered successfully";
    }

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signin")
    public String login(@RequestBody LoginRequest req) {
        User user = userRepo.findByEmail(req.email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(encoder.matches(req.password, user.getPassword())) {
            return jwtUtil.generateToken(user.getEmail());
        }
        throw new RuntimeException("Invalid credentials");
    }

}
