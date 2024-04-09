package com.easy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        long start = System.currentTimeMillis();
        String test = "sku_type";
        System.out.println(formatTableName(test, false, '_'));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    private static String formatTableName(String tableName, Boolean upperCaseFirstLetter,char c){
        String substring = tableName.substring(tableName.indexOf(c));
        int index;
        while((index = substring.indexOf(c)) != -1){
            Character s = substring.charAt(index + 1);
            if(Character.isLowerCase(s)){
                s = Character.toUpperCase(s);
            }
            substring = substring.substring(0, index) + s + substring.substring(index + 2);
        }
        return upperCaseFirstLetter ? substring : Character.toLowerCase(substring.charAt(0)) + substring.substring(1);
    }

    private static String formatTableName2(String tableName, Boolean upperCaseFirstLetter,char c){
        String substring = tableName.substring(tableName.indexOf(c) + 1);
        String[] split = substring.split("_");
        StringBuffer resultBuffer = new StringBuffer();
            resultBuffer.append(upperCaseFirstLetter ? Character.toUpperCase(split[0].charAt(0)) + split[0].substring(1) : Character.toLowerCase(split[0].charAt(0)) + split[0].substring(1));
        for(int i = 1; i < split.length; i++){
            resultBuffer.append(Character.toUpperCase(split[i].charAt(0)) + split[i].substring(1));
        }
        return resultBuffer.toString();
    }
}
