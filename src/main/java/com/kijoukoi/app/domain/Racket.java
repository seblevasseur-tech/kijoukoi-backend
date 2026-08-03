package com.kijoukoi.app.domain;

import jakarta.persistence.*;

@Embeddable
public class Racket {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blade_id")
    private Blade blade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forehand_rubber_id")
    private Rubber forehandRubber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backhand_rubber_id")
    private Rubber backhandRubber;

    public Racket() {
    }

    public Racket(Blade blade, Rubber forehandRubber, Rubber backhandRubber) {
        this.blade = blade;
        this.forehandRubber = forehandRubber;
        this.backhandRubber = backhandRubber;
    }

    // Getters and Setters
    public Blade getBlade() { return blade; }
    public void setBlade(Blade blade) { this.blade = blade; }

    public Rubber getForehandRubber() { return forehandRubber; }
    public void setForehandRubber(Rubber forehandRubber) { this.forehandRubber = forehandRubber; }

    public Rubber getBackhandRubber() { return backhandRubber; }
    public void setBackhandRubber(Rubber backhandRubber) { this.backhandRubber = backhandRubber; }
}
