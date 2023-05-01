package service;

import constants.PrinterStatus;
import dao.TxtDAO;
import pojo.bo.PrinterRawMessage;
import pojo.bo.PrinterStatistic;
import pojo.po.PrinterTreatedMessage;
import pojo.po.StatisticTime;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * 专门处理txt数据的服务类,供controller层调用
 *
 * @author Secret
 */
public class TxtDataManageService {
    public TxtDataManageService() {
    }

    /**
     * 将txt数据转换为打印机原始消息
     *
     * @param txtData 从txt文件中读取的数据
     * @return {@link PrinterRawMessage}
     */
    public static PrinterRawMessage toPrinterRawMessage(String txtData) {
        return new PrinterRawMessage(txtData);
    }

    /**
     * 将txt数据转换为打印机处理过后的消息
     *
     * @param txtData 从txt文件中读取的数据
     * @return {@link PrinterTreatedMessage}
     */
    public static PrinterTreatedMessage toPrinterTreatedMessage(String txtData) {
        PrinterRawMessage printerRawMessage = new PrinterRawMessage(txtData);
        return new PrinterTreatedMessage(printerRawMessage);
    }

    /**
     * 通过txt数据得到打印机的状态
     *
     * @param txtData 从txt文件中读取的数据
     * @return {@link PrinterStatus}
     */
    public static PrinterStatus getPrinterStatus(String txtData) {
        PrinterRawMessage printerRawMessage = new PrinterRawMessage(txtData);
        return printerRawMessage.getPrinterStatusByStatusValue();
    }

    /**
     * 通过txt数据得到打印机的额外参数的额外详细描述
     *
     * @param txtData 从txt文件中读取的数据
     * @return {@link String}
     */
    public static String getPrinterParamDescription(String txtData) {
        PrinterRawMessage printerRawMessage = new PrinterRawMessage(txtData);
        return printerRawMessage.getPrinterStatusByStatusValue().getParamsDescription(printerRawMessage);
    }

    /**
     * 通过txt数据得到打印机的额外参数的值
     *
     * @param txtData 从txt文件中读取的数据
     * @return {@link Number[]}
     */
    public static Number[] getPrinterParamValue(String txtData) {
        PrinterRawMessage printerRawMessage = new PrinterRawMessage(txtData);
        return printerRawMessage.getParams().stream().map(param -> {
            try {
                return Integer.parseInt(param);
            } catch (NumberFormatException e) {
                return Double.parseDouble(param);
            }
        }).toArray(Number[]::new);
    }

    /**
     * 插入txt数据至数据库中,DAO层代码的封装
     *
     * @param original  原始语句
     * @param printerId 打印机id
     * @throws SQLException sqlexception异常
     */
    public static void insertTxtData(String original, int printerId) throws SQLException {
        insertTxtData(new PrinterRawMessage(original), printerId);
    }
    public static void insertTxtData(PrinterRawMessage printerRawMessage, int printerId) throws SQLException {
        PrinterTreatedMessage printerTreatedMessage = new PrinterTreatedMessage(printerRawMessage);
        HashMap<String, Object> paramsHashMap = new HashMap<>();
        if(printerTreatedMessage.getFirstParam()!=null) {
            paramsHashMap.put("firstParam",printerTreatedMessage.getFirstParam());
        }
        if(printerTreatedMessage.getSecondParam()!=null) {
            paramsHashMap.put("secondParam",printerTreatedMessage.getSecondParam());
        }
        TxtDAO.insertTxtData(new PrinterTreatedMessage(printerRawMessage), paramsHashMap, printerId);
    }

    public static void insertStatisticData(PrinterStatistic printerStatistic, PrinterRawMessage printerRawMessage, int printerID) throws SQLException {
        StatisticTime statisticTime = new StatisticTime(printerStatistic.getOnTime()
                ,printerStatistic.getPrintTime(),printerStatistic.getIdleTime()
                ,printerStatistic.getExceptionTime());
        Timestamp txtDataTimestamp = new Timestamp(printerRawMessage.getTimestamp()*1000);
        TxtDAO.insertStatisticData(statisticTime,txtDataTimestamp,printerID);
    }

    public static HashMap<String,Object> toWebSocketMap(PrinterTreatedMessage printerTreatedMessage, int printerId){
        HashMap<String, Object> jsonHashMap = new HashMap<>();
        PrinterStatus printerStatus = printerTreatedMessage.getPrinterStatus();
        jsonHashMap.put("printerId",printerId);
        jsonHashMap.put("statusValue",printerStatus.getStatusValue());
        jsonHashMap.put("shortDescription",printerStatus.getShortDescription());
        jsonHashMap.put("paramDescription",printerTreatedMessage.getParamDescription());
        if(printerStatus == PrinterStatus.WORKBENCH_TEMPERATURE){
            jsonHashMap.put("temperature",printerTreatedMessage.getFirstParam());
        }else{
            jsonHashMap.put("temperature",null);
        }
        return jsonHashMap;
    }

}
