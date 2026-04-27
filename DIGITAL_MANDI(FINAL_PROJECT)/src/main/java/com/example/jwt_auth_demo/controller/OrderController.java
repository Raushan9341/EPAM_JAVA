package com.example.jwt_auth_demo.controller;

import com.example.jwt_auth_demo.entity.Crop;
import com.example.jwt_auth_demo.entity.Order;
import com.example.jwt_auth_demo.entity.User;
import com.example.jwt_auth_demo.repository.CropRepository;
import com.example.jwt_auth_demo.repository.OrderRepository;
import com.example.jwt_auth_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody Order order, Authentication auth) {
        try {
            String traderName = auth.getName();
            order.setTraderName(traderName);
            order.setStatus("PENDING");
            order.setOrderedAt(LocalDateTime.now());

            List<Crop> crops = cropRepository.findAll();
            crops.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(order.getCropName())
                            && c.getFarmerName().equalsIgnoreCase(order.getFarmerName()))
                    .findFirst()
                    .ifPresent(c -> order.setFarmerPhone(c.getPhone()));

            Order saved = orderRepository.save(order);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Order place nahi hua: " + e.getMessage()));
        }
    }

    @GetMapping("/received")
    public ResponseEntity<?> getOrdersForFarmer(Authentication auth) {
        try {
            List<Order> orders = orderRepository.findByFarmerName(auth.getName());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Orders load nahi hue"));
        }
    }


    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(Authentication auth) {
        try {
            List<Order> orders = orderRepository.findByTraderName(auth.getName());

            // ✅ Phone number hide karo agar status PENDING hai
            orders.forEach(o -> {
                if (!"CONFIRMED".equals(o.getStatus())) {
                    o.setFarmerPhone(null); // PENDING order mein phone mat dikhao
                }
            });

            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Orders load nahi hue"));
        }
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<?> confirmOrder(@PathVariable Long id, Authentication auth) {
        return orderRepository.findById(id).map(order -> {
            if (!order.getFarmerName().equals(auth.getName())) {
                return ResponseEntity.status(403)
                        .body(Map.of("message", "Unauthorized - Ye aapka order nahi hai"));
            }
            if ("CONFIRMED".equals(order.getStatus())) {
                return ResponseEntity.ok(Map.of("message", "Order already confirmed hai"));
            }

            order.setStatus("CONFIRMED");
            orderRepository.save(order);

            return ResponseEntity.ok(Map.of(
                    "message", "Order Confirmed! 🎉",
                    "orderId", order.getId(),
                    "traderName", order.getTraderName(),
                    "farmerPhone", order.getFarmerPhone() != null ? order.getFarmerPhone() : ""
            ));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectOrder(@PathVariable Long id, Authentication auth) {
        return orderRepository.findById(id).map(order -> {
            if (!order.getFarmerName().equals(auth.getName())) {
                return ResponseEntity.status(403)
                        .body(Map.of("message", "Unauthorized - Ye aapka order nahi hai"));
            }
            if (!"PENDING".equals(order.getStatus())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Sirf PENDING orders reject ho sakte hain"));
            }

            order.setStatus("REJECTED");
            orderRepository.save(order);

            return ResponseEntity.ok(Map.of(
                    "message", "Order rejected.",
                    "orderId", order.getId()
            ));
        }).orElse(ResponseEntity.notFound().build());
    }
}