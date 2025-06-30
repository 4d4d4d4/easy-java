package com.easyjava.enums;


/**
 * @Classname DateTimePatternEnum
 * @Description 时间格式枚举
 */
public enum DateTimePatternEnum {
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"), YYYY_MM_DD("yyyy-MM-dd");
    private String pattern;

    DateTimePatternEnum() {
    }

    DateTimePatternEnum(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
