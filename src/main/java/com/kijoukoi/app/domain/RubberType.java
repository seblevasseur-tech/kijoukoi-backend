package com.kijoukoi.app.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "rubber_type")
public class RubberType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public RubberType() {}

    public RubberType(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
