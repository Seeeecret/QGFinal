package controller;

import com.alibaba.fastjson.JSONObject;
import dao.TxtWatcherThread;
import pojo.PrinterRawMessage;
import pojo.PrinterStatistic;
import service.TxtDataManageService;
import utils.Mapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


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
            printerStatistic = new PrinterStatistic(printerID);
            printerStatisticHashMap.put(printerID,printerStatistic);
        }
        PrinterRawMessage printerRawMessage = new PrinterRawMessage(txtData);
//        TxtDataManageService.insertTxtData(txtData, printerID);
        TxtDataManageService.insertTxtData(printerRawMessage, printerID);
        printerStatistic.analyzeTxtData(txtData);
        TxtDataManageService.insertStatisticData(printerStatistic);
        HashMap<String, Object> jsonMap = new HashMap<>(5);
        jsonMap.put("msg", "请求响应成功");
        jsonMap.put("code", 200);
        Mapper.writeValue(response.getWriter(), jsonMap);

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

        HashMap<String, Object> jsonMap = new HashMap<>(5);
        jsonMap.put("code", 200);
        jsonMap.put("msg", "请求响应成功");
        Mapper.writeValue(response.getWriter(), jsonMap);
//        request.setAttribute("method","txtData");
//        request.getRequestDispatcher("/txtData").forward(request,response);
    }
}