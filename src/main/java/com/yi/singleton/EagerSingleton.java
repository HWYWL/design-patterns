package com.yi.singleton;

import com.yi.common.NotRecommend;
import com.yi.common.ThreadSafe;

/**
 * 饿汉式单例模式
 * @author YI
 * @date 2018-10-10 15:04:18
 */
@ThreadSafe
@NotRecommend
public class EagerSingleton {
    private static EagerSingleton uniqueInstance = new EagerSingleton();
}
