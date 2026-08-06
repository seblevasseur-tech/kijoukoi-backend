package com.kijoukoi.app.domain.dto;

public class CreateBladeDTO {
    private String name;
    private Long brandId;
    private Integer weight;
    private Long typeId;
    private String imageBase64;

    public CreateBladeDTO() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
