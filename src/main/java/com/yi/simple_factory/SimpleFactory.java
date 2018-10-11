package com.yi.simple_factory;

import com.yi.common.Product;

/**
 * 工厂实现
 * @author YI
 * @date 2018-10-11 10:15:48
 */
public class SimpleFactory {
    public Product createProduct(int type) {
        if (type == 1) {
            return new ConcreteProduct1();
        } else if (type == 2) {
            return new ConcreteProduct2();
        }

        return new ConcreteProduct();
    }
}
