package pojo.po;

import java.time.LocalTime;

/**
 * 统计四项时间的类,封装了依赖计时器的计时方法,也存储的统计时间数据的值
 *
 * @author Secret
 */
public class StatisticTime {

    private LocalTime onTime;
    private LocalTime printTime;
    private LocalTime idleTime;
    private LocalTime exceptionTime;

    public StatisticTime() {
        this(0, 0, 0, 0);
    }

    public StatisticTime(int onTime, int printTime, int idleTime, int exceptionTime) {
        this.onTime = LocalTime.ofSecondOfDay(onTime);
        this.printTime = LocalTime.ofSecondOfDay(printTime);
        this.idleTime = LocalTime.ofSecondOfDay(idleTime);
        this.exceptionTime = LocalTime.ofSecondOfDay(exceptionTime);
    }

    public LocalTime getOnTime() {
        return onTime;
    }

    public LocalTime getPrintTime() {
        return printTime;
    }

    public LocalTime getIdleTime() {
        return idleTime;
    }

    public LocalTime getExceptionTime() {
        return exceptionTime;
    }
}


