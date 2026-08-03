package com.kijoukoi.app.presentation;

import com.kijoukoi.app.domain.PlayerTag;
import com.kijoukoi.app.infrastructure.PlayerTagRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
public class TagController {

    private final PlayerTagRepository tagRepository;

    public TagController(PlayerTagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public List<PlayerTag> getAllTags() {
        return tagRepository.findAll();
    }
}
