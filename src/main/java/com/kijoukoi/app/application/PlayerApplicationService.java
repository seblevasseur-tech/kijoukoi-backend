package com.kijoukoi.app.application;

import com.kijoukoi.app.domain.Player;
import com.kijoukoi.app.infrastructure.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlayerApplicationService {

    private final PlayerRepository playerRepository;

    public PlayerApplicationService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerByLogin(String login) {
        return playerRepository.findByLogin(login);
    }

    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public Optional<Player> updatePlayer(Long id, Player updatedPlayer) {
        return playerRepository.findById(id).map(player -> {
            player.setRacket(updatedPlayer.getRacket());
            player.setLastRacketUpdateDate(java.time.LocalDateTime.now());
            
            player.getTags().clear();
            if (updatedPlayer.getTags() != null) {
                player.getTags().addAll(updatedPlayer.getTags());
            }
            
            return playerRepository.save(player);
        });
    }

    public Optional<String> getPlayerAvatarDataUri(Long id) {
        return playerRepository.findById(id)
                .map(Player::getAvatar)
                .map(com.kijoukoi.app.domain.Image::getDataUri);
    }
}
