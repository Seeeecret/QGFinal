package pojo.po;

import pojo.bo.PrinterStatistic;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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

    public StatisticTime(PrinterStatistic printerStatistic) {
        this(printerStatistic.getOnTime(), printerStatistic.getPrintTime(),
                printerStatistic.getIdleTime(), printerStatistic.getExceptionTime());
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

    public HashMap<String,String> toTimeString(){
        HashMap<String,String> map = new HashMap<>(7);
        map.put("onTime",this.getOnTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        map.put("printTime",this.getPrintTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        map.put("idleTime",this.getIdleTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        map.put("exceptionTime",this.getExceptionTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return map;
    }

}


