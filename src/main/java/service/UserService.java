package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import constants.Role;
import dao.UserDAO;
import pojo.po.User;
import utils.CRUDUtil;
import utils.JwtUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author Secret
 */
public class UserService {

    public static User query(String username) throws SQLException {
        User user = null;
        // try-with-resources语句会自动关闭资源!!记得改
        Connection connection = null;
        try {
            connection = CRUDUtil.getConnection();
            user = UserDAO.query(connection, username);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CRUDUtil.close(connection);
        }
        return user;
    }

    public static User query(int id) throws SQLException {
        User user = null;
        // try-with-resources语句会自动关闭资源!!记得改
        Connection connection = null;
        try {
            connection = CRUDUtil.getConnection();
            user = UserDAO.query(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CRUDUtil.close(connection);
        }
        return user;
    }

    public static String login(String username, String password) throws SQLException {
        User user = null;
        Connection connection = null;
        try {
            connection = CRUDUtil.getConnection();
            user = UserDAO.query(connection, username);
            if (user != null && user.getPassword().equals(password)) {
                String token = JwtUtil.generateToken(Integer.toString(user.getUserId()), user.getUsername(), user.getRole().getRoleId());
                 return token;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CRUDUtil.close(connection);
        }
        return null;
    }
 public static void updateInfoOnly(int id, HashMap<String,Object> jsonHashMap) throws SQLException {
        User user = null;
        Connection connection = null;
     String jsonString = JSON.toJSONString(jsonHashMap);
     try {
            connection = CRUDUtil.getConnection();
            user = UserDAO.query(connection, id);
            if (user != null) {
                user.setJsonInfo(jsonString);
                UserDAO.updateInfoOnly(connection, user);
                return;
            }
            CRUDUtil.close(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CRUDUtil.close(connection);
        }
 }
 public static void updateParentInfoOnly(int webUserId, int sqlParentId, HashMap<String,Object> jsonHashMap) throws SQLException {
     User user = null;
     Connection connection = null;
     String jsonString = JSON.toJSONString(jsonHashMap);
     try {
         connection = CRUDUtil.getConnection();
         user = UserDAO.query(connection, webUserId);
         if (user != null) {
             user.setJsonInfo(jsonString);
             user.setParentId(sqlParentId);
             UserDAO.updateParentInfoOnly(connection, user);
             return;
         }
         CRUDUtil.close(connection);
     } catch (SQLException e) {
         throw new RuntimeException(e);
     } finally {
         CRUDUtil.close(connection);
     }
 }
    /**
     * 注册账号
     *
     * @param username 用户名
     * @param password 密码
     * @return boolean
     */
    public static boolean register(String username, String password, Role userRole,Integer parentId,String info) throws SQLException {
        User user = null;
        Connection connection = null;
        try {
            connection = CRUDUtil.getConnection();
            user = UserDAO.query(connection, username);
            if (user == null) {
                user = new User(username, password,userRole,parentId,info);
                UserDAO.insert(connection, user);
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CRUDUtil.close(connection);
        }
        return false;
    }

    public static boolean changePassword(String username, String password) throws SQLException {
        User user = null;
        Connection connection = null;
        try {
            connection = CRUDUtil.getConnection();
            user = UserDAO.query(connection, username);
            if (user != null) {
                user.setPassword(password);
                UserDAO.updateInfoOnly(connection, user);
                return true;
            }
            CRUDUtil.close(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CRUDUtil.close(connection);
        }
        return false;
    }

    public static void deleteUser(String username) throws SQLException {
        User user = null;
        Connection connection = null;
        try {
            connection = CRUDUtil.getConnection();
            user = UserDAO.query(connection, username);
            if (user != null) {
                UserDAO.delete(connection, username);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CRUDUtil.close(connection);
        }
    }

    public static String getEnterpriseCode(User user) {
        return JSONObject.parseObject(user.getJsonInfo()).getString("enterpriseCode");
    }
    public static String getEnterprise(User user) {
        return JSONObject.parseObject(user.getJsonInfo()).getString("enterprise");
    }
    public static boolean checkEnterpriseCode(String code, User user) {
        return code.equals(getEnterpriseCode(user));
    }
    public UserService() {

    }
}
