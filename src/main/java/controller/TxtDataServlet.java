package controller;

import dao.TxtWatcherThread;
import utils.Mapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Secret
 */
@WebServlet("/txtData")
public class TxtDataServlet extends BaseServlet {
    public void txtData(HttpServletRequest request,
                        HttpServletResponse response)
            throws IOException {
        // 设置响应内容类型
        String method = request.getParameter("method");
        String txtData = request.getParameter("txtData");
        System.out.println(method + "  " + txtData);
    }

    public void txtDataThread(HttpServletRequest request,
                              HttpServletResponse response) throws ServletException, IOException {
        TxtWatcherThread txtWatcherThread = new TxtWatcherThread();
        txtWatcherThread.start();
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("code", 200);
        jsonMap.put("msg", "请求响应成功");
        Mapper.writeValue(response.getWriter(), jsonMap);
//        request.setAttribute("method","txtData");
//        request.getRequestDispatcher("/txtData").forward(request,response);
    }
}