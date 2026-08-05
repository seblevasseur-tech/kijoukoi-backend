package com.kijoukoi.app.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_uri", columnDefinition = "TEXT", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String dataUri;

    public Image() {
    }

    public Image(String dataUri) {
        this.dataUri = dataUri;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataUri() {
        return dataUri;
    }

    public void setDataUri(String dataUri) {
        this.dataUri = dataUri;
    }
}
