package pojo.po;

import constants.PrinterStatus;
import pojo.bo.PrinterRawMessage;

import java.sql.Timestamp;

/**
 * 打印机的处理过后的消息
 *
 * @author Secret
 * @date 2023/04/22
 */
public class PrinterTreatedMessage {
    public PrinterStatus printerStatus;
/*
 这里的timeStamp是java.sql包下的，与PrinterRawMessage中的long类的timeStamp不同,
 其本身是毫秒单位的本时区的时间戳，针对CRUD操作有相应方法的封装
*/
    public Timestamp timestamp;
    public String paramDescription;

    public Number firstParam;
    public String secondParam;

    /**
     * 定义一个构造方法，接受一个打印机原始信息，返回处理后的信息
     * 使用Stream流的map方法，将params中的每个元素转换为Object类型
     * 最后再利用try...catch确定具体的类型，判断到底是否有额外参数后强转赋值
     *
     * @param printerRawMessage 打印机原始信息
     */
    public PrinterTreatedMessage(PrinterRawMessage printerRawMessage) {
        this.printerStatus = printerRawMessage.getPrinterStatusByStatusValue();
        this.paramDescription = this.printerStatus.getParamsDescription(printerRawMessage);
        this.timestamp = new Timestamp(printerRawMessage.getTimestamp()*1000);
        Object[] objectArray = printerRawMessage.
                getParams().stream().map(param -> {
                    try {
                        return Integer.parseInt(param);
                    } catch (NumberFormatException e) {
                        try {
                            return Double.parseDouble(param);
                        } catch (NumberFormatException e1) {
                            return param;
                        }
                    }
                }).toArray(Object[]::new);

        if (objectArray.length == 1 && objectArray[0] instanceof Number) {
            this.firstParam = (Number) objectArray[0];
        } else if (objectArray.length == 2 && objectArray[0] instanceof Number
                && objectArray[1] instanceof String) {
            this.firstParam = (Number) objectArray[0];
            this.secondParam = (String) objectArray[1];
        }
    }

    public PrinterTreatedMessage() {
    }

    public PrinterStatus getPrinterStatus() {
        return printerStatus;
    }

    public String getParamDescription() {
        return paramDescription;
    }

    public Number getFirstParam() {
        return firstParam;
    }

    public String getSecondParam() {
        return secondParam;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "PrinterTreatedMessage{" +
                "printerStatus=" + printerStatus +
                ", timestamp=" + timestamp.getTime()/1000 +
                ", paramDescription='" + paramDescription + '\'' +
                ", firstParam=" + firstParam +
                ", secondParam='" + secondParam + '\'' +
                '}';
    }
}
