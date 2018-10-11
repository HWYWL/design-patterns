package com.yi.simple_factory;

import com.yi.common.Product;
import com.yi.common.Recommend;

/**
 * 错误的实现，需要考虑将代码放到简单工厂中
 * @author YI
 * @date 2018-10-11 10:13:32
 */
@Recommend
public class Client2 {
    public static void main(String[] args) {
        SimpleFactory simpleFactory = new SimpleFactory();

        Product product = simpleFactory.createProduct(1);
        product.hello();
    }
}
