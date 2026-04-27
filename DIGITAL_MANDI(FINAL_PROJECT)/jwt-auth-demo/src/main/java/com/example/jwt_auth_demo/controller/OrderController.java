package com.example.jwt_auth_demo.controller;

import com.example.jwt_auth_demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // Trader order place karega
    @PostMapping("/place")
    public Map<String, String> placeOrder(@RequestBody Order order, Authentication auth) {
        order.setTraderName(auth.getName());
        orderRepository.save(order);
        return Map.of("message", "Order placed successfully!");
    }

    // Trader apne orders dekhega
    @GetMapping("/my-orders")
    public List<Order> myOrders(Authentication auth) {
        return orderRepository.findByTraderName(auth.getName());
    }

    // Farmer dekhega kaun kaun ne order diya
    @GetMapping("/received")
    public List<Order> receivedOrders(Authentication auth) {
        return orderRepository.findByFarmerName(auth.getName());
    }
}