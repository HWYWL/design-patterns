package com.yi.factory_method;

import com.yi.common.Product;

/**
 * 实现工厂抽象方法
 * @author YI
 * @date 2018-10-11 10:42:21
 */
public class ConcreteFactory extends Factory {
    @Override
    public Product factoryMethod() {
        return () -> System.out.println("我是工厂生产的产品，大家可以叫我啦啦♪(^∇^*)");
    }
}
