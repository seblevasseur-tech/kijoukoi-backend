package com.kijoukoi.app.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "rubber")
public class Rubber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private RubberType rubberType;

    private Double hardness;

    @Column(columnDefinition = "TEXT")
    private String image;

    public Rubber() {
    }

    public Rubber(String name, Brand brand, String image) {
        this.name = name;
        this.brand = brand;
        this.image = image;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    public RubberType getRubberType() { return rubberType; }
    public void setRubberType(RubberType rubberType) { this.rubberType = rubberType; }

    public Double getHardness() { return hardness; }
    public void setHardness(Double hardness) { this.hardness = hardness; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
