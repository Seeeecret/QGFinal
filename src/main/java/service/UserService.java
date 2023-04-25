package service;

import dao.UserDAO;
import pojo.po.User;
import utils.CRUDUtil;

import java.sql.Connection;
import java.sql.SQLException;

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

    public static User login(String username, String password) throws SQLException {
        User user = null;
        Connection connection = null;
        try {
            connection = CRUDUtil.getConnection();
            user = UserDAO.query(connection, username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CRUDUtil.close(connection);
        }
        return null;
    }

    /**
     * 注册账号
     *
     * @param username 用户名
     * @param password 密码
     * @return boolean
     */
    public static boolean register(String username, String password) throws SQLException {
        User user = null;
        Connection connection = null;
        try {
            connection = CRUDUtil.getConnection();
            user = UserDAO.query(connection, username);
            if (user == null) {
                user = new User(username, password);
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
                UserDAO.update(connection, user);
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

    public UserService() {

    }
}
