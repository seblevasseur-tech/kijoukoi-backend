package com.kijoukoi.app.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    private Integer age;

    private String nationality;

    private Integer ranking;

    private LocalDateTime registrationDate;

    private String gender;

    private LocalDateTime lastRacketUpdateDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Image avatar;

    @Embedded
    private Racket racket;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "player_tags",
        joinColumns = @JoinColumn(name = "player_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private java.util.Set<PlayerTag> tags = new java.util.HashSet<>();

    public Player() {
    }

    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public Integer getRanking() { return ranking; }
    public void setRanking(Integer ranking) { this.ranking = ranking; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDateTime getLastRacketUpdateDate() { return lastRacketUpdateDate; }
    public void setLastRacketUpdateDate(LocalDateTime lastRacketUpdateDate) { this.lastRacketUpdateDate = lastRacketUpdateDate; }

    public Image getAvatar() { return avatar; }
    public void setAvatar(Image avatar) { this.avatar = avatar; }

    public Racket getRacket() { return racket; }
    public void setRacket(Racket racket) { this.racket = racket; }

    public java.util.Set<PlayerTag> getTags() { return tags; }
    public void setTags(java.util.Set<PlayerTag> tags) { this.tags = tags; }
}
