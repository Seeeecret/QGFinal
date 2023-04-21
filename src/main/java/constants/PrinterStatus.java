package constants;

import pojo.PrinterMessage;

/**
 * 打印机状态
 *
 * @author Secret
 * @date 2023/04/21
 */
public enum PrinterStatus {
    // 打印机状态的枚举常量
    WORK_NOT_STARTED(1, "作业未开始", 0),
    WORK_IN_PROGRESS(2, "正在作业", 0),
    WORK_PAUSED_BY_USER(3, "作业由用户暂停", 0),
    WORK_PAUSED_BY_SYSTEM(4, "作业被动暂停", 1),
    WORK_STOPPED_BY_USER(5, "作业由用户停止", 0),
    WORK_STOPPED_BY_SYSTEM(6, "作业被动停止", 1),
    WORK_FINISHED(7, "作业正常结束", 0),
    WORKBENCH_TEMPERATURE(21, "工作台的当前温度", 1),
    WORK_BEGIN_PRINTING(40, "作业进入打印阶段", 1),
    WORK_TIME_REMAINING(41, "作业距离完成还需要的秒数", 1),
    USER_CLOSE_SOFTWARE(46, "用户正常关闭控制软件", 0),
    WORK_NAME_STRING(80, "输出作业名称字符串", 2);

    private final int statusValue;
    private final String shortDescription;
    private final int paramCount;

    private PrinterStatus(int statusValue, String shortDescription, int paramCount) {
        this.statusValue = statusValue;
        this.shortDescription = shortDescription;
        this.paramCount = paramCount;
    }

    /*当s为1，作业未开始，无额外参数，读到这个值可视为用户打开了打印机硬件控制系统并成功登陆。
    当s为2，正在作业，无额外参数，表示用户点击了开始作业按钮，从而进入正在作业的状态（预热，s
    为40表示真正开始打印）
    当s为3，作业由用户暂停，无额外参数，
    当s为4，作业被动暂停，p1是32位的无符号整形，表示被动暂停的原因,
    p1为1表示“当前的作业零件数为零，作业已暂停，请先加件！”
    p1为2表示“刮板复位发生错误，已暂停作业！
    p1为3~18为“氮气压力异常”
    当s为5，作业由用户停止，无额外参数，
    当s为6，作业被动停止，p1为32位的无符号整形，代表被动停止的原因,
    p1为1表示“工作台或成型桶温度过高”
    p1为2表示“急停开关压下”
    p1为3~4为“氮气压力异常”
    当s为7，作业正常结束，无额外参数
    当s为21，表示工作台的当前温度，p1为单精度浮点数类型
    当s为40，表示作业进入打印阶段，p1为无符号整数类型，表示进入打印的时间，可以利用该时间，计
    算打印作业开始了多长时间。（这个时间减去当前时间，动态更新前端数据）
    当s为41，表示正在进行的作业距离完成还需要的秒数，p1为无符号整数类型
    当s为46，用户正常关闭控制软件，无额外参数
    当s为80，输出作业名称字符串，两个额外参数，p1是32位无符号整形，表示作业名称字符串的长度
    （字节数），p2为作业名称字符串（GBK编码）*/
    public String getParamsDescription(PrinterMessage printerMessage) {
        double paramIndex = Double.parseDouble(printerMessage.getParams().get(3));
        String paramDescription = "";
        switch (this) {
            case WORK_NOT_STARTED:
            case WORK_IN_PROGRESS:
            case WORK_PAUSED_BY_USER:
            case WORK_STOPPED_BY_USER:
            case WORK_BEGIN_PRINTING:
            case USER_CLOSE_SOFTWARE:
            case WORK_FINISHED:
                break;
            case WORK_PAUSED_BY_SYSTEM: {
                if (paramIndex == 1) {
                    paramDescription = "当前的作业零件数为零，作业已暂停，请先加件!";
                } else if (paramIndex == 2) {
                    paramDescription = "刮板复位发生错误，已暂停作业！";
                } else if (paramIndex >= 3 && paramIndex <= 18) {
                    paramDescription = "氮气压力异常";
                } else {
                    paramDescription = "未知参数问题使作业被动暂停，请检查!";
                }
                break;
            }
            case WORK_STOPPED_BY_SYSTEM: {
                if (paramIndex == 1) {
                    paramDescription = "工作台或成型桶温度过高";
                } else if (paramIndex == 2) {
                    paramDescription = "急停开关压下";
                } else if (paramIndex >= 3 && paramIndex <= 4) {
                    paramDescription = "氮气压力异常";
                } else {
                    paramDescription = "未知参数问题使作业被动停止，请检查!";
                }
                break;
            }
            case WORKBENCH_TEMPERATURE: {
                paramDescription = "温度:" + paramIndex + "°C";
                break;
            }
            case WORK_TIME_REMAINING: {
                paramDescription= "剩余时间:"+ (int) paramIndex + "秒";
                break;
            }
            case WORK_NAME_STRING:
                paramDescription = "作业名称:" + printerMessage.getParams().get(4)+" 名称长度:"+ (int) paramIndex;
            default:
        }
        return paramDescription;
    }

    public static PrinterStatus fromStatusValue(int statusValue) {
        for (PrinterStatus ps : PrinterStatus.values()) {
            if (ps.statusValue == statusValue) {
                return ps;
            }
        }
        return null;
    }

    public int getStatusValue() {
        return statusValue;
    }





    public int getParamCount() {
        return paramCount;
    }


    public String getShortDescription() {
        return shortDescription;
    }
}
