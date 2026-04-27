package com.example.jwt_auth_demo.entity; // Tera sahi path

public class LoginRequest {
    private String username;
    private String password;

    // Getters and Setters (Shortcut: Alt + Insert dabao IntelliJ mein)
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}