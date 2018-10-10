package com.yi.singleton;

import com.yi.common.Recommend;
import com.yi.common.ThreadSafe;

/**
 * 懒汉式单例模式-双重校验锁-线程安全
 * @author YI
 * @date 2018-10-10 15:11:21
 */
@Recommend
@ThreadSafe
public class DoubleCheckLock {
    private volatile static DoubleCheckLock uniqueInstance;

    private DoubleCheckLock() {
    }

    public static DoubleCheckLock getUniqueInstance() {
        if (uniqueInstance == null) {
            synchronized (DoubleCheckLock.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new DoubleCheckLock();
                }
            }
        }

        return uniqueInstance;
    }
}
