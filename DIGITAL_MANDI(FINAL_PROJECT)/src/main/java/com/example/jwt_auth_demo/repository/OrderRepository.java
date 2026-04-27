package com.example.jwt_auth_demo.repository;

import com.example.jwt_auth_demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Farmer ke liye - uske naam pe aaye saare orders
    List<Order> findByFarmerName(String farmerName);

    // Trader ke liye - usne jo orders place kiye
    List<Order> findByTraderName(String traderName);

    // Pending orders count (optional - stats ke liye)
    List<Order> findByFarmerNameAndStatus(String farmerName, String status);
}