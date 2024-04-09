package com.easy.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @Classname TypeConversionEnum
 * @Description 什么也没有写哦~
 * @Date 2024/4/3 23:32
 * @Created by 憧憬
 */
public enum TypeConversionEnum {
    SQL_DATE_TIME(new String[]{"datetime", "timestamp"}, "Date", "DateTime类型"),
    SQL_DATE(new String[]{"date"}, "Date", "Date类型"),
    SQL_DECIMAL(new String[]{"decimal", "double", "float"},"BigDecimal", "Double类型"),
    SQL_STRING_TYPE(new String[]{"char", "varchar", "text", "mediumtext", "longtext"}, "String", "String类型"),
    SQL_INTEGER_TYPE(new String[]{"int", "tinyint"}, "Integer", "Integer类型"),
    SQL_LONG_TYPE(new String[]{"bigint"}, "Long", "Long类型");
    ;
    public static final Logger logger = LoggerFactory.getLogger(TypeConversionEnum.class);
    private String[] sqlTypes;
    private String javaType;
    private String description;

    TypeConversionEnum() {
    }

    TypeConversionEnum(String[] sqlTypes, String javaType, String description) {
        this.sqlTypes = sqlTypes;
        this.javaType = javaType;
        this.description = description;
    }

    public String[] getSqlTypes() {
        return sqlTypes;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getDescription() {
        return description;
    }

    public static TypeConversionEnum getJavaTypeBySqlType(String sqlType){
        TypeConversionEnum[] values = TypeConversionEnum.values();
        for(TypeConversionEnum t : values){
            boolean match = Arrays.stream(t.getSqlTypes()).anyMatch(s -> s.equalsIgnoreCase(sqlType));
            if (match){
                return t;
            }
        }
        logger.error("对该{}类型，未找到对应的JAVA类型", sqlType);
        throw new RuntimeException("未知SQL参数类型");
    }
}
