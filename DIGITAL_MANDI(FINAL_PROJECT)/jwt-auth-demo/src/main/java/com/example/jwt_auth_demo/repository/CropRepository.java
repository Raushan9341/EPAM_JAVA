package com.example.jwt_auth_demo.repository;

import com.example.jwt_auth_demo.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CropRepository extends JpaRepository<Crop, Long> {}