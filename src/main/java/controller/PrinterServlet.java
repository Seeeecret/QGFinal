package controller;

import com.alibaba.fastjson.JSONObject;
import pojo.dto.ResponseResultSet;
import pojo.po.Printer;
import service.PrinterService;
import utils.Mapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/printer")
public class PrinterServlet extends BaseServlet {

    /**
     * 根据json里的username和roleId,调用Service层的方法,查找该用户可查看的打印机信息,返回json数据
     *
     * @param request    请求
     * @param response   响应
     * @param jsonObject json对象
     * @throws IOException  ioexception
     * @throws SQLException sqlexception异常
     */
    public void getPrinter(HttpServletRequest request,
                           HttpServletResponse response, JSONObject jsonObject)
            throws IOException, SQLException {
        String method = jsonObject.getString("method");
        int roleId = jsonObject.getInteger("roleId");
        String username = jsonObject.getString("username");

        List<Printer> availablePrinterList = PrinterService.getAvailablePrinter(username, roleId);
        Mapper.writeValue(response.getWriter()
                , ResponseResultSet.success(response)
                        .data("printerList", availablePrinterList));
    }
    public void getPrinterName(HttpServletRequest request,
                           HttpServletResponse response, JSONObject jsonObject)
            throws IOException, SQLException {
        String method = jsonObject.getString("method");
        int printerId = jsonObject.getInteger("printerId");
        String printerName = PrinterService.getPrinterName(printerId);
        Mapper.writeValue(response.getWriter()
                , ResponseResultSet.success(response)
                        .data("printerName", printerName));
    }
}
