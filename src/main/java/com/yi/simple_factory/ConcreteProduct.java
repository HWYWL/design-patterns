package com.yi.simple_factory;

/**
 * 接口实现
 * @author YI
 * @date 2018-10-11 10:11:37
 */
public class ConcreteProduct implements Product {
    @Override
    public void hello() {
        System.out.println("Hello Java");
    }
}
