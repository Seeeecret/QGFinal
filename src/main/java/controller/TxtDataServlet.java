package controller;

import com.alibaba.fastjson.JSONObject;
import dao.TxtWatcherThread;
import pojo.bo.PrinterRawMessage;
import pojo.bo.PrinterStatistic;
import pojo.dto.ResponseResultSet;
import service.TxtDataManageService;
import utils.Mapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


/**
 * 服务器上接受请求,处理txt数据的servlet
 *
 * @author Secret
 * @date 2023/04/24
 */
@WebServlet("/txtData")
public class TxtDataServlet extends BaseServlet {
    private static HashMap<Integer,PrinterStatistic> printerStatisticHashMap = new HashMap<>(10);

    public static HashMap<Integer, PrinterStatistic> getPrinterStatisticHashMap() {
        return printerStatisticHashMap;
    }

    public static void setPrinterStatisticHashMap(HashMap<Integer, PrinterStatistic> printerStatisticHashMap) {
        TxtDataServlet.printerStatisticHashMap = printerStatisticHashMap;
    }

    /**
     * 处理txt数据的方法
     *
     * @param request  请求
     * @param response 响应
     * @throws IOException ioexception
     */
    public void txtData(HttpServletRequest request,
                        HttpServletResponse response, JSONObject jsonObject)
            throws IOException, SQLException {

        String method = jsonObject.getString("method");
        String txtData = jsonObject.getString("txtData");
        int printerID = jsonObject.getInteger("printerID");
        PrinterStatistic printerStatistic;
        if(printerStatisticHashMap. containsKey(printerID)){
            printerStatistic = printerStatisticHashMap.get(printerID);
        }else {
            printerStatistic = new PrinterStatistic(0);
            printerStatisticHashMap.put(printerID,printerStatistic);
        }
        PrinterRawMessage printerRawMessage = new PrinterRawMessage(txtData);
        printerStatistic.analyzeTxtData(txtData);
        TxtDataManageService.insertTxtData(printerRawMessage, printerID);
        TxtDataManageService.insertStatisticData(printerStatistic, printerRawMessage, printerID);
        Mapper.writeValue(response.getWriter(), ResponseResultSet.success());

    }

    /**
     * 开始发送txt数据的线程
     *
     * @param request  请求
     * @param response 响应
     * @throws IOException ioexception
     */
    public void txtDataThread(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {

        TxtWatcherThread txtWatcherThread = new TxtWatcherThread();
        txtWatcherThread.start();

//        HashMap<String, Object> jsonMap = new HashMap<>(5);
//        jsonMap.put("code", 200);
//        jsonMap.put("msg", "请求响应成功");
        Mapper.writeValue(response.getWriter(), ResponseResultSet.success());
    }
}