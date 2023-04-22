package pojo;

import constants.PrinterStatus;

/**
 * 打印机的处理过后的消息
 *
 * @author Secret
 * @date 2023/04/22
 */
public class PrinterTreatedMessage {
    public PrinterStatus printerStatus;
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

    @Override
    public String toString() {
        return "PrinterTreatedMessage{" +
                "printerStatus=" + printerStatus +
                ", paramDescription='" + paramDescription + '\'' +
                ", firstParam=" + firstParam +
                ", secondParam='" + secondParam + '\'' +
                '}';
    }
}
