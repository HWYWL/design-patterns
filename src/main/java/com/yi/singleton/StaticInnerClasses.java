package com.yi.singleton;

import com.yi.common.Recommend;
import com.yi.common.ThreadSafe;

/**
 * 静态内部类实现单例模式
 * @author YI
 * @date 2018-10-10 15:18:16
 */
@Recommend
@ThreadSafe
public class StaticInnerClasses {
    private StaticInnerClasses() {
    }

    private static class SingletonHolder {
        private static final StaticInnerClasses INSTANCE = new StaticInnerClasses();
    }

    public static StaticInnerClasses getUniqueInstance() {
        return SingletonHolder.INSTANCE;
    }
}
