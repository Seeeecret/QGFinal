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
import static pojo.dto.ResponseResultSet.*;

/**
 * @author Secret
 */
@WebServlet("/user")
public class UserServlet extends BaseServlet {
    public static final Integer DEFAULT_PARENT_ID = 0;

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
            loginResultSet = success(response);
            response.addHeader("Authorization", "Bearer " + token);
        } else {
            loginResultSet = fail(response);
        }
        Mapper.writeValue(response.getWriter(), loginResultSet);
    }

    public void register(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        boolean register = UserService.register(username, password, Role.fromRoleId(roleId), null, null);
        ResponseResultSet registerResultSet = null;
        if (register) {
            registerResultSet = success(response);
        } else {
            registerResultSet = fail(response);
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
            changePasswordResultSet = success(response);
        } else {
            changePasswordResultSet = fail(response);
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
            deleteResultSet = success(response);
        } else {
            deleteResultSet = fail(response);
        }
        Mapper.writeValue(response.getWriter(), deleteResultSet);

    }

    /**
     * 更新用户信息
     *
     * @param request    请求
     * @param response   响应
     * @param jsonObject json对象
     * @throws ServletException servlet异常
     * @throws IOException      ioexception
     * @throws SQLException     sqlexception异常
     */
    public void updateUserInfo(HttpServletRequest request,
                               HttpServletResponse response, JSONObject jsonObject)
            throws ServletException, IOException, SQLException {
        String token = JwtUtil.getToken(request);
        int id = Integer.parseInt(Objects.requireNonNull(JwtUtil.getId(token)));
        ResponseResultSet updateUserInfoResultSet = null;
//        将jsonObject转换为hashmap,并获取父企业和父企业代码
        HashMap<String, Object> jsonHashMap = new HashMap<>(jsonObject);
        String webParentEnterprise = (String) jsonHashMap.get("enterprise");
        String webParentEnterpriseCode = (String) jsonHashMap.get("enterpriseCode");
//        移除不需要的字段
        jsonHashMap.remove("method");
        jsonHashMap.remove("page");

        User queryParentUser = UserService.query(webParentEnterprise);
        User idUser = UserService.query(id);
        switch (idUser.getRole().getRoleId()) {
            case 3:
                if (webParentEnterprise == null || "".equals(webParentEnterprise)
                        || "".equals(jsonHashMap.get("enterpriseCode")) || jsonHashMap.get("enterpriseCode") == null) {
//       如果父企业为空或者父企业代码为空,则不更新父企业和父企业代码，维持原样
                    jsonHashMap.replace("enterprise", UserService.getEnterprise(idUser));
                    jsonHashMap.replace("enterpriseCode", UserService.getEnterpriseCode(idUser));

                    UserService.updateInfoOnly(id, jsonHashMap);
                    updateUserInfoResultSet = success(response);
                } else {
                    if (queryParentUser == null || !(UserService.checkEnterpriseCode(webParentEnterpriseCode, queryParentUser))) {
                        //       如果父企业为空或者父企业代码为空,则不更新父企业和父企业代码，维持原样
                        jsonHashMap.replace("enterprise", UserService.getEnterprise(idUser));
                        jsonHashMap.replace("enterpriseCode", UserService.getEnterpriseCode(idUser));

                        UserService.updateInfoOnly(id, jsonHashMap);
                        updateUserInfoResultSet = partialContent(response);

                    } else {
                        UserService.updateParentInfoOnly(id,queryParentUser.getUserId(),jsonHashMap);
                        updateUserInfoResultSet = success(response);
                    }
                }
                break;
            case 2:
                UserService.updateParentInfoOnly(id, DEFAULT_PARENT_ID,jsonHashMap);
                updateUserInfoResultSet = success(response);
                break;
            case 1:
                UserService.updateInfoOnly(id, jsonHashMap);
                updateUserInfoResultSet = success(response);
                break;
            default:
                break;
        }
        Mapper.writeValue(response.getWriter(), updateUserInfoResultSet);
    }

    public void queryUserInfo(HttpServletRequest request,
                              HttpServletResponse response, JSONObject jsonObject)
            throws ServletException, IOException, SQLException {
        String token = JwtUtil.getToken(request);
        String username = JwtUtil.getSubject(token);
        User user = UserService.query(username);
        ResponseResultSet queryUserInfoResultSet = null;
        if (user != null) {
            queryUserInfoResultSet = success(response).data("userInfo", user.getJsonInfo()).data("username", user.getUsername());
        } else {
            queryUserInfoResultSet = fail(response);
        }
        Mapper.writeValue(response.getWriter(), queryUserInfoResultSet);
    }
}
