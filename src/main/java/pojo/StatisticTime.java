package pojo;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticTime {

    private long timeCounter;
    private final ScheduledThreadPoolExecutor executorService;
    private final TimerTask task;
    private boolean isRunning;

    public StatisticTime() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private AtomicInteger threadNumber = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "StatisticTimeThread"+threadNumber.getAndIncrement());
            }
        };
        this.timeCounter = 0;
        this.executorService = new ScheduledThreadPoolExecutor(1, threadFactory);
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
            executorService.scheduleAtFixedRate(task, 0, 1000, TimeUnit.MILLISECONDS);
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            executorService.shutdown();
            isRunning = false;
        }
    }


}


