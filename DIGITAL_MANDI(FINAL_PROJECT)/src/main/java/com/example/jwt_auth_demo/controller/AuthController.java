package com.example.jwt_auth_demo.controller;

import com.example.jwt_auth_demo.entity.User;
import com.example.jwt_auth_demo.entity.LoginRequest;
import com.example.jwt_auth_demo.repository.UserRepository;
import com.example.jwt_auth_demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String registerUser(@RequestBody User userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));

        String rawRole = (userInfo.getRole() == null) ? "FARMER" : userInfo.getRole().toUpperCase();
        if (!rawRole.startsWith("ROLE_")) {
            userInfo.setRole("ROLE_" + rawRole);
        } else {
            userInfo.setRole(rawRole);
        }

        userRepository.save(userInfo);
        return "User Registered Successfully as " + userInfo.getRole();
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest loginData) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginData.getUsername(), loginData.getPassword()
                    )
            );

            if (auth.isAuthenticated()) {
                User user = userRepository.findByUsername(loginData.getUsername())
                        .orElseThrow(() -> new RuntimeException("User nahi mila!"));

                String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                String roleForFrontend = user.getRole().replace("ROLE_", "").toLowerCase();


                return Map.of(
                        "token", token,
                        "role", roleForFrontend,
                        "username", user.getUsername(),   // ✅ ADDED
                        "message", "Login Successful!"
                );
            }
        } catch (Exception e) {
            return Map.of("message", "Invalid Username or Password!");
        }
        return Map.of("message", "Authentication Fail!");
    }
}