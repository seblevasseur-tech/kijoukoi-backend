package com.kijoukoi.app.presentation;

import com.kijoukoi.app.domain.Blade;
import com.kijoukoi.app.domain.Brand;
import com.kijoukoi.app.domain.Rubber;
import com.kijoukoi.app.infrastructure.BladeRepository;
import com.kijoukoi.app.infrastructure.BrandRepository;
import com.kijoukoi.app.infrastructure.RubberRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@CrossOrigin(origins = "*")
public class EquipmentController {

    private final BladeRepository bladeRepository;
    private final RubberRepository rubberRepository;
    private final BrandRepository brandRepository;

    public EquipmentController(BladeRepository bladeRepository, RubberRepository rubberRepository, BrandRepository brandRepository) {
        this.bladeRepository = bladeRepository;
        this.rubberRepository = rubberRepository;
        this.brandRepository = brandRepository;
    }

    @GetMapping("/blades")
    public List<Blade> getAllBlades() {
        return bladeRepository.findAll();
    }

    @GetMapping("/rubbers")
    public List<Rubber> getAllRubbers() {
        return rubberRepository.findAll();
    }

    @GetMapping("/brands")
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}
