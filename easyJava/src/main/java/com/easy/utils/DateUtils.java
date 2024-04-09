package com.easy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname DateUtils
 * @Description 什么也没有写哦~
 * @Date 2024/4/5 16:41
 * @Created by 憧憬
 */
public class DateUtils {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY2MM2DD = "yyyy/MM/dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String format(Date date, String parent){
        return new SimpleDateFormat(parent).format(date);
    }

    public static Date parse(String date, String pattern) {
        Date parse;
        try {
            parse = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return parse;
    }

}
