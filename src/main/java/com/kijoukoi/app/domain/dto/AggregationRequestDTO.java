package com.kijoukoi.app.domain.dto;

import java.util.List;

public class AggregationRequestDTO {
    private String groupBy; // e.g., "racket.blade.brand.name"
    private String metric;  // e.g., "COUNT"
    private List<FilterDTO> filters;

    public AggregationRequestDTO() {}

    public AggregationRequestDTO(String groupBy, String metric, List<FilterDTO> filters) {
        this.groupBy = groupBy;
        this.metric = metric;
        this.filters = filters;
    }

    public String getGroupBy() { return groupBy; }
    public void setGroupBy(String groupBy) { this.groupBy = groupBy; }

    public String getMetric() { return metric; }
    public void setMetric(String metric) { this.metric = metric; }

    public List<FilterDTO> getFilters() { return filters; }
    public void setFilters(List<FilterDTO> filters) { this.filters = filters; }
}
