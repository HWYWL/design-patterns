package com.yi.simple_factory;

import com.yi.common.NotRecommend;

/**
 * 错误的实现，需要考虑将代码放到简单工厂中
 * @author YI
 * @date 2018-10-11 10:13:32
 */
@NotRecommend
public class Client1 {
    public static void main(String[] args) {
        int type = 1;
        Product product;
        if (type == 1) {
            product = new ConcreteProduct1();
        } else if (type == 2) {
            product = new ConcreteProduct2();
        } else {
            product = new ConcreteProduct();
        }

        product.hello();
    }
}
