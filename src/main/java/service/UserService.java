package service;

import dao.UserDAO;
import pojo.User;
import utils.CRUDUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Secret
 */
public class UserService {

    public  static User query(String username) {
//        User user = new User("username", "password");
        User user = null;
        try (Connection connection = CRUDUtil.getConnection()) {
            user = UserDAO.query(connection, username);
            CRUDUtil.close(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    public static User login(String username, String password) {
        User user = null;
        try (Connection connection = CRUDUtil.getConnection()) {
            user = UserDAO.query(connection, username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            CRUDUtil.close(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 注册账号
    public static boolean register(String username, String password) {
        User user = null;
        try (Connection connection = CRUDUtil.getConnection()) {
            user = UserDAO.query(connection, username);
            if (user == null) {
                user = new User(username, password);
                UserDAO.insert(connection, user);
                return true;
            }
            CRUDUtil.close(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean changePassword(String username, String password) {
        User user = null;
        try (Connection connection = CRUDUtil.getConnection()) {
            user = UserDAO.query(connection, username);
            if (user != null) {
                user.setPassword(password);
                UserDAO.update(connection, user);
                return true;
            }
            CRUDUtil.close(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean deleteUser(String username) {
        User user = null;
        try (Connection connection = CRUDUtil.getConnection()) {
            user = UserDAO.query(connection, username);
            if (user != null) {
                UserDAO.delete(connection, username);
                return true;
            }
            CRUDUtil.close(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public UserService() {

    }
}
