package com.easy.bean;

/**
 * @Classname FieldInfo
 * @Description 字段信息
 * @Date 2024/4/3 20:24
 * @Created by 憧憬
 */
public class FieldInfo {
    // 字段名称
    private String FieldName;
    // bean属性名称
    private String propertyName;
    // 字段的mysql类型
    private String sqlType;
    // 字段的java类型
    private String javaType;
    // 字段sql类型的长度
    private Integer typeLength;

    public Integer getTypeLength() {
        return typeLength;
    }

    public void setTypeLength(Integer typeLength) {
        this.typeLength = typeLength;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public Boolean getNull() {
        return isNull;
    }

    public void setNull(Boolean aNull) {
        isNull = aNull;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    // 字段小数点
    private Integer decimalPlaces;
    // 是否为空
    private Boolean isNull;
    // 是否为Key类型
    private String Key;
    // 字段备注
    private String comment;
    // 扩展字段信息
    private String fieldInfos;
    // 字段是否自增长
    private Boolean isAutoIncrement;

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String fieldName) {
        FieldName = fieldName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(Boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "FieldName='" + FieldName + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", sqlType='" + sqlType + '\'' +
                ", javaType='" + javaType + '\'' +
                ", comment='" + comment + '\'' +
                ", isAutoIncrement='" + isAutoIncrement + '\'' +
                '}';
    }
    public String getFieldInfos() {
        return fieldInfos;
    }
    public void setFieldInfos(String fieldInfos) {
        this.fieldInfos = fieldInfos;
    }
    public Boolean getAutoIncrement() {
        return isAutoIncrement;
    }
    public void setAutoIncrement(Boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }
}
