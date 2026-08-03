package com.kijoukoi.app.presentation;

import com.kijoukoi.app.domain.Player;
import com.kijoukoi.app.infrastructure.PlayerRepository;
import com.kijoukoi.app.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final PlayerRepository playerRepository;
    private final JwtUtils jwtUtils;

    public AuthController(PlayerRepository playerRepository, JwtUtils jwtUtils) {
        this.playerRepository = playerRepository;
        this.jwtUtils = jwtUtils;
    }

    public static class LoginRequest {
        public String login;
        public String password;
    }

    public static class JwtResponse {
        public String token;
        public Long id;
        public String login;

        public JwtResponse(String token, Long id, String login) {
            this.token = token;
            this.id = id;
            this.login = login;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Recherche très basique, sans encoder le mot de passe pour le moment
        Optional<Player> playerOpt = playerRepository.findAll().stream()
                .filter(p -> p.getLogin().equals(loginRequest.login) && p.getPassword().equals(loginRequest.password))
                .findFirst();

        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            String jwt = jwtUtils.generateJwtToken(player.getLogin());
            return ResponseEntity.ok(new JwtResponse(jwt, player.getId(), player.getLogin()));
        } else {
            return ResponseEntity.status(401).body("Erreur : Identifiants incorrects");
        }
    }
}
