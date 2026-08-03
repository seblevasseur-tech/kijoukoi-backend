package com.kijoukoi.app.presentation;

import com.kijoukoi.app.domain.Player;
import com.kijoukoi.app.infrastructure.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            player.getTagAssignments().clear();
            if (updatedPlayer.getTagAssignments() != null) {
                updatedPlayer.getTagAssignments().forEach(assignment -> {
                    assignment.setPlayer(player);
                    player.getTagAssignments().add(assignment);
                });
            }
            
            return ResponseEntity.ok(playerRepository.save(player));
        }).orElse(ResponseEntity.notFound().build());
    }
}
