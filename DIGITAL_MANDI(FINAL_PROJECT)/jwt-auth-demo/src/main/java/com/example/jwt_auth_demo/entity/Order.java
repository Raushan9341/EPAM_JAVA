package com.example.jwt_auth_demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String traderName;       // Jo buy kar raha hai
    private String farmerName;       // Jiska crop hai
    private String cropName;
    private double quantity;
    private double price;
    private String unit;
    private String location;
    private String status;           // PENDING, CONFIRMED, COMPLETED

    private LocalDateTime orderedAt;

    @PrePersist
    public void prePersist() {
        this.orderedAt = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING";
    }
}