package com.easy.test;

/**
 * @Classname Main
 * @Description 什么也没有写哦~
 * @Date 2024/4/27 0:12
 * @Created by 憧憬
 */
public class Main {

    private static int[] primeNums = new int[]{17, 19, 29, 31, 37};

    private static final int length = Integer.MAX_VALUE;

    public static void main(String[] args) {
//        String key = "abcd";
//        System.out.println(length);
//        for (int i : primeNums) {
//            // 计算hashcode
//            int hashcode = hash(key, i);
//            // 计算映射在bitset上的位置
//            int bitIndex = hashcode & (length - 1);
//            System.out.println(Integer.toBinaryString(hashcode));
//            System.out.println(Integer.toBinaryString(length - 1));
//            System.out.println("--------------------------------------------------");
//            System.out.println(Integer.toBinaryString(bitIndex));
//            System.out.println("****************************************************");
//        }
        while (true) {
            int min = 0;
            int max = 1;
            int randomInt = (int) (Math.random() * (max - min + 1)) + min;
            System.out.println(randomInt);
        }
    }

    private static int hash(String key, int prime) {
        int h = 0;
        char[] value = key.toCharArray();
        for (char c : value) {
            h = prime * h + c;
        }
        return h;
    }

    public static boolean hasKey(String key) {
        for (int i : primeNums) {
            // 计算hashcode
            int hashcode = hash(key, i);
            // 计算映射在bitset上的位置
            int bitIndex = hashcode & (length - 1);
            // 只要有一个位置对应不上，则返回false
        }
        return true;
    }

}