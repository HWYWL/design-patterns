package com.yi.prototype;

/**
 * 原型模式
 * @author YI
 * @date 2018-10-11 11:37:21
 */
public class Client {
    public static void main(String[] args) {
        Prototype prototype = new ConcretePrototype("abc");
        Prototype clone = prototype.myClone();

        System.out.println(clone.toString());
    }
}
