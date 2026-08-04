package com.kijoukoi.app.domain.dto;

public class BrandStatDTO {
    private String brandName;
    private Long count;

    public BrandStatDTO() {
    }

    public BrandStatDTO(String brandName, Long count) {
        this.brandName = brandName;
        this.count = count;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
