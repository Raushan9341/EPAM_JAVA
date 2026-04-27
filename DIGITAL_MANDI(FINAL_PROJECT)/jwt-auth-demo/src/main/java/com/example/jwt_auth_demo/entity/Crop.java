package com.example.jwt_auth_demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "crops")
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double quantity;
    private double price;
    private String unit;
    private String location;
    private String farmerName;
    private String category; // ✅ NAYA FIELD
}