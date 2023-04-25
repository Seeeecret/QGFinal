package pojo.bo;

import constants.PrinterStatus;
import pojo.po.PrinterTreatedMessage;
import service.TxtDataManageService;

/**
 * 存储打印机统计时间的外部封装类
 *
 * @author Secret
 * @date 2023/04/23
 */
public class PrinterStatistic {
    private int onTime;
    private int printTime;
    private int idleTime;
    private int exceptionTime;

    private boolean onTimeFlag;
    private boolean printTimeFlag;
    private boolean idleTimeFlag;
    private boolean exceptionTimeFlag;


    public PrinterStatistic(int onTimeStartedValue, int printTimeStartedValue,
                            int idleTimeStartedValue, int exceptionTimeStartedValue) {
        onTime = onTimeStartedValue;
        printTime = printTimeStartedValue;
        idleTime = idleTimeStartedValue;
        exceptionTime = exceptionTimeStartedValue;
        onTimeFlag = false;
        printTimeFlag = false;
        idleTimeFlag = false;
        exceptionTimeFlag = false;
    }

    public PrinterStatistic(int allStartedValue) {
        this(allStartedValue, allStartedValue, allStartedValue, allStartedValue);
    }

    /**
     * 默认构造函数,默认所有时间都为0,打印机ID为1
     */
    public PrinterStatistic() {
        this(0);
    }

    /**
     * 分析字符串信息,再根据分析得到打印机状态更新统计时间
     * 由于不确定打印机的信息是否会有bug,所以在switch后仍加上了数个if判断和flag的设置统计时间
     *
     * @param txtData 从txt文件中读取的数据
     */
    public void analyzeTxtData(String txtData) {
        PrinterTreatedMessage treatedMessage = TxtDataManageService.toPrinterTreatedMessage(txtData);
        analyzeTxtData(treatedMessage.printerStatus);
    }

    public void analyzeTxtData(PrinterRawMessage printerRawMessage) {
        analyzeTxtData(printerRawMessage.getOriginal());
    }

    public void analyzeTxtData(PrinterTreatedMessage printerTreatedMessage) {
        analyzeTxtData(printerTreatedMessage.printerStatus);
    }

    public void analyzeTxtData(PrinterStatus printerStatus) {
        switch (printerStatus) {
            case WORKBENCH_TEMPERATURE:
            case WORK_BEGIN_PRINTING:
            case WORK_TIME_REMAINING:
            case WORK_NAME_STRING:
                break;
            case WORK_NOT_STARTED:
                if (!onTimeFlag) {
                    onTimeFlag = true;
                }
                break;
            case WORK_PAUSED_BY_USER:
            case WORK_PAUSED_BY_SYSTEM:
            case WORK_STOPPED_BY_USER:
            case WORK_FINISHED:
                if (!idleTimeFlag) {
                    idleTimeFlag = true;
                }
                if (printTimeFlag) {
                    printTimeFlag = false;
                }
                if (exceptionTimeFlag) {
                    exceptionTimeFlag = false;
                }
                break;

            case WORK_IN_PROGRESS:
                if (!printTimeFlag) {
                    printTimeFlag = true;
                }
                if (idleTimeFlag) {
                    idleTimeFlag = false;
                }
                if (exceptionTimeFlag) {
                    exceptionTimeFlag = false;
                }
                break;

            case WORK_STOPPED_BY_SYSTEM:
                if (!exceptionTimeFlag) {
                    exceptionTimeFlag = true;
                }
                if (printTimeFlag) {
                    printTimeFlag = false;
                }
                if (idleTimeFlag) {
                    idleTimeFlag = false;
                }
                break;

            case USER_CLOSE_SOFTWARE:
                if (onTimeFlag) {
                    onTimeFlag = false;
                }
                if (printTimeFlag) {
                    printTimeFlag = false;
                }
                if (idleTimeFlag) {
                    idleTimeFlag = false;
                }
                if (exceptionTimeFlag) {
                    exceptionTimeFlag = false;
                }
                break;
            default:
                break;
        }
        if (onTimeFlag) {
            onTime++;
        }
        if (printTimeFlag) {
            printTime++;
        }
        if (exceptionTimeFlag) {
            exceptionTime++;
        }
        if (idleTimeFlag) {
            idleTime++;
        }
    }

    public int getOnTime() {
        return onTime;
    }

    public int getPrintTime() {
        return printTime;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public int getExceptionTime() {
        return exceptionTime;
    }
}
