package com.kijoukoi.app.domain.dto;

public class FilterDTO {
    private String field;
    private String operator; // "EQ", "GTE", "LTE", "LIKE", etc.
    private Object value;

    public FilterDTO() {}

    public FilterDTO(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
}
