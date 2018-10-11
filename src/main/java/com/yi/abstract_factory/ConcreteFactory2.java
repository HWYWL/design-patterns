package com.yi.abstract_factory;

/**
 * 实现工厂二
 * @author YI
 * @date 2018-10-11 11:01:28
 */
public class ConcreteFactory2 extends AbstractFactory {
    @Override
    AbstractProductA createProductA() {
        return new ProductA2();
    }

    @Override
    AbstractProductB createProductB() {
        return new ProductB2();
    }
}
