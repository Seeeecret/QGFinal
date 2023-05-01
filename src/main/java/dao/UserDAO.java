package dao;

import constants.Role;
import pojo.po.User;
import utils.CRUDUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static User query(Connection connection, String username) {
        User user = null;
        CRUDUtil.ResultSetWrapper queryResultSetWrapper = null;
        try {
            queryResultSetWrapper = CRUDUtil.executeCommonQuery("SELECT * FROM user WHERE username = ?", username);
            ResultSet resultSet = queryResultSetWrapper.getResultSet();
            if (resultSet.next()) {
                user = new User();
                user.setUserId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.fromRoleId(resultSet.getInt("role_id")));
                user.setParentId(resultSet.getInt("parent_id"));
                user.setJsonInfo(resultSet.getString("info"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (queryResultSetWrapper != null) {
                queryResultSetWrapper.close();
            }
        }
        return user;
    }

    public static User query(Connection connection, int userId) {
        User user = null;
        CRUDUtil.ResultSetWrapper queryResultSetWrapper = null;
        try {
            queryResultSetWrapper = CRUDUtil.executeCommonQuery("SELECT * FROM user WHERE id = ?", userId);
            ResultSet resultSet = queryResultSetWrapper.getResultSet();
            if (resultSet.next()) {
                user = new User();
                user.setUserId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(Role.fromRoleId(resultSet.getInt("role_id")));
                user.setParentId(resultSet.getInt("parent_id"));
                user.setJsonInfo(resultSet.getString("info"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (queryResultSetWrapper != null) {
                queryResultSetWrapper.close();
            }
        }
        return user;
    }

    public static void insert(Connection connection, User user) {
        try {
            CRUDUtil.executeCommonInsert("insert into user (username, password, role_id, parent_id, info) VALUES (?,?,?,?,?)", user.getUsername(), user.getPassword(), user.getRole().getRoleId(), user.getParentId(), user.getJsonInfo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateInfoOnly(Connection connection, User user) {
        try {
            CRUDUtil.executeSpecialUpdate("user", "where id= " + user.getUserId(), "info = '" + user.getJsonInfo()+"'");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateParentInfoOnly(Connection connection, User user) {
        try {
            CRUDUtil.executeSpecialUpdate("user", "where id= " + user.getUserId(), "info = '" + user.getJsonInfo()+"'","parent_id = " + user.getParentId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateParentId(Connection connection, User user) {
        try {
            CRUDUtil.executeSpecialUpdate("user", "where id= " + user.getUserId(), "parent_id = " + user.getParentId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        public static void delete (Connection connection, String username){
            try {
                CRUDUtil.executeSpecialDelete("user", "username = " + username);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



    public UserDAO() {
        }
    }
