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

import static controller.TxtDataServlet.printerBeginTimeHashMap;

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
        if (printerTreatedMessage.getFirstParam() != null) {
            paramsHashMap.put("firstParam", printerTreatedMessage.getFirstParam());
        }
        if (printerTreatedMessage.getSecondParam() != null) {
            paramsHashMap.put("secondParam", printerTreatedMessage.getSecondParam());
        }
        TxtDAO.insertTxtData(new PrinterTreatedMessage(printerRawMessage), paramsHashMap, printerId);
    }

    public static void insertStatisticData(PrinterStatistic printerStatistic, PrinterRawMessage printerRawMessage, int printerID) throws SQLException {
        StatisticTime statisticTime = new StatisticTime(printerStatistic);
        Timestamp txtDataTimestamp = new Timestamp(printerRawMessage.getTimestamp() * 1000);
        TxtDAO.insertStatisticData(statisticTime, txtDataTimestamp, printerID);
    }

    public static HashMap<String, Object> toWebSocketMap(PrinterTreatedMessage printerTreatedMessage, PrinterStatistic printerStatistic, int printerId) {
        HashMap<String, Object> jsonHashMap = new HashMap<>();
        PrinterStatus printerStatus = printerTreatedMessage.getPrinterStatus();
        Integer statusValue = printerStatus.getStatusValue();
        StatisticTime statisticTime = new StatisticTime(printerStatistic);

        jsonHashMap.put("statisticTime", statisticTime.toTimeString());
        jsonHashMap.put("printerId", printerId);
        jsonHashMap.put("statusValue", statusValue);
        jsonHashMap.put("shortDescription", printerStatus.getShortDescription());
        jsonHashMap.put("paramDescription", printerTreatedMessage.getParamDescription());

        if (printerStatus == PrinterStatus.WORK_BEGIN_PRINTING) {
            if (printerBeginTimeHashMap.containsKey(printerId)) {
                printerBeginTimeHashMap.replace(printerId, printerTreatedMessage.getTimestamp());
            } else {
                printerBeginTimeHashMap.put(printerId, printerTreatedMessage.getTimestamp());
            }
        }

        if (printerStatus == PrinterStatus.WORKBENCH_TEMPERATURE) {
            jsonHashMap.put("printerTemperature", printerTreatedMessage.getFirstParam());
        } else {
            jsonHashMap.put("printerTemperature", null);
        }

        if (printerStatus == PrinterStatus.WORK_TIME_REMAINING) {
            if (printerBeginTimeHashMap.containsKey(printerId)) {
                long taskAlreadyCostTime = printerTreatedMessage.getTimestamp().getTime() / 1000 - printerBeginTimeHashMap.get(printerId).getTime() / 1000;
                long taskTotalTime = taskAlreadyCostTime + (printerTreatedMessage.getFirstParam()).longValue();
                double taskAlreadyCostTimePercent = (double) taskAlreadyCostTime / taskTotalTime;
                String taskAlreadyCostTimePercentString = String.format("%.1f", taskAlreadyCostTimePercent * 100);
                jsonHashMap.put("printerProgress", taskAlreadyCostTimePercentString + "%");
            } else {
                jsonHashMap.put("printerProgress", "0%");
            }
        }else if(printerStatus == PrinterStatus.WORK_BEGIN_PRINTING){
            jsonHashMap.put("printerProgress", "0%");
        }else if(printerStatus == PrinterStatus.WORK_FINISHED) {
            jsonHashMap.put("printerProgress", "100.0%");
        }else{
            jsonHashMap.put("printerProgress", null);
        }
        return jsonHashMap;
    }
}
