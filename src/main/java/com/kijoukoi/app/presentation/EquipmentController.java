package com.kijoukoi.app.presentation;

import com.kijoukoi.app.domain.Blade;
import com.kijoukoi.app.domain.BladeType;
import com.kijoukoi.app.domain.Brand;
import com.kijoukoi.app.domain.Rubber;
import com.kijoukoi.app.domain.RubberType;
import com.kijoukoi.app.infrastructure.BladeRepository;
import com.kijoukoi.app.infrastructure.BladeTypeRepository;
import com.kijoukoi.app.infrastructure.BrandRepository;
import com.kijoukoi.app.infrastructure.RubberRepository;
import com.kijoukoi.app.infrastructure.RubberTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@CrossOrigin(origins = "*")
public class EquipmentController {

    private final BladeRepository bladeRepository;
    private final RubberRepository rubberRepository;
    private final BrandRepository brandRepository;
    private final BladeTypeRepository bladeTypeRepository;
    private final RubberTypeRepository rubberTypeRepository;

    public EquipmentController(BladeRepository bladeRepository, 
                               RubberRepository rubberRepository, 
                               BrandRepository brandRepository,
                               BladeTypeRepository bladeTypeRepository,
                               RubberTypeRepository rubberTypeRepository) {
        this.bladeRepository = bladeRepository;
        this.rubberRepository = rubberRepository;
        this.brandRepository = brandRepository;
        this.bladeTypeRepository = bladeTypeRepository;
        this.rubberTypeRepository = rubberTypeRepository;
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

    @GetMapping("/blade-types")
    public List<BladeType> getAllBladeTypes() {
        return bladeTypeRepository.findAll();
    }

    @GetMapping("/rubber-types")
    public List<RubberType> getAllRubberTypes() {
        return rubberTypeRepository.findAll();
    }
}
