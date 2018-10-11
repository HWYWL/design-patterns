package com.yi.factory_method;

import com.yi.common.Product;

/**
 * 工厂方法
 * @author YI
 * @date 2018-10-11 10:35:41
 */
public abstract class Factory {
    /**
     * 抽象方法
     * @return
     */
    abstract public Product factoryMethod();

    public void doSomething() {
        Product product = factoryMethod();
        // do something with the product
    }
}
