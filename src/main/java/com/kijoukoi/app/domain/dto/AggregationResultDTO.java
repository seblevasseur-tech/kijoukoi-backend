package com.kijoukoi.app.domain.dto;

public class AggregationResultDTO {
    private String label;
    private Number value;

    public AggregationResultDTO() {}

    public AggregationResultDTO(String label, Long value) {
        this.label = label;
        this.value = value;
    }

    public AggregationResultDTO(Integer label, Long value) {
        this.label = label != null ? String.valueOf(label) : null;
        this.value = value;
    }

    public AggregationResultDTO(Double label, Long value) {
        this.label = label != null ? String.valueOf(label) : null;
        this.value = value;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Number getValue() { return value; }
    public void setValue(Number value) { this.value = value; }
}
