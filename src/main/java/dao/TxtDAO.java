package dao;

import constants.PrinterStatus;
import pojo.po.PrinterTreatedMessage;
import pojo.po.StatisticTime;
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
     * 经过师兄指点，统计四个时间的逻辑进行了修改，采用了txt文件中的时间戳↑
     *
     * @param printerID        打印机id
     * @param statisticTime    统计时间
     * @param txtDataTimestamp txt文件中的时间戳
     * @throws SQLException sqlexception异常
     */
    public static void insertStatisticData(StatisticTime statisticTime, Timestamp txtDataTimestamp, int printerID) throws SQLException {
        LocalTime onTime = statisticTime.getOnTime();
        LocalTime printTime = statisticTime.getPrintTime();
        LocalTime idleTime = statisticTime.getIdleTime();
        LocalTime exceptionTime = statisticTime.getExceptionTime();
        CRUDUtil.executeCommonInsert("insert into printer_statistic values (?,?,?,?,?,?)"
                , printerID
                , txtDataTimestamp, onTime, printTime, idleTime, exceptionTime);

    }
}