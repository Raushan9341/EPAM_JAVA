package com.example.jwt_auth_demo.controller;

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

    // Farmer crop add karega
    @PostMapping("/add")
    public Crop addCrop(@RequestBody Crop crop, Authentication auth) {
        crop.setFarmerName(auth.getName());
        return cropRepository.save(crop);
    }

    // Trader/Farmer sab crops dekhega
    @GetMapping("/all")
    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    // ✅ NEW: Farmer apni crop delete karega
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteCrop(@PathVariable Long id, Authentication auth) {
        return cropRepository.findById(id).map(crop -> {
            // Check: sirf owner hi delete kar sake
            if (!crop.getFarmerName().equals(auth.getName())) {
                return ResponseEntity.status(403)
                        .<Map<String, String>>body(Map.of("message", "You can only delete your own crops!"));
            }
            cropRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Crop deleted successfully!"));
        }).orElse(ResponseEntity.status(404)
                .<Map<String, String>>body(Map.of("message", "Crop not found!")));
    }
}