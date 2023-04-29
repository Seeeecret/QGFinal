package controller;

import com.alibaba.fastjson.JSONObject;
import constants.Role;
import pojo.dto.ResponseResultSet;
import pojo.po.User;
import service.UserService;
import utils.JwtUtil;
import utils.Mapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

import static constants.RoleConstants.TRUE;

/**
 * @author Secret
 */
@WebServlet("/user")
public class UserServlet extends BaseServlet {

    public void login(HttpServletRequest request,
                      HttpServletResponse response, JSONObject jsonObject)
            throws IOException, SQLException {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String remember = jsonObject.getString("remember");
        String token = UserService.login(username, password);

        ResponseResultSet loginResultSet = null;
        if (token != null) {
            if (TRUE.equals(remember)) {
                Cookie usernameCookie = new Cookie("username", username);
                Cookie passwordCookie = new Cookie("password", password);
                usernameCookie.setMaxAge(60 * 60 * 24 * 7);
                passwordCookie.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(usernameCookie);
                response.addCookie(passwordCookie);
            }
            loginResultSet = ResponseResultSet.success(response);
            response.addHeader("Authorization", "Bearer "+ token);
        } else {
            loginResultSet = ResponseResultSet.fail(response);
        }
        Mapper.writeValue(response.getWriter(), loginResultSet);
    }

    public void register(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        boolean register = UserService.register(username, password, Role.fromRoleId(roleId),null,null);
        ResponseResultSet registerResultSet = null;
        if (register) {
            registerResultSet = ResponseResultSet.success(response);
        } else {
            registerResultSet = ResponseResultSet.fail(response);
        }
        Mapper.writeValue(response.getWriter(), registerResultSet);
    }

    public void changePassword(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException, SQLException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean changePassword = UserService.changePassword(username, password);
        ResponseResultSet changePasswordResultSet = null;
        if (changePassword) {
            changePasswordResultSet = ResponseResultSet.success(response);
        } else {
            changePasswordResultSet = ResponseResultSet.fail(response);
        }
        Mapper.writeValue(response.getWriter(), changePasswordResultSet);
    }

    public void deleteUser(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String token = UserService.login(username, password);
        ResponseResultSet deleteResultSet = null;
        if (token != null) {
            UserService.deleteUser(username);
            deleteResultSet = ResponseResultSet.success(response);
        } else {
            deleteResultSet = ResponseResultSet.fail(response);
        }
        Mapper.writeValue(response.getWriter(), deleteResultSet);

    }
    public void updateUserInfo(HttpServletRequest request,
                               HttpServletResponse response, JSONObject jsonObject)
            throws ServletException, IOException, SQLException {
        String token = JwtUtil.getToken(request);
        String username = JwtUtil.getSubject(token);
        int id = Integer.parseInt(Objects.requireNonNull(JwtUtil.getId(token)));
        ResponseResultSet updateUserInfoResultSet = null;
        HashMap<String,Object> jsonHashMap = new HashMap<>(jsonObject);
        String parentEnterprise = (String) jsonHashMap.get("enterprise");
        String parentEnterpriseCode = (String) jsonHashMap.get("enterpriseCode");

        if(parentEnterprise==null|| "".equals(parentEnterprise)
                || "".equals(jsonHashMap.get("enterpriseCode"))||jsonHashMap.get("enterpriseCode")==null){
            UserService.updateInfoOnly(id,jsonHashMap);
            updateUserInfoResultSet = ResponseResultSet.success(response);
        }else {
            User queryUser = UserService.query(parentEnterprise);
            if (queryUser == null || !(UserService.checkEnterpriseCode(parentEnterpriseCode, queryUser))) {
                UserService.updateInfoOnly(id, jsonHashMap);
                updateUserInfoResultSet = ResponseResultSet.partialContent(response);
            }else{
                UserService.updateParentInfoOnly(id,jsonHashMap);
                updateUserInfoResultSet = ResponseResultSet.success(response);
            }
        }
        Mapper.writeValue(response.getWriter(), updateUserInfoResultSet);
    }
}
