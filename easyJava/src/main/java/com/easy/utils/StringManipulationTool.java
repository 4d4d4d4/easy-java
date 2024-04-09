package com.easy.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Classname StringUtils
 * @Description 什么也没有写哦~
 * @Date 2024/4/3 21:50
 * @Created by 憧憬
 */
public class StringManipulationTool {

    // 将字符串中的第一个字母转为大写
    public static String upperCaseFirstLetter(String field){
        if(StringUtils.isEmpty(field)){
            return field;
        }
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    // 将字符串中的第一个字母转为小写
    public static String lowerCaseFirstLetter(String field){
        if(StringUtils.isEmpty(field)){
            return field;
        }
        return field.substring(0, 1).toLowerCase() + field.substring(1);
    }
}
