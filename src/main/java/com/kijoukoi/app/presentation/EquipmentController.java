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
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

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

    private ResponseEntity<byte[]> parseAndReturnImage(String dataUri) {
        if (dataUri == null || dataUri.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            String[] parts = dataUri.split(",");
            String base64String = parts.length > 1 ? parts[1] : parts[0];
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, org.springframework.http.HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/blades/{id}/image")
    public ResponseEntity<byte[]> getBladeImage(@PathVariable Long id) {
        Optional<Blade> blade = bladeRepository.findById(id);
        if (blade.isPresent() && blade.get().getImage() != null) {
            return parseAndReturnImage(blade.get().getImage());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/rubbers/{id}/image")
    public ResponseEntity<byte[]> getRubberImage(@PathVariable Long id) {
        Optional<Rubber> rubber = rubberRepository.findById(id);
        if (rubber.isPresent() && rubber.get().getImage() != null) {
            return parseAndReturnImage(rubber.get().getImage());
        }
        return ResponseEntity.notFound().build();
    }
}
