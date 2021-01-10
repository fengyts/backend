package com.backend.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {

    private static final int POOL_SIZE = 9;

    private volatile static ExecutorService threadPool;

    private ThreadPoolUtils(){}

    private static class ThreadPoolInstance {
        private static final ExecutorService instance = Executors.newFixedThreadPool(POOL_SIZE);
    }

    private static ExecutorService getThreadPool() {
        return ThreadPoolInstance.instance;
    }

}
