package com.kijoukoi.app.presentation;

import com.kijoukoi.app.domain.Player;
import com.kijoukoi.app.infrastructure.PlayerRepository;
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

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping("/me")
    public ResponseEntity<Player> getMe(org.springframework.security.core.Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        return playerRepository.findByLogin(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        return playerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Simplification : mise à jour complète du joueur (y compris sa raquette)
    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        return playerRepository.findById(id).map(player -> {
            player.setRacket(updatedPlayer.getRacket());
            player.setLastRacketUpdateDate(java.time.LocalDateTime.now());
            
            // Mise à jour des tags
            player.getTags().clear();
            if (updatedPlayer.getTags() != null) {
                player.getTags().addAll(updatedPlayer.getTags());
            }
            
            return ResponseEntity.ok(playerRepository.save(player));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<byte[]> getPlayerAvatar(@PathVariable Long id) {
        return playerRepository.findById(id).map(player -> {
            String dataUri = player.getAvatar();
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
        }).orElse(ResponseEntity.notFound().build());
    }
}
