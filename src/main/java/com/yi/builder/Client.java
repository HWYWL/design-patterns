package com.yi.builder;

/**
 * 生成器设计模式
 * @author YI
 * @date 2018-10-11 11:29:24
 */
public class Client {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        final int count = 26;

        for (int i = 0; i < count; i++) {
            sb.append((char) ('a' + i));
        }

        System.out.println(sb.toString());
    }
}
