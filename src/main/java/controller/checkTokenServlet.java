package controller;

import com.alibaba.fastjson.JSONObject;
import pojo.dto.ResponseResultSet;
import utils.JwtUtil;
import utils.Mapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/checkToken")
public class checkTokenServlet extends BaseServlet{

    public void checkToken(HttpServletRequest request,
                                 HttpServletResponse response, JSONObject jsonObject)
            throws IOException {
        String token = JwtUtil.getToken(request);
        boolean isLogin = JwtUtil.validateToken(token);
        ResponseResultSet checkLoginStatusResultSet = null;
        if (isLogin) {
            checkLoginStatusResultSet = ResponseResultSet.success(response);
            checkLoginStatusResultSet.data("roleId",JwtUtil.getRoleId(token));
            checkLoginStatusResultSet.data("username",JwtUtil.getSubject(token));
        } else {
            checkLoginStatusResultSet = ResponseResultSet.fail(response);
        }
        Mapper.writeValue(response.getWriter(), checkLoginStatusResultSet);
    }
}
