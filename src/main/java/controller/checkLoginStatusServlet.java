package controller;

import org.json.JSONObject;
import pojo.dto.ResponseResultSet;
import utils.JwtUtil;
import utils.Mapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/checkLoginStatus")
public class checkLoginStatusServlet extends BaseServlet{

    public void checkLoginStatus(HttpServletRequest request,
                                 HttpServletResponse response, JSONObject jsonObject)
            throws IOException, SQLException {
        String token = JwtUtil.getToken(request);
        boolean isLogin = JwtUtil.validateToken(token);
        ResponseResultSet checkLoginStatusResultSet = null;
        if (isLogin) {
            checkLoginStatusResultSet = ResponseResultSet.success(response);
        } else {
            checkLoginStatusResultSet = ResponseResultSet.fail(response);
        }
        Mapper.writeValue(response.getWriter(), checkLoginStatusResultSet);
    }
}
