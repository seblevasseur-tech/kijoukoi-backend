package com.kijoukoi.app.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "player_tag")
public class PlayerTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public PlayerTag() {
    }

    public PlayerTag(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
