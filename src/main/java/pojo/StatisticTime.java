package pojo;

import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 统计四项时间的类,封装了依赖计时器的计时方法,也存储的统计时间数据的值
 *
 * @author Secret
 */
public class StatisticTime {

    private long timeCounter;
    private ScheduledThreadPoolExecutor executorService;
    private TimerTask task;
    private boolean isRunning;

    private ScheduledFuture<?> future;
    public StatisticTime() {
        this(0);
    }

    public StatisticTime(long timeValue) {
        future = null;
        timeCounter = timeValue;
        ThreadFactory threadFactory = new ThreadFactory() {
            private AtomicInteger threadNumber = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {

                Thread t = new Thread(r, "StatisticTimeThread" + threadNumber.getAndIncrement());
                t.setDaemon(false);
                return t;
            }
        };
        this.executorService = new ScheduledThreadPoolExecutor(2, threadFactory);
        task = new TimerTask() {
            @Override
            public void run() {
                timeCounter++;
            }
        };
        isRunning = false;
    }

    public void start() {
        if (!isRunning) {
            // 1000ms执行一次
            future = executorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            if (future != null) {
                future.cancel(true);
            }
//            executorService.shutdown();
            isRunning = false;
        }
    }

    public long getTimeCounter() {
        return timeCounter;
    }



}


