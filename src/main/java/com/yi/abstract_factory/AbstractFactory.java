package com.yi.abstract_factory;

/**
 * 抽象类工厂
 * @author YI
 * @date 2018-10-11 11:01:28
 */
public abstract class AbstractFactory {
    abstract AbstractProductA createProductA();

    abstract AbstractProductB createProductB();
}
