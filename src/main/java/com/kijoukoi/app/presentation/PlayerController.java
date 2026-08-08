package com.kijoukoi.app.presentation;

import com.kijoukoi.app.application.PlayerApplicationService;
import com.kijoukoi.app.domain.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*") // Autoriser le frontend Angular en dev
public class PlayerController {

    private final PlayerApplicationService playerService;

    public PlayerController(PlayerApplicationService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/me")
    public ResponseEntity<Player> getMe(org.springframework.security.core.Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        return playerService.getPlayerByLogin(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        return playerService.getPlayerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        return playerService.updatePlayer(id, updatedPlayer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<?> getPlayerAvatar(@PathVariable Long id) {
        return playerService.getPlayerAvatarDataUri(id).map(dataUri -> {
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
        }).orElseGet(() -> new ResponseEntity<>(org.springframework.http.HttpStatus.NOT_FOUND));
    }

    @GetMapping("/excel/template")
    public ResponseEntity<byte[]> downloadExcelTemplate() {
        try {
            byte[] fileContent = playerService.generateExcelTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "template_joueurs.xlsx");
            return new ResponseEntity<>(fileContent, headers, org.springframework.http.HttpStatus.OK);
        } catch (Throwable e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(("Erreur serveur: " + e.getMessage()).getBytes());
        }
    }

    @PostMapping("/excel/upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Le fichier est vide."));
        }
        try {
            int count = playerService.importPlayersFromExcel(file);
            return ResponseEntity.ok(Map.of("message", "Importation réussie. " + count + " joueurs ajoutés."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Erreur lors de l'importation: " + e.getMessage()));
        }
    }
}
