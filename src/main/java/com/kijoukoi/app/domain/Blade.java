package com.kijoukoi.app.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "blade")
public class Blade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    private Integer weight;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private BladeType bladeType;

    @Column(columnDefinition = "TEXT")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String image;

    public Blade() {
    }

    public Blade(String name, Brand brand, Integer weight, String image) {
        this.name = name;
        this.brand = brand;
        this.weight = weight;
        this.image = image;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public BladeType getBladeType() { return bladeType; }
    public void setBladeType(BladeType bladeType) { this.bladeType = bladeType; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
