package com.yi.prototype;

/**
 * 复制原型来创建新对象
 * @author YI
 * @date 2018-10-11 11:37:00
 */
public class ConcretePrototype extends Prototype {

    private String filed;

    public ConcretePrototype(String filed) {
        this.filed = filed;
    }

    @Override
    Prototype myClone() {
        return new ConcretePrototype(filed);
    }

    @Override
    public String toString() {
        return filed;
    }
}
