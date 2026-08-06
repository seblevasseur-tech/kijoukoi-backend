package com.kijoukoi.app.application;

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
import com.kijoukoi.app.infrastructure.DynamicEquipmentRepository;
import com.kijoukoi.app.domain.Image;
import com.kijoukoi.app.domain.dto.CreateBladeDTO;
import com.kijoukoi.app.domain.dto.FilterDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EquipmentApplicationService {

    private final BladeRepository bladeRepository;
    private final RubberRepository rubberRepository;
    private final BrandRepository brandRepository;
    private final BladeTypeRepository bladeTypeRepository;
    private final RubberTypeRepository rubberTypeRepository;
    private final DynamicEquipmentRepository dynamicEquipmentRepository;

    public EquipmentApplicationService(BladeRepository bladeRepository,
                                       RubberRepository rubberRepository,
                                       BrandRepository brandRepository,
                                       BladeTypeRepository bladeTypeRepository,
                                       RubberTypeRepository rubberTypeRepository,
                                       DynamicEquipmentRepository dynamicEquipmentRepository) {
        this.bladeRepository = bladeRepository;
        this.rubberRepository = rubberRepository;
        this.brandRepository = brandRepository;
        this.bladeTypeRepository = bladeTypeRepository;
        this.rubberTypeRepository = rubberTypeRepository;
        this.dynamicEquipmentRepository = dynamicEquipmentRepository;
    }

    
    public Blade createBlade(CreateBladeDTO dto) {
        Brand brand = brandRepository.findById(dto.getBrandId())
            .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
        BladeType type = bladeTypeRepository.findById(dto.getTypeId())
            .orElseThrow(() -> new IllegalArgumentException("BladeType not found"));

        Image image = null;
        if (dto.getImageBase64() != null && !dto.getImageBase64().isEmpty()) {
            image = new Image();
            image.setDataUri(dto.getImageBase64());
        }

        Blade blade = new Blade(dto.getName(), brand, dto.getWeight(), image);
        blade.setBladeType(type);
        return bladeRepository.save(blade);
    }

    public List<Blade> searchBlades(List<FilterDTO> filters) {
        return dynamicEquipmentRepository.searchBlades(filters);
    }

    public List<Rubber> searchRubbers(List<FilterDTO> filters) {
        return dynamicEquipmentRepository.searchRubbers(filters);
    }

    public List<Blade> getAllBlades() {
        return bladeRepository.findAll();
    }

    public List<Rubber> getAllRubbers() {
        return rubberRepository.findAll();
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public List<BladeType> getAllBladeTypes() {
        return bladeTypeRepository.findAll();
    }

    public List<RubberType> getAllRubberTypes() {
        return rubberTypeRepository.findAll();
    }

    public Optional<String> getBladeImageDataUri(Long id) {
        return bladeRepository.findById(id)
                .map(Blade::getImage)
                .map(com.kijoukoi.app.domain.Image::getDataUri);
    }

    public Optional<String> getRubberImageDataUri(Long id) {
        return rubberRepository.findById(id)
                .map(Rubber::getImage)
                .map(com.kijoukoi.app.domain.Image::getDataUri);
    }
}
