package com.kijoukoi.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "player_tag_assignment")
public class PlayerTagAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @JsonIgnore // Prevent infinite recursion during serialization
    private Player player;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private PlayerTag tag;

    @Column(nullable = false)
    private Boolean isPositive;

    public PlayerTagAssignment() {
    }

    public PlayerTagAssignment(Player player, PlayerTag tag, Boolean isPositive) {
        this.player = player;
        this.tag = tag;
        this.isPositive = isPositive;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public PlayerTag getTag() { return tag; }
    public void setTag(PlayerTag tag) { this.tag = tag; }

    public Boolean getIsPositive() { return isPositive; }
    public void setIsPositive(Boolean isPositive) { this.isPositive = isPositive; }
}
