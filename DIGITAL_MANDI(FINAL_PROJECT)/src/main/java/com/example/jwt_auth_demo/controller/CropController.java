package com.example.jwt_auth_demo.controller;

// 1. Sabse zaroori imports (Inhe check karo)
import com.example.jwt_auth_demo.entity.Crop;
import com.example.jwt_auth_demo.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crops")
public class CropController {

    @Autowired
    private CropRepository cropRepository;

    @PostMapping("/add")
    public Crop addCrop(@RequestBody Crop crop, Authentication auth) {
        // auth.getName() se logged-in kisan ka naam save hoga
        crop.setFarmerName(auth.getName());
        return cropRepository.save(crop);
    }

    @GetMapping("/all")
    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCrop(@PathVariable Long id, Authentication auth) {
        return cropRepository.findById(id).map(crop -> {
            // Check karo ki wahi kisan delete kar raha hai jisne add kiya tha
            if (crop.getFarmerName().equals(auth.getName())) {
                cropRepository.delete(crop);
                return ResponseEntity.ok(Map.of("message", "Crop deleted successfully"));
            }
            return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
        }).orElse(ResponseEntity.notFound().build());
    }
}