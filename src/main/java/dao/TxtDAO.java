package dao;

import constants.PrinterStatus;
import pojo.PrinterStatistic;
import pojo.PrinterTreatedMessage;
import utils.CRUDUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;

/**
 * 持久层，用于从数据库中读取数据和写入处理后的数据到数据库中
 *
 * @author Secret
 * @date 2023/04/22
 */
public class TxtDAO {
    public static void insertTxtData(PrinterTreatedMessage printerTreatedMessage, String original, int printerID) throws SQLException {

//        if (printerTreatedMessage.printerStatus == PrinterStatus.WORKBENCH_TEMPERATURE) {
//            CRUDUtil.executeSpecialInsert("printer_message", "", "("
//                    + printerID + "," + printerTreatedMessage.getTimestamp().getTime() + "," + printerTreatedMessage.firstParam + ",'" + original + "')");
        if (printerTreatedMessage.printerStatus == PrinterStatus.WORKBENCH_TEMPERATURE) {
            CRUDUtil.executeCommonInsert("insert into printer_message values (?,?,?,?)", printerID, printerTreatedMessage.getTimestamp(), printerTreatedMessage.firstParam, original);
        } else {
            CRUDUtil.executeCommonInsert("insert into printer_message (printer_id,message_time,message) values (?,?,?)", printerID, printerTreatedMessage.getTimestamp(), original);
        }
    }

    /**
     * 插入统计数据,这里会有一个用谁的时间戳的问题，
     * 因为这里的时间是从电脑中读取的，而不是从打印机中读取的，所以可能会有一定的误差
     *
     * @param printerStatistic 打印机统计
     * @param longTimestamp    长时间戳
     * @param printerID        打印机id
     * @throws SQLException sqlexception异常
     */
    public static void insertStatisticData(PrinterStatistic printerStatistic, long longTimestamp, int printerID) throws SQLException {
        Timestamp statisticTime = new Timestamp(longTimestamp*1000);
        LocalTime onTime = LocalTime.ofSecondOfDay(printerStatistic.getOnTime().getTimeCounter());
        LocalTime printTime = LocalTime.ofSecondOfDay(printerStatistic.getPrintTime().getTimeCounter());
        LocalTime idleTime = LocalTime.ofSecondOfDay(printerStatistic.getIdleTime().getTimeCounter());
        LocalTime exceptionTime = LocalTime.ofSecondOfDay(printerStatistic.getExceptionTime().getTimeCounter());
        CRUDUtil.executeCommonInsert("insert into printer_statistic values (?,?,?,?,?,?)"
                , printerID
                , statisticTime, onTime, printTime, idleTime, exceptionTime);

    }
}