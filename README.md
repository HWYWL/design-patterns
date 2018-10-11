# design-patterns
<!-- GFM-TOC -->
* [一、概述](#一概述)
* [二、创建型](#二创建型)
    * [1. 单例（Singleton）](#1-单例singleton)
    * [2. 简单工厂（Simple Factory）](#2-简单工厂simple-factory)
    * [3. 工厂方法（Factory Method）](#3-工厂方法factory-method)
    * [4. 抽象工厂（Abstract Factory）](#4-抽象工厂abstract-factory)
    * [5. 生成器（Builder）](#5-生成器builder)
    * [6. 原型模式（Prototype）](#6-原型模式prototype)
* [三、行为型](#三行为型)
    * [1. 责任链（Chain Of Responsibility）](#1-责任链chain-of-responsibility)
    * [2. 命令（Command）](#2-命令command)
    * [3. 解释器（Interpreter）](#3-解释器interpreter)
    * [4. 迭代器（Iterator）](#4-迭代器iterator)
    * [5. 中介者（Mediator）](#5-中介者mediator)
    * [6. 备忘录（Memento）](#6-备忘录memento)
    * [7. 观察者（Observer）](#7-观察者observer)
    * [8. 状态（State）](#8-状态state)
    * [9. 策略（Strategy）](#9-策略strategy)
    * [10. 模板方法（Template Method）](#10-模板方法template-method)
    * [11. 访问者（Visitor）](#11-访问者visitor)
    * [12. 空对象（Null）](#12-空对象null)
* [四、结构型](#四结构型)
    * [1. 适配器（Adapter）](#1-适配器adapter)
    * [2. 桥接（Bridge）](#2-桥接bridge)
    * [3. 组合（Composite）](#3-组合composite)
    * [4. 装饰（Decorator）](#4-装饰decorator)
    * [5. 外观（Facade）](#5-外观facade)
    * [6. 享元（Flyweight）](#6-享元flyweight)
    * [7. 代理（Proxy）](#7-代理proxy)
* [参考资料](#参考资料)
<!-- GFM-TOC -->


# 一、概述

设计模式是解决问题的方案，学习现有的设计模式可以做到经验复用。

拥有设计模式词汇，在沟通时就能用更少的词汇来讨论，并且不需要了解底层细节。

[源码](https://github.com/HWYWL/design-patterns)
## 1.单例（Singleton）

### Intent

确保一个类只有一个实例，并提供该实例的全局访问点。

### Class Diagram

使用一个私有构造函数、一个私有静态变量以及一个公有静态函数来实现。

私有构造函数保证了不能通过构造函数来创建对象实例，只能通过公有静态函数返回唯一的私有静态变量。

![](https://i.imgur.com/zJiI9m5.jpg)

### Implementation

#### Ⅰ 懒汉式-线程不安全

以下实现中，私有静态变量 uniqueInstance 被延迟实例化，这样做的好处是，如果没有用到该类，那么就不会实例化 uniqueInstance，从而节约资源。

这个实现在多线程环境下是不安全的，如果多个线程能够同时进入 `if (uniqueInstance == null)` ，并且此时 uniqueInstance 为 null，那么会有多个线程执行 `uniqueInstance = new Singleton();` 语句，这将导致实例化多次 uniqueInstance。

```java
public class Singleton {

    private static Singleton uniqueInstance;

    private Singleton() {
    }

    public static Singleton getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Singleton();
        }
        return uniqueInstance;
    }
}
```

#### Ⅱ 饿汉式-线程安全

线程不安全问题主要是由于 uniqueInstance 被实例化多次，采取直接实例化 uniqueInstance 的方式就不会产生线程不安全问题。

但是直接实例化的方式也丢失了延迟实例化带来的节约资源的好处。

```java
private static Singleton uniqueInstance = new Singleton();
```

#### Ⅲ 懒汉式-线程安全

只需要对 getUniqueInstance() 方法加锁，那么在一个时间点只能有一个线程能够进入该方法，从而避免了实例化多次 uniqueInstance。

但是当一个线程进入该方法之后，其它试图进入该方法的线程都必须等待，即使 uniqueInstance 已经被实例化了。这会让线程阻塞时间过长，因此该方法有性能问题，不推荐使用。

```java
public static synchronized Singleton getUniqueInstance() {
    if (uniqueInstance == null) {
        uniqueInstance = new Singleton();
    }
    return uniqueInstance;
}
```

#### Ⅳ 双重校验锁-线程安全

uniqueInstance 只需要被实例化一次，之后就可以直接使用了。加锁操作只需要对实例化那部分的代码进行，只有当 uniqueInstance 没有被实例化时，才需要进行加锁。

双重校验锁先判断 uniqueInstance 是否已经被实例化，如果没有被实例化，那么才对实例化语句进行加锁。

```java
public class Singleton {

    private volatile static Singleton uniqueInstance;

    private Singleton() {
    }

    public static Singleton getUniqueInstance() {
        if (uniqueInstance == null) {
            synchronized (Singleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }
}
```

考虑下面的实现，也就是只使用了一个 if 语句。在 uniqueInstance == null 的情况下，如果两个线程都执行了 if 语句，那么两个线程都会进入 if 语句块内。虽然在 if 语句块内有加锁操作，但是两个线程都会执行 `uniqueInstance = new Singleton();` 这条语句，只是先后的问题，那么就会进行两次实例化。因此必须使用双重校验锁，也就是需要使用两个 if 语句。

```java
if (uniqueInstance == null) {
    synchronized (Singleton.class) {
        uniqueInstance = new Singleton();
    }
}
```

uniqueInstance 采用 volatile 关键字修饰也是很有必要的， `uniqueInstance = new Singleton();` 这段代码其实是分为三步执行：

1. 为 uniqueInstance 分配内存空间
2. 初始化 uniqueInstance
3. 将 uniqueInstance 指向分配的内存地址

但是由于 JVM 具有指令重排的特性，执行顺序有可能变成 1>3>2。指令重排在单线程环境下不会出先问题，但是在多线程环境下会导致一个线程获得还没有初始化的实例。例如，线程 T<sub>1</sub> 执行了 1 和 3，此时 T<sub>2</sub> 调用 getUniqueInstance() 后发现 uniqueInstance 不为空，因此返回 uniqueInstance，但此时 uniqueInstance 还未被初始化。

使用 volatile 可以禁止 JVM 的指令重排，保证在多线程环境下也能正常运行。

#### Ⅴ 静态内部类实现

当 Singleton 类加载时，静态内部类 SingletonHolder 没有被加载进内存。只有当调用 `getUniqueInstance()` 方法从而触发 `SingletonHolder.INSTANCE` 时 SingletonHolder 才会被加载，此时初始化 INSTANCE 实例，并且 JVM 能确保 INSTANCE 只被实例化一次。

这种方式不仅具有延迟初始化的好处，而且由 JVM 提供了对线程安全的支持。

```java
public class Singleton {

    private Singleton() {
    }

    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getUniqueInstance() {
        return SingletonHolder.INSTANCE;
    }
}
```

#### Ⅵ 枚举实现

```java
public enum Singleton {

    INSTANCE;

    private String objName;


    public String getObjName() {
        return objName;
    }


    public void setObjName(String objName) {
        this.objName = objName;
    }


    public static void main(String[] args) {

        // 单例测试
        Singleton firstSingleton = Singleton.INSTANCE;
        firstSingleton.setObjName("firstName");
        System.out.println(firstSingleton.getObjName());
        Singleton secondSingleton = Singleton.INSTANCE;
        secondSingleton.setObjName("secondName");
        System.out.println(firstSingleton.getObjName());
        System.out.println(secondSingleton.getObjName());

        // 反射获取实例测试
        try {
            Singleton[] enumConstants = Singleton.class.getEnumConstants();
            for (Singleton enumConstant : enumConstants) {
                System.out.println(enumConstant.getObjName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```html
firstName
secondName
secondName
secondName
```

该实现在多次序列化再进行反序列化之后，不会得到多个实例。而其它实现需要使用 transient 修饰所有字段，并且实现序列化和反序列化的方法。

该实现可以防止反射攻击。在其它实现中，通过 setAccessible() 方法可以将私有构造函数的访问级别设置为 public，然后调用构造函数从而实例化对象，如果要防止这种攻击，需要在构造函数中添加防止多次实例化的代码。该实现是由 JVM 保证只会实例化一次，因此不会出现上述的反射攻击。

### Examples

- Logger Classes
- Configuration Classes
- Accesing resources in shared mode
- Factories implemented as Singletons

### JDK

- [java.lang.Runtime#getRuntime()](http://docs.oracle.com/javase/8/docs/api/java/lang/Runtime.html#getRuntime%28%29)
- [java.awt.Desktop#getDesktop()](http://docs.oracle.com/javase/8/docs/api/java/awt/Desktop.html#getDesktop--)
- [java.lang.System#getSecurityManager()](http://docs.oracle.com/javase/8/docs/api/java/lang/System.html#getSecurityManager--)


## 2. 简单工厂（Simple Factory）

### Intent

在创建一个对象时不向客户暴露内部细节，并提供一个创建对象的通用接口。

### Class Diagram

简单工厂把实例化的操作单独放到一个类中，这个类就成为简单工厂类，让简单工厂类来决定应该用哪个具体子类来实例化。

这样做能把客户类和具体子类的实现解耦，客户类不再需要知道有哪些子类以及应当实例化哪个子类。客户类往往有多个，如果不使用简单工厂，那么所有的客户类都要知道所有子类的细节。而且一旦子类发生改变，例如增加子类，那么所有的客户类都要进行修改。

![](https://i.imgur.com/chP5OPu.jpg)

### Implementation

```java
public interface Product {
    void hello();
}
```

```java
public class ConcreteProduct implements Product {
    @Override
    public void hello() {
        System.out.println("Hello Java");
    }
}
```

```java
public class ConcreteProduct1 implements Product {
    @Override
    public void hello() {
        System.out.println("Hello Python");
    }
}
```

```java
public class ConcreteProduct2 implements Product {
    @Override
    public void hello() {
        System.out.println("Hello Go");
    }
}
```

以下的 Client 类包含了实例化的代码，这是一种错误的实现。如果在客户类中存在这种实例化代码，就需要考虑将代码放到简单工厂中。

```java
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
```

以下的 SimpleFactory 是简单工厂实现，它被所有需要进行实例化的客户类调用。

```java
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
```

```java
public class Client2 {
    public static void main(String[] args) {
        SimpleFactory simpleFactory = new SimpleFactory();

        Product product = simpleFactory.createProduct(1);
        product.hello();
    }
}
```

## 3. 工厂方法（Factory Method）

### Intent

定义了一个创建对象的接口，但由子类决定要实例化哪个类。工厂方法把实例化操作推迟到子类。

### Class Diagram

在简单工厂中，创建对象的是另一个类，而在工厂方法中，是由子类来创建对象。

下图中，Factory 有一个 doSomething() 方法，这个方法需要用到一个产品对象，这个产品对象由 factoryMethod() 方法创建。该方法是抽象的，需要由子类去实现。

![](https://i.imgur.com/uvXIKrJ.jpg)

### Implementation

```java
public abstract class Factory {
    abstract public Product factoryMethod();
    public void doSomething() {
        Product product = factoryMethod();
        // do something with the product
    }
}
```

```java
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
```

```java
/**
 * 实现工厂抽象方法
 * @author YI
 * @date 2018-10-11 10:42:21
 */
public class ConcreteFactory1 extends Factory {
    @Override
    public Product factoryMethod() {
        return () -> System.out.println("我是工厂生产的产品，大家可以叫我嘻嘻(#^.^#)");
    }
}
```

```java
/**
 * 实现工厂抽象方法
 * @author YI
 * @date 2018-10-11 10:42:21
 */
public class ConcreteFactory2 extends Factory {
    @Override
    public Product factoryMethod() {
        return () -> System.out.println("我是工厂生产的产品，大家可以叫我哈哈^_^");
    }
}

```

### JDK

- [java.util.Calendar](http://docs.oracle.com/javase/8/docs/api/java/util/Calendar.html#getInstance--)
- [java.util.ResourceBundle](http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html#getBundle-java.lang.String-)
- [java.text.NumberFormat](http://docs.oracle.com/javase/8/docs/api/java/text/NumberFormat.html#getInstance--)
- [java.nio.charset.Charset](http://docs.oracle.com/javase/8/docs/api/java/nio/charset/Charset.html#forName-java.lang.String-)
- [java.net.URLStreamHandlerFactory](http://docs.oracle.com/javase/8/docs/api/java/net/URLStreamHandlerFactory.html#createURLStreamHandler-java.lang.String-)
- [java.util.EnumSet](https://docs.oracle.com/javase/8/docs/api/java/util/EnumSet.html#of-E-)
- [javax.xml.bind.JAXBContext](https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/JAXBContext.html#createMarshaller--)

## 4. 抽象工厂（Abstract Factory）

### Intent

提供一个接口，用于创建  **相关的对象家族** 。

### Class Diagram

抽象工厂模式创建的是对象家族，也就是很多对象而不是一个对象，并且这些对象是相关的，也就是说必须一起创建出来。而工厂方法模式只是用于创建一个对象，这和抽象工厂模式有很大不同。

抽象工厂模式用到了工厂方法模式来创建单一对象，AbstractFactory 中的 createProductA() 和 createProductB() 方法都是让子类来实现，这两个方法单独来看就是在创建一个对象，这符合工厂方法模式的定义。

至于创建对象的家族这一概念是在 Client 体现，Client 要通过 AbstractFactory 同时调用两个方法来创建出两个对象，在这里这两个对象就有很大的相关性，Client 需要同时创建出这两个对象。

从高层次来看，抽象工厂使用了组合，即 Cilent 组合了 AbstractFactory，而工厂方法模式使用了继承。

![](https://i.imgur.com/rHg602N.jpg)

### Implementation

```java
public class AbstractProductA {
    public void helloA(String h){
        System.out.println("HelloA " + h);
    }
}
```

```java
public class AbstractProductB {
    public void helloB(String h){
        System.out.println("HelloB " + h);
    }
}
```

```java
public class ProductA1 extends AbstractProductA {
    public void hiA1(String h){
        System.out.println("HiA1 " + h);
    }
}

```

```java
public class ProductA2 extends AbstractProductA {
    public void hiA2(String h){
        System.out.println("HiA2 " + h);
    }
}

```

```java
public class ProductB1 extends AbstractProductB {
    public void hiB1(String h){
        System.out.println("HiB1 " + h);
    }
}
```

```java
public class ProductB2 extends AbstractProductB {
    public void hiB2(String h){
        System.out.println("HiB2 " + h);
    }
}
```

```java
/**
 * 抽象类工厂
 * @author YI
 * @date 2018-10-11 11:01:28
 */
public abstract class AbstractFactory {
    abstract AbstractProductA createProductA();

    abstract AbstractProductB createProductB();
}
```

```java
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
```

```java
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
```

```java
/**
 * 通过不同的工厂获取不同的对象
 * @author YI
 * @date 2018-10-11 11:03:36
 */
public class Client {
    public static void main(String[] args) {
        AbstractFactory abstractFactory = new ConcreteFactory1();

        AbstractProductA productA = abstractFactory.createProductA();
        AbstractProductB productB = abstractFactory.createProductB();
        productA.helloA("美女");
        productB.helloB("校花");
        // do something with productA and productB
    }
}
```

### JDK

- [javax.xml.parsers.DocumentBuilderFactory](http://docs.oracle.com/javase/8/docs/api/javax/xml/parsers/DocumentBuilderFactory.html)
- [javax.xml.transform.TransformerFactory](http://docs.oracle.com/javase/8/docs/api/javax/xml/transform/TransformerFactory.html#newInstance--)
- [javax.xml.xpath.XPathFactory](http://docs.oracle.com/javase/8/docs/api/javax/xml/xpath/XPathFactory.html#newInstance--)


## 5. 生成器（Builder）

### Intent

封装一个对象的构造过程，并允许按步骤构造。

### Class Diagram

![](https://i.imgur.com/ACUSEcw.jpg)

### Implementation

以下是一个简易的 StringBuilder 实现，参考了 JDK 1.8 源码。

```java
public class AbstractStringBuilder {
    protected char[] value;

    protected int count;

    public AbstractStringBuilder(int capacity) {
        count = 0;
        value = new char[capacity];
    }

    public AbstractStringBuilder append(char c) {
        ensureCapacityInternal(count + 1);
        value[count++] = c;
        return this;
    }

    private void ensureCapacityInternal(int minimumCapacity) {
        // overflow-conscious code
        if (minimumCapacity - value.length > 0)
            expandCapacity(minimumCapacity);
    }

    void expandCapacity(int minimumCapacity) {
        int newCapacity = value.length * 2 + 2;
        if (newCapacity - minimumCapacity < 0)
            newCapacity = minimumCapacity;
        if (newCapacity < 0) {
            if (minimumCapacity < 0) // overflow
                throw new OutOfMemoryError();
            newCapacity = Integer.MAX_VALUE;
        }
        value = Arrays.copyOf(value, newCapacity);
    }
}
```

```java
public class StringBuilder extends AbstractStringBuilder {
    public StringBuilder() {
        super(16);
    }

    @Override
    public String toString() {
        // Create a copy, don't share the array
        return new String(value, 0, count);
    }
}
```

```java
public class Client {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        final int count = 26;
        for (int i = 0; i < count; i++) {
            sb.append((char) ('a' + i));
        }
        System.out.println(sb.toString());
    }
}
```

```html
abcdefghijklmnopqrstuvwxyz
```

### JDK

- [java.lang.StringBuilder](http://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html)
- [java.nio.ByteBuffer](http://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html#put-byte-)
- [java.lang.StringBuffer](http://docs.oracle.com/javase/8/docs/api/java/lang/StringBuffer.html#append-boolean-)
- [java.lang.Appendable](http://docs.oracle.com/javase/8/docs/api/java/lang/Appendable.html)
- [Apache Camel builders](https://github.com/apache/camel/tree/0e195428ee04531be27a0b659005e3aa8d159d23/camel-core/src/main/java/org/apache/camel/builder)


### 感谢
- [CyC2018](https://github.com/CyC2018/CS-Notes/blob/master/notes/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F.md) 大佬的整理


### 问题建议

- 联系我的邮箱：ilovey_hwy@163.com
- 我的博客：http://www.hwy.ac.cn
- GitHub：https://github.com/HWYWL