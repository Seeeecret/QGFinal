package dao;

import pojo.po.User;
import utils.CRUDUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
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
            CRUDUtil.executeSpecialInsert("user", "username,password", "(" + user.getUsername() + "," + user.getPassword() + ")");
//            PreparedStatement statement = connection.prepareStatement("INSERT INTO user_merit (username, password) VALUES (?, ?)");
//            statement.setString(1, user.getUsername());
//            statement.setString(2, user.getPassword());
//            i = statement.executeUpdate();
//            CRUDUtil.close(statement);
//            return i;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(Connection connection, User user) {
        try {
            CRUDUtil.executeSpecialUpdate("user", "where username= " + user.getUsername(), "password = " + user.getPassword());
//            PreparedStatement statement = connection.prepareStatement("UPDATE user_merit SET password = ? WHERE username = ?");
//            statement.setString(1, user.getPassword());
//            statement.setString(2, user.getUsername());
//            int i = statement.executeUpdate();
//            CRUDUtil.close(statement);
//            return i;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(Connection connection, String username) {
        try {
            CRUDUtil.executeSpecialDelete("user", "username = " + username);
//            PreparedStatement statement = connection.prepareStatement("DELETE FROM user_merit WHERE username = ?");
//            statement.setString(1, username);
//            int i = statement.executeUpdate();
//            CRUDUtil.close(statement);
//            return i;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static int addMerit(String username) throws SQLException {
        Connection connection = CRUDUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_merit SET merit = merit + 1 WHERE username = ?");
        preparedStatement.setString(1, username);
        int i = preparedStatement.executeUpdate();
        CRUDUtil.close(preparedStatement);
        CRUDUtil.close(connection);
        return i;
    }

    public UserDAO() {
    }
}
