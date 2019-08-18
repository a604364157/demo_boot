package com.jjx.boot.pool;

import com.jjx.boot.util.YamlUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单例线程池
 *
 * @author Administrator
 */
public class ThreadPoolSingle {

    /**
     * 线程池
     */
    private ExecutorService threadPool;
    /**
     * 定时任务管理器
     */
    private ScheduledExecutorService service;
    /**
     * Spring的定时任务管理器
     */
    private ThreadPoolTaskScheduler scheduler;

    /**
     * 防止被外实例化
     */
    private ThreadPoolSingle() {
        int minSize = 1;
        int maxSize = 3;
        int keepTime = 2;
        int queueSize = 5;

        PoolConfig config = YamlUtil.getYaml("common.yml", "pool", PoolConfig.class);
        if (config.getMinPoolSize() != 0) {
            minSize = config.getMinPoolSize();
        }
        if (config.getMaxPoolSize() != 0) {
            maxSize = config.getMaxPoolSize();
        }
        if (config.getKeepTime() != 0) {
            keepTime = config.getKeepTime();
        }
        if (config.getMaxQueueSize() != 0) {
            queueSize = config.getMaxQueueSize();
        }
        /*
         * 实例线程阻塞队列
         */
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>(queueSize);
        /*
         * 最小活动线程
         * 最大活动线程
         * 超出最小活动线程的空闲线程活动时间
         * 时间单位
         * 阻塞队列
         * 线程工厂
         */
        threadPool = new ThreadPoolExecutor(minSize, maxSize, keepTime, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.DiscardOldestPolicy());
        /*
         *定时任务管理器
         */
        service = new ScheduledThreadPoolExecutor(minSize, new ScheduledThreadPoolExecutor.DiscardOldestPolicy());

        scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
    }

    private static ThreadPoolSingle single = null;

    public static ThreadPoolSingle getSingle() {
        if (single == null) {
            single = new ThreadPoolSingle();
        }
        return single;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public ScheduledExecutorService getService() {
        return service;
    }

    public ThreadPoolTaskScheduler getScheduler() {
        return scheduler;
    }

}
