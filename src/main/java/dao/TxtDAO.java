package dao;

import com.alibaba.fastjson.JSON;
import pojo.po.PrinterTreatedMessage;
import pojo.po.StatisticTime;
import utils.CRUDUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * 持久层，用于从数据库中读取数据和写入处理后的数据到数据库中
 *
 * @author Secret
 * @date 2023/04/22
 */
public class TxtDAO {
    public static void insertTxtData(PrinterTreatedMessage printerTreatedMessage, HashMap<String, Object> paramsHashMap, int printerID) throws SQLException {
        CRUDUtil.executeCommonInsert("insert into printer_message values (?,?,?,?)", printerID, printerTreatedMessage.getTimestamp()
                , printerTreatedMessage.getPrinterStatus().getStatusValue(), JSON.toJSONString(paramsHashMap));
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
        CRUDUtil.ResultSetWrapper statisticTimeResultSetWrapper = CRUDUtil.executeSpecialQuery("statistic_time", "printer_statistic", "where printer_id = " + printerID);
        ResultSet statisticTimeSet = statisticTimeResultSetWrapper.getResultSet();
        while (statisticTimeSet.next()) {
            LocalDate statisticDataDate = statisticTimeSet.getTimestamp("statistic_time").toLocalDateTime().toLocalDate();
            if (statisticDataDate.isEqual(txtDataTimestamp.toLocalDateTime().toLocalDate())) {
                updateStatisticData(statisticTime, txtDataTimestamp, printerID);
                statisticTimeResultSetWrapper.close();
                return;
            }
        }
        CRUDUtil.executeCommonInsert("insert into printer_statistic values (?,?,?,?,?,?)"
                , printerID, txtDataTimestamp
                , statisticTime.getOnTime(), statisticTime.getPrintTime()
                , statisticTime.getIdleTime(), statisticTime.getExceptionTime());
        statisticTimeResultSetWrapper.close();
        return;
    }

    public static void updateStatisticData(StatisticTime statisticTime, Timestamp txtDataTimestamp, int printerID) throws SQLException {
        CRUDUtil.executeCommonUpdate("updateInfoOnly printer_statistic set statistic_time = ? , daily_on_time = ? , daily_print_time = ? " + ", daily_idle_time = ? , daily_exception_time = ? where printer_id = ?"
                , txtDataTimestamp, statisticTime.getOnTime(), statisticTime.getPrintTime()
                , statisticTime.getIdleTime(), statisticTime.getExceptionTime(), printerID);
return;
    }
}