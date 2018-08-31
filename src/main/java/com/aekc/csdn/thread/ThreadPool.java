package com.aekc.csdn.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    /** 线程池维护线程的最少数量 */
    private static final int CORE_POOL_SIZE = 2;

    /** 线程池维护线程的最大数量 */
    private static final int MAX_POOL_SIZE = 10;

    /** 线程池维护线程所允许的空闲时间 */
    private static final long KEEP_ALIVE_TIME = 4;

    /** 线程池维护线程所允许的空闲时间的单位 */
    private static final TimeUnit UNIT = TimeUnit.SECONDS;

    /** 线程池所使用的缓冲队列，这里队列大小为3 */
    private static final BlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<Runnable>(3);

    /**
     * 	线程池对拒绝任务的处理策略：
     * 	AbortPolicy为抛出异常；
     * 	CallerRunsPolicy为重试添加当前的任务，他会自动重复调用execute()方法；
     * 	DiscardOldestPolicy为抛弃旧的任务；
     * 	DiscardPolicy为抛弃当前的任务
     */
    private static final ThreadPoolExecutor.AbortPolicy HANDLER = new ThreadPoolExecutor.AbortPolicy();

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, UNIT, WORK_QUEUE, HANDLER);

    /**
     * 加入到线程池中
     * @param runnable 任务
     */
    public static void runInThread(Runnable runnable) {
        threadPool.execute(runnable);
    }
}
