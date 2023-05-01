package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 我的连接池
 *
 * @author Secret
 * @date 2023/04/17
 */
public class MyConnectionPool {
    private final ConcurrentLinkedQueue<Connection> connections;

    private final int initialSize;
    private final int maxActive;
    private final int maxIdle;
    private final int minIdle;
    private final int maxWait;
    private final long maxAge;
    private final String username;
    private final String password;
    private final String driverClassName;
    private final String url;

    public MyConnectionPool(Properties props) throws SQLException, ClassNotFoundException {
        this.initialSize = Integer.parseInt(props.getProperty("initialSize"));
        this.maxActive = Integer.parseInt(props.getProperty("maxActive"));
        this.maxIdle = Integer.parseInt(props.getProperty("maxIdle"));
        this.minIdle = Integer.parseInt(props.getProperty("minIdle"));
        this.maxWait = Integer.parseInt(props.getProperty("maxWait"));
        this.maxAge = Long.parseLong(props.getProperty("maxAge"));
        this.url = props.getProperty("url");
        this.driverClassName = props.getProperty("driverClassName");
        this.username = props.getProperty("username");
        this.password = props.getProperty("password");
        Class.forName(driverClassName);
        connections = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < initialSize; i++) {
            Connection conn = DriverManager.getConnection(url, username, password);
            long createTime = System.currentTimeMillis();
            conn.setClientInfo("createTime", String.valueOf(createTime));
            connections.add(conn);
        }
    }

    /**
     * 连接是否有效
     *
     * @param conn 连接
     * @return boolean
     * @throws SQLException sqlexception异常
     */
    private boolean isConnectionValid(Connection conn) throws SQLException {
        if (conn.isClosed()) {
            return false;
        }

        String createTimeStr = conn.getClientInfo("createTime");
        if (createTimeStr != null) {
            long createTime = Long.parseLong(createTimeStr);
            long age = System.currentTimeMillis() - createTime;
            if (age > maxAge) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取连接
     *
     * @return 连接
     * @throws SQLException SQL异常
     */
    public Connection getConnection() throws SQLException {
        while (connections.size() < minIdle) {
            Connection conn = DriverManager.getConnection(url, username, password);
            connections.add(conn);
        }

        Connection conn = connections.poll();
        while (conn != null && !isConnectionValid(conn)) {
            try {
                conn.close();
            } catch (SQLException e) {
                // ignore exception on close
            }
            conn = connections.poll();
        }

        if (conn == null && connections.size() < maxActive) {
            conn = DriverManager.getConnection(url, username, password);
        }

        if (conn != null) {
            long createTime = System.currentTimeMillis();
            conn.setClientInfo("createTime", String.valueOf(createTime));
        } else {
            throw new SQLException("Connection pool exhausted.");
        }

        return conn;
    }

    /**
     * 释放连接
     *
     * @param conn 连接
     * @throws SQLException sqlexception异常
     */
    public  void releaseConnection(Connection conn) throws SQLException {
        if (conn!=null && connections.size() < maxIdle) {
            connections.offer(conn);
        } else {
            conn.close();
        }
    }

    /**
     * 关闭所有连接
     *
     * @throws SQLException sqlexception异常
     */
    public  void closeAll() throws SQLException {
        for (Connection conn : connections) {
            if(conn!=null) {
                conn.close();
            }
        }
        connections.clear();
    }
}
