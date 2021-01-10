package com.backend.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.tomcat.jni.Pool;

public class ThreadPoolUtilsDDL {

    private static final int POOL_SIZE = 9;

    private volatile static ExecutorService fixedThreadPool;

    private ThreadPoolUtilsDDL() {
    }

    private static ExecutorService getThreadPool() {
        if (null == fixedThreadPool) {
            synchronized (ThreadPoolUtilsDDL.class) {
                if (null == fixedThreadPool) {
                    fixedThreadPool = Executors.newFixedThreadPool(POOL_SIZE);
                }
            }
        }
        return fixedThreadPool;
    }

}
