package com.yi.singleton;

import com.yi.common.NotRecommend;
import com.yi.common.NotThreadSafe;

/**
 * 懒汉式单例模式
 * @author YI
 * @date 2018-10-10 14:59:04
 */
@NotRecommend
@NotThreadSafe
public class LazySingleton {
    private static LazySingleton uniqueInstance;

    public LazySingleton() {
    }

    public static LazySingleton getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new LazySingleton();
        }

        return uniqueInstance;
    }
}
