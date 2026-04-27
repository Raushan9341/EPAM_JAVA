package com.example.jwt_auth_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTraderName(String traderName);
    List<Order> findByFarmerName(String farmerName);
}