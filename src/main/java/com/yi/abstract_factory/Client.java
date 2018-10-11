package com.yi.abstract_factory;

/**
 * 通过不同的工厂获取不同的对象
 * @author YI
 * @date 2018-10-11 11:03:36
 */
public class Client {
    public static void main(String[] args) {
        AbstractFactory abstractFactory = new ConcreteFactory1();

        AbstractProductA productA = abstractFactory.createProductA();
        AbstractProductB productB = abstractFactory.createProductB();
        productA.helloA("美女");
        productB.helloB("校花");
        // do something with productA and productB
    }
}
