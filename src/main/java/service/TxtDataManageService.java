package service;

import constants.PrinterStatus;
import pojo.PrinterRawMessage;
import pojo.PrinterTreatedMessage;

/**
 * 处理txt数据的服务类
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

}
