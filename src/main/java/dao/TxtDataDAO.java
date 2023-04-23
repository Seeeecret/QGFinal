package dao;

import constants.PrinterStatus;
import pojo.PrinterTreatedMessage;
import utils.CRUDUtil;

import java.sql.SQLException;

/**
 * 持久层，用于从数据库中读取数据和写入处理后的数据到数据库中
 *
 * @author Secret
 * @date 2023/04/22
 */
public class TxtDataDAO {
    public static void insertTxtData(PrinterTreatedMessage printerTreatedMessage, String original,int printerID) throws SQLException {

//        if (printerTreatedMessage.printerStatus == PrinterStatus.WORKBENCH_TEMPERATURE) {
//            CRUDUtil.executeSpecialInsert("printer_message", "", "("
//                    + printerID + "," + printerTreatedMessage.getTimestamp().getTime() + "," + printerTreatedMessage.firstParam + ",'" + original + "')");
        if (printerTreatedMessage.printerStatus == PrinterStatus.WORKBENCH_TEMPERATURE) {
            CRUDUtil.executeCommonInsert("insert into printer_message values (?,?,?,?)", printerID, printerTreatedMessage.getTimestamp(), printerTreatedMessage.firstParam, original);
        } else {
            CRUDUtil.executeCommonInsert("insert into printer_message (printer_id,message_time,message) values (?,?,?)", printerID, printerTreatedMessage.getTimestamp(), original);
        }
    }
    }
