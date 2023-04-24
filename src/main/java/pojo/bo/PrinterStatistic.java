package pojo.bo;

import constants.PrinterStatus;
import pojo.po.PrinterTreatedMessage;
import pojo.po.StatisticTime;
import service.TxtDataManageService;

/**
 * 存储打印机统计时间的外部封装类
 *
 * @author Secret
 * @date 2023/04/23
 */
public class PrinterStatistic {
    private StatisticTime onTime;
    private StatisticTime printTime;
    private StatisticTime idleTime;
    private StatisticTime exceptionTime;

    private boolean onTimeFlag;
    private boolean printTimeFlag;
    private boolean idleTimeFlag;
    private boolean exceptionTimeFlag;


    public PrinterStatistic(long onTimeStartedValue,long printTimeStartedValue,
                            long idleTimeStartedValue,long exceptionTimeStartedValue) {
        onTime = new StatisticTime(onTimeStartedValue);
        printTime = new StatisticTime(printTimeStartedValue);
        idleTime = new StatisticTime(idleTimeStartedValue);
        exceptionTime = new StatisticTime(exceptionTimeStartedValue);
        onTimeFlag = false;
        printTimeFlag = false;
        idleTimeFlag = false;
        exceptionTimeFlag = false;
    }
    public PrinterStatistic(long allStartedValue) {
        this(allStartedValue,allStartedValue,allStartedValue,allStartedValue);
    }

    /**
     * 默认构造函数,默认所有时间都为0,打印机ID为1
     */
    public PrinterStatistic() {
        this(0);
    }
    /**
     * 分析字符串信息,再根据分析得到打印机状态更新统计时间
     *由于不确定打印机的信息是否会有bug,所以在switch后仍加上了数个if判断和flag的设置统计时间
     *
     * @param txtData 从txt文件中读取的数据
     */
    public void analyzeTxtData(String txtData) {
        PrinterTreatedMessage treatedMessage = TxtDataManageService.toPrinterTreatedMessage(txtData);
        analyzeTxtData(treatedMessage.printerStatus);
    }

    public void analyzeTxtData(PrinterRawMessage printerRawMessage){
        analyzeTxtData(printerRawMessage.getOriginal());
    }

    public void analyzeTxtData(PrinterTreatedMessage printerTreatedMessage){
        analyzeTxtData(printerTreatedMessage.printerStatus);
    }

    public void analyzeTxtData(PrinterStatus printerStatus){
        switch (printerStatus) {
            case WORK_NAME_STRING:
                break;
            case WORK_NOT_STARTED:
                if (!onTimeFlag) {
                    onTime.start();
                    onTimeFlag = true;
                }
                break;

            case WORK_PAUSED_BY_USER:
            case WORK_PAUSED_BY_SYSTEM:
            case WORK_STOPPED_BY_USER:
            case WORK_FINISHED:
                if (!idleTimeFlag) {
                    idleTime.start();
                    idleTimeFlag = true;
                }
                if(printTimeFlag){
                    printTime.stop();
                    printTimeFlag = false;
                }
                if (exceptionTimeFlag) {
                    exceptionTime.stop();
                    exceptionTimeFlag = false;
                }
                break;

            case WORK_IN_PROGRESS:
                if (!printTimeFlag) {
                    printTime.start();
                    printTimeFlag = true;
                }
                if(idleTimeFlag){
                    idleTime.stop();
                    idleTimeFlag = false;
                }
                if(exceptionTimeFlag){
                    exceptionTime.stop();
                    exceptionTimeFlag = false;
                }
                break;

            case WORK_STOPPED_BY_SYSTEM:
                if(!exceptionTimeFlag){
                    exceptionTime.start();
                    exceptionTimeFlag = true;
                }
                if (printTimeFlag) {
                    printTime.stop();
                    printTimeFlag = false;
                }
                if (idleTimeFlag) {
                    idleTime.stop();
                    idleTimeFlag = false;
                }
                break;

            case USER_CLOSE_SOFTWARE:
                if (onTimeFlag) {
                    onTime.stop();
                    onTimeFlag = false;
                }
                if (printTimeFlag) {
                    printTime.stop();
                    printTimeFlag = false;
                }
                if (idleTimeFlag) {
                    idleTime.stop();
                    idleTimeFlag = false;
                }
                if (exceptionTimeFlag) {
                    exceptionTime.stop();
                    exceptionTimeFlag = false;
                }
                break;
            default:
                break;
        }
    }

    public StatisticTime getOnTime() {
        return onTime;
    }

    public StatisticTime getPrintTime() {
        return printTime;
    }

    public StatisticTime getIdleTime() {
        return idleTime;
    }

    public StatisticTime getExceptionTime() {
        return exceptionTime;
    }

}
