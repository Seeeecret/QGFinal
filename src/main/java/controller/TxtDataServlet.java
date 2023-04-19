package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Secret
 */
@WebServlet("/txtData")
public class TxtDataServlet extends BaseServlet {
    public void receiveData(HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException {
        // 设置响应内容类型
        String method = request.getParameter("method");
        String txtData = request.getParameter("txtData");
        System.out.println(method +"  " + txtData);

    }
}