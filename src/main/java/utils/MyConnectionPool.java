package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Properties;

/**
 * 我的连接池
 *
 * @author Secret
 * @date 2023/04/17
 */
public class MyConnectionPool {
    protected static LinkedList<Connection> connections;
    protected int initialSize;
    protected int maxActive;
    protected int maxIdle;
    protected int minIdle;
    protected int maxWait;
    protected long maxAge;
    protected String username;
    protected String password;
    protected String driverClassName;
    protected String url;

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
        connections = new LinkedList<Connection>();
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
     * 获得连接
     *
     * @return {@link Connection}
     * @throws SQLException sqlexception异常
     */
    public synchronized Connection getConnection() throws SQLException {
        while (connections.size() < minIdle) {
            Connection conn = DriverManager.getConnection(url, username, password);
            connections.add(conn);
        }

        ListIterator<Connection> it = connections.listIterator();
        while (it.hasNext()) {
            Connection conn = it.next();
            if (isConnectionValid(conn)) {
//                it.remove();
                return conn;
            } else {
                if (conn.isClosed()) {
                    it.remove();
                }
            }
        }

        if (connections.size() < maxActive) {
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        }

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < maxWait) {
            for (Connection conn : connections) {
                if (isConnectionValid(conn)) {
                    connections.remove(conn);
                    return conn;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        throw new SQLException("Connection wait timeout");
    }

    /**
     * 释放连接
     *
     * @param conn 连接
     * @throws SQLException sqlexception异常
     */
    public synchronized void releaseConnection(Connection conn) throws SQLException {
        connections.remove(conn);
        if (connections.size() < maxIdle) {
            connections.add(conn);
        } else {
            conn.close();
        }
    }

    /**
     * 关闭所有连接
     *
     * @throws SQLException sqlexception异常
     */
    public synchronized void closeAll() throws SQLException {
        for (Connection conn : connections) {
            conn.close();
        }
        connections.clear();
    }
}
