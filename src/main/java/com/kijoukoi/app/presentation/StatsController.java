package com.kijoukoi.app.presentation;

import com.kijoukoi.app.domain.dto.AggregationRequestDTO;
import com.kijoukoi.app.domain.dto.AggregationResultDTO;
import com.kijoukoi.app.infrastructure.DynamicStatsRepository;
import com.kijoukoi.app.domain.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {

    private final DynamicStatsRepository dynamicStatsRepository;

    public StatsController(DynamicStatsRepository dynamicStatsRepository) {
        this.dynamicStatsRepository = dynamicStatsRepository;
    }

    @PostMapping("/aggregate")
    public ResponseEntity<List<AggregationResultDTO>> aggregate(@RequestBody AggregationRequestDTO request) {
        List<AggregationResultDTO> stats = dynamicStatsRepository.aggregate(request);
        return ResponseEntity.ok(stats);
    }


    @PostMapping("/players")
    public ResponseEntity<List<Player>> searchPlayers(@RequestBody AggregationRequestDTO request) {
        List<Player> players = dynamicStatsRepository.searchPlayers(request);
        return ResponseEntity.ok(players);
    }
}