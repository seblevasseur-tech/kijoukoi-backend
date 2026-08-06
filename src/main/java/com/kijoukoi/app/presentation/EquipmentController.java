package com.kijoukoi.app.presentation;

import com.kijoukoi.app.application.EquipmentApplicationService;
import com.kijoukoi.app.domain.Blade;
import com.kijoukoi.app.domain.BladeType;
import com.kijoukoi.app.domain.Brand;
import com.kijoukoi.app.domain.Rubber;
import com.kijoukoi.app.domain.RubberType;
import com.kijoukoi.app.domain.dto.CreateBladeDTO;
import com.kijoukoi.app.domain.dto.FilterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@CrossOrigin(origins = "*")
public class EquipmentController {

    private final EquipmentApplicationService equipmentService;

    public EquipmentController(EquipmentApplicationService equipmentService) {
        this.equipmentService = equipmentService;
    }
    
    @PostMapping("/blades")
    public ResponseEntity<Blade> createBlade(@RequestBody CreateBladeDTO dto) {
        Blade blade = equipmentService.createBlade(dto);
        return ResponseEntity.ok(blade);
    }

    @PostMapping("/blades/search")
    public List<Blade> searchBlades(@RequestBody List<FilterDTO> filters) {
        return equipmentService.searchBlades(filters);
    }

    @GetMapping("/blades")
    public List<Blade> getAllBlades() {
        return equipmentService.getAllBlades();
    }

    @PostMapping("/rubbers/search")
    public List<Rubber> searchRubbers(@RequestBody List<FilterDTO> filters) {
        return equipmentService.searchRubbers(filters);
    }

    @GetMapping("/rubbers")
    public List<Rubber> getAllRubbers() {
        return equipmentService.getAllRubbers();
    }

    @GetMapping("/brands")
    public List<Brand> getAllBrands() {
        return equipmentService.getAllBrands();
    }

    @GetMapping("/blade-types")
    public List<BladeType> getAllBladeTypes() {
        return equipmentService.getAllBladeTypes();
    }

    @GetMapping("/rubber-types")
    public List<RubberType> getAllRubberTypes() {
        return equipmentService.getAllRubberTypes();
    }

    private ResponseEntity<?> parseAndReturnImage(String dataUri) {
        if (dataUri == null || dataUri.isEmpty()) {
            return new ResponseEntity<>(org.springframework.http.HttpStatus.NOT_FOUND);
        }
        try {
            String[] parts = dataUri.split(",");
            String base64String = parts.length > 1 ? parts[1] : parts[0];
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, org.springframework.http.HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(org.springframework.http.HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/blades/{id}/image")
    public ResponseEntity<?> getBladeImage(@PathVariable Long id) {
        return equipmentService.getBladeImageDataUri(id)
                .map(this::parseAndReturnImage)
                .orElseGet(() -> new ResponseEntity<>(org.springframework.http.HttpStatus.NOT_FOUND));
    }

    @GetMapping("/rubbers/{id}/image")
    public ResponseEntity<?> getRubberImage(@PathVariable Long id) {
        return equipmentService.getRubberImageDataUri(id)
                .map(this::parseAndReturnImage)
                .orElseGet(() -> new ResponseEntity<>(org.springframework.http.HttpStatus.NOT_FOUND));
    }
}
