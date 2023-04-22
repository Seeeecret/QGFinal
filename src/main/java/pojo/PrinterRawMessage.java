package pojo;
import constants.PrinterStatus;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Secret
 */
public class PrinterRawMessage {
    private long timestamp;
    private int statusValue;
    private LinkedList<String> params;
    private String original;

    /**
     * 定义一个构造方法，接受一个字符串参数，并对其进行分割和赋值
     *
     * @param message 消息
     */
    public PrinterRawMessage(String message) {
        this.original = message;
        String[] parts = message.split(":");
        this.timestamp = Long.parseLong(parts[0]);
        this.statusValue = Integer.parseInt(parts[1]);
//        this.params = new LinkedList<>();
        // 将parts数组中从第三个开始的子字符串添加到params列表中
        this.params = Arrays.stream(parts).skip(2).collect(Collectors.toCollection(LinkedList::new));
    }

    public PrinterRawMessage() {
    }

    /**
     * 得到状态的枚举类型
     *
     * @return {@link PrinterStatus}
     */
    public PrinterStatus getPrinterStatusByStatusValue() {
        return PrinterStatus.fromStatusValue(statusValue);
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public int getStatusValue() {
        return this.statusValue;
    }

    public LinkedList<String> getParams() {
        return this.params;
    }

    public String getOriginal() {
        return this.original;
    }

    @Override
    public String toString() {
        return "PrinterRawMessage{" +
                "timestamp=" + timestamp +
                ", statusValue=" + statusValue +
                ", params=" + params +
                ", original='" + original + '\'' +
                '}';
    }
}