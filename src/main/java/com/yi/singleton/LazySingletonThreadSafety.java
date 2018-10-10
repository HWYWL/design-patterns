package com.yi.singleton;

import com.yi.common.NotRecommend;
import com.yi.common.ThreadSafe;

/**
 * 懒汉式单例模式-线程安全
 * @author YI
 * @date 2018-10-10 14:59:04
 */
@NotRecommend
@ThreadSafe
public class LazySingletonThreadSafety {
    private static LazySingletonThreadSafety uniqueInstance;

    public LazySingletonThreadSafety() {
    }

    public static synchronized LazySingletonThreadSafety getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new LazySingletonThreadSafety();
        }

        return uniqueInstance;
    }
}
