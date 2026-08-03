package com.kijoukoi.app.presentation;

import com.kijoukoi.app.domain.Blade;
import com.kijoukoi.app.domain.Brand;
import com.kijoukoi.app.domain.Rubber;
import com.kijoukoi.app.infrastructure.BladeRepository;
import com.kijoukoi.app.infrastructure.BrandRepository;
import com.kijoukoi.app.infrastructure.RubberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final BladeRepository bladeRepository;
    private final RubberRepository rubberRepository;
    private final BrandRepository brandRepository;

    public AdminController(BladeRepository bladeRepository, RubberRepository rubberRepository, BrandRepository brandRepository) {
        this.bladeRepository = bladeRepository;
        this.rubberRepository = rubberRepository;
        this.brandRepository = brandRepository;
    }

    @PostMapping("/brands")
    public Brand createBrand(@RequestBody Brand brand) {
        return brandRepository.save(brand);
    }

    @PostMapping("/blades")
    public ResponseEntity<Blade> createBlade(@RequestBody Blade blade) {
        if (blade.getBrand() == null || blade.getBrand().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return brandRepository.findById(blade.getBrand().getId()).map(brand -> {
            blade.setBrand(brand);
            return ResponseEntity.ok(bladeRepository.save(blade));
        }).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/rubbers")
    public ResponseEntity<Rubber> createRubber(@RequestBody Rubber rubber) {
        if (rubber.getBrand() == null || rubber.getBrand().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return brandRepository.findById(rubber.getBrand().getId()).map(brand -> {
            rubber.setBrand(brand);
            return ResponseEntity.ok(rubberRepository.save(rubber));
        }).orElse(ResponseEntity.badRequest().build());
    }
}
