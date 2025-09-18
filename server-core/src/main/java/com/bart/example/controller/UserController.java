package com.bart.example.controller;

import com.bart.example.infrastructure.webserving.annotations.GetMapping;
import com.bart.example.infrastructure.webserving.annotations.PostMapping;
import com.bart.example.infrastructure.injector.annotations.Singleton;
import com.bart.example.model.User;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

@Singleton
public class UserController {
    @GetMapping("/users")
    public List<User> getUsers(HttpExchange exchange) {
        return List.of(User.builder()
                .name("def")
                .age(20)
                .build());
    }

    @GetMapping("/users/{id}")
    public User getUserById(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String userId = path.substring(path.lastIndexOf('/') + 1);
        return User.builder()
                .name(userId)
                .age(20)
                .build();
    }

    @PostMapping("/users")
    public String createUser(HttpExchange exchange) {
        // Parse request body, create user
        return "User created";
    }
}
