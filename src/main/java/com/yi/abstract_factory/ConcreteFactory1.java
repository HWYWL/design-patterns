package com.yi.abstract_factory;

/**
 * 实现工厂一
 * @author YI
 * @date 2018-10-11 11:01:28
 */
public class ConcreteFactory1 extends AbstractFactory {
    @Override
    AbstractProductA createProductA() {
        return new ProductA1();
    }

    @Override
    AbstractProductB createProductB() {
        return new ProductB1();
    }
}
