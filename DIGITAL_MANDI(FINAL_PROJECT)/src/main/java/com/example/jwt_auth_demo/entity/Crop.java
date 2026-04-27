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
    private String category;
    private double quantity;
    private double price;
    private String unit;
    private String location;
    private String farmerName;

    // ✅ NEW: Farmer ka phone number (traders ko sirf confirm ke baad dikhega)
    private String phone;
}