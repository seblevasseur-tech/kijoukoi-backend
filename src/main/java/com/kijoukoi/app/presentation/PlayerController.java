package com.kijoukoi.app.presentation;

import com.kijoukoi.app.application.PlayerApplicationService;
import com.kijoukoi.app.domain.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

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
}
