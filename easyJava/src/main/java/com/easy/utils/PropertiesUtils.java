package com.easy.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname PropertiesUtils
 * @Description 什么也没有写哦~
 * @Date 2024/3/31 23:46
 * @Created by 憧憬
 */
public class PropertiesUtils {
    private static Properties properties = new Properties();
    private static Map<String, String> PROPER_MAP = new ConcurrentHashMap<>();

    static {
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(new InputStreamReader(is, StandardCharsets.UTF_8));

            Iterator<Object> iterator = properties.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                String property = properties.getProperty(key);
                PROPER_MAP.put(key, property);
            }

        }catch (Exception e){
        }finally {
            if(is != null){
                try {
                    is.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static String getString(String key){
        return PROPER_MAP.get(key);
    }
    public static void main(String[] args){
        String string = getString("db.url");
        System.out.println(string);

    }
}
