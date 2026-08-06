package com.kijoukoi.app.domain.dto;

public class CreateRubberDTO {
    private String name;
    private Long brandId;
    private Long typeId;
    private Double hardness;
    private String imageBase64;

    public CreateRubberDTO() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }

    public Double getHardness() { return hardness; }
    public void setHardness(Double hardness) { this.hardness = hardness; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
