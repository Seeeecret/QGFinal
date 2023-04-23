package pojo;

import service.TxtDataManageService;
import constants.PrinterStatus;
/**
 * 存储打印机统计时间的类/对象
 *
 * @author Secret
 * @date 2023/04/23
 */
public class PrinterStatistic {
    private long onTime;
    private long printTime;
    private long idleTime;
    private long exceptionTime;

    public PrinterStatistic() {
        onTime = 0;
        printTime = 0;
        idleTime = 0;
        exceptionTime = 0;
    }
    public void analyzeToStringMessage(String txtData){
        boolean onTimeFlag = false;
        boolean printTimeFlag = false;
        boolean idleTimeFlag = false;
        boolean exceptionTimeFlag = false;
        PrinterTreatedMessage treatedMessage = TxtDataManageService.toPrinterTreatedMessage(txtData);
        switch (treatedMessage.printerStatus){
            case WORK_NOT_STARTED:
                if (!onTimeFlag){
                    onTime ++;
                    onTimeFlag = true;
                }
                break;
            case PRINTING:
                printTime += treatedMessage.getTimestamp().getTime();
                break;
            case IDLE:
                idleTime += treatedMessage.getTimestamp().getTime();
                break;
            default:
                exceptionTime += treatedMessage.getTimestamp().getTime();
                break;
        }

    }
}
