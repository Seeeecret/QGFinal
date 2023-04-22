package controller;

import dao.UserDAO;
import pojo.User;
import service.UserService;
import utils.Mapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import static constants.RoleConstants.TRUE;

/**
 * @author Secret
 */
@WebServlet("/user")
public class UserServlet extends BaseServlet {
    public void addMerit(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {
        // 设置响应内容类型
        HashMap<String, Object> jsonMap = new HashMap<>(5);
        String username = request.getParameter("username");
        int i = 0;
        try {
            i = UserDAO.addMerit(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            int merit = UserService.query(username).getMerit();
            if (i == 0) {
                jsonMap.put("data", i);
                jsonMap.put("code", 400);
                jsonMap.put("msg", "功德增加失败");
            } else {
                jsonMap.put("code", 200);
                jsonMap.put("data", merit);
                jsonMap.put("msg", "功德增加成功");
            }
        }
        Mapper.writeValue(response.getWriter(), jsonMap);

    }

    public void queryMerit(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {
        // 设置响应内容类型
        HashMap<String, Object> jsonMap = new HashMap<>(5);
        String username = request.getParameter("username");
        int i = 0;
        i = UserService.query(username).getMerit();
        jsonMap.put("code", 200);
        jsonMap.put("msg", "功德查询成功");
        jsonMap.put("data", i);
        Mapper.writeValue(response.getWriter(), jsonMap);
    }

    public void login(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {
        // 设置响应内容类型
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        User login;
        login = UserService.login(username, password);
        HashMap<String, Object> jsonMap = new HashMap<>(5);
        if (login != null) {
            if(TRUE.equals(remember)) {
                Cookie usernameCookie = new Cookie("username", username);
                Cookie passwordCookie = new Cookie("password", password);
                usernameCookie.setMaxAge(60 * 60 * 24 * 7);
                passwordCookie.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(usernameCookie);
                response.addCookie(passwordCookie);

            }
            jsonMap.put("code", 200);
            jsonMap.put("msg", "登陆成功");
            jsonMap.put("data", login);
        } else {
            jsonMap.put("code", 400);
            jsonMap.put("msg", "登陆失败");
        }
        Mapper.writeValue(response.getWriter(), jsonMap);
    }

    public void register(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean register = UserService.register(username, password);
        HashMap<String, Object> jsonMap = new HashMap<>(5);
        if (register) {
            jsonMap.put("code", 200);
            jsonMap.put("msg", "注册成功");
        } else {
            jsonMap.put("code", 400);
            jsonMap.put("msg", "用户名重复, 注册失败");
        }
        Mapper.writeValue(response.getWriter(), jsonMap);
    }

    public void changePassword(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {
        // 设置响应内容类型
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean changePassword = UserService.changePassword(username, password);
        HashMap<String, Object> jsonMap = new HashMap<>(5);
        if (changePassword) {
            jsonMap.put("code", 200);
            jsonMap.put("msg", "修改成功");
        } else {
            jsonMap.put("code", 400);
            jsonMap.put("msg", "用户不存在, 修改失败");
        }
        Mapper.writeValue(response.getWriter(), jsonMap);
    }

    public void deleteUser(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User login = UserService.login(username, password);
        HashMap<String, Object> jsonMap = new HashMap<>(5);

        if (login != null) {
            boolean delete = UserService.deleteUser(username);
            jsonMap.put("username", login.getUsername());
            jsonMap.put("password", login.getPassword());
            jsonMap.put("code", 200);
            jsonMap.put("msg", "删除成功");
        } else {
            jsonMap.put("code", 400);
            jsonMap.put("msg", "账号或密码错误, 删除失败");
        }
        Mapper.writeValue(response.getWriter(), jsonMap);

    }
}
