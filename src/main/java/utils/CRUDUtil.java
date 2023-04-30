package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


/**
 * @author Secret
 */
public class CRUDUtil {
    /**
     * ResultSetWrapper类，用于包装ResultSet对象
     *
     * @author Secret
     * @date 2023/04/29
     */
    public static class ResultSetWrapper{
        private ResultSet resultSet;
        private PreparedStatement preparedStatement;
        private Connection connection;

        public ResultSetWrapper(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
            this.resultSet = resultSet;
            this.preparedStatement = preparedStatement;
            this.connection = connection;
        }

        public ResultSet getResultSet() {
            return resultSet;
        }

        public PreparedStatement getPreparedStatement() {
            return preparedStatement;
        }

        public Connection getConnection() {
            return connection;
        }

        public void close() {
            try {
                if (resultSet!=null) {
                    resultSet.close();
                }
                if (preparedStatement!=null) {
                    preparedStatement.close();
                }
                if (connection!=null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static MyConnectionPool myConnectionPool;

    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = CRUDUtil.class.getClassLoader().getResourceAsStream("MyConnectionPool.properties");
            properties.load(resourceAsStream);
            myConnectionPool = new MyConnectionPool(properties);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(resourceAsStream!=null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private CRUDUtil() {
    }


    /**
     * 获得连接
     *
     * @return {@link Connection}
     * @throws SQLException sqlexception异常
     */
    public static Connection getConnection() throws SQLException {
        return myConnectionPool.getConnection();
    }



    /**
     * 将连接交还数据源
     *
     * @param connection 连接
     * @throws SQLException sqlexception异常
     */
    public static void close(Connection connection) throws SQLException {
        myConnectionPool.releaseConnection(connection);
    }


    public static void close(Statement statement) throws SQLException {
        if (statement!=null){
            statement.close();
        }
    }


    public static void close(PreparedStatement preparedStatement) throws SQLException {
        if (preparedStatement!=null){
            preparedStatement.close();
        }
    }


    public static void close(ResultSet resultSet) throws SQLException {
        if (resultSet!=null){
            resultSet.close();
        }
    }


    public static void close(ResultSet resultSet,PreparedStatement preparedStatement,
                             Statement statement, Connection connection
                             ) throws SQLException {
        if (resultSet!=null){
            resultSet.close();
        }
        if (preparedStatement!=null){
            preparedStatement.close();
        }
        if (statement!=null){
            statement.close();
        }
        if (connection!=null){
            connection.close();
        }

    }

    /**
     * 创建新表
     *
     * @param createTableSQL 传入完整的创建新表的语句
     * @throws SQLException sqlexception异常
     */
    public static void createNewTable(String createTableSQL) throws SQLException {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = myConnectionPool.getConnection();
            statement = connection.createStatement();
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CRUDUtil.close(null, null, statement, connection);
        }
    }

    /**
     * 执行普通查询语句
     *
     * @param params               数量可变的参数
     * @param PreparedStatementSQL 预编译sql
     * @return {@link ResultSetWrapper}
     */
    public static ResultSetWrapper executeCommonQuery(String PreparedStatementSQL, Object... params) {
        ResultSet rs;
        try  {
            Connection connection = CRUDUtil.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(PreparedStatementSQL);
            for (int i = 0; i < params.length; i++) {
                queryStatement.setObject(i + 1, params[i]);
            }
            rs = queryStatement.executeQuery();
            return new ResultSetWrapper(rs,queryStatement,connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行特别查询语句
     *
     * @param queryContentName 查询内容名
     * @param tableName        表名
     * @param otherLimits      其他限制条件，如 where ... 注意字符串要用\'转义单引号
     */
    public static ResultSetWrapper executeSpecialQuery(String queryContentName, String tableName, String otherLimits) {
        String sql = "select " +
                queryContentName +
                " from " +
                tableName +
                " " +
                otherLimits;
        ResultSet rs;
        try  {
            Connection connection = CRUDUtil.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(sql);
            rs = queryStatement.executeQuery();
            return new ResultSetWrapper(rs,queryStatement,connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 执行插入语句
     *
     * @param sql        sql
     * @param params     参数个数
     * @return int
     * @throws SQLException sqlexception异常
     */
    public static int executeCommonInsert(String sql, Object... params) throws SQLException{
        return executeCommonUpdate(sql,params);
    }

    /**
     *
     * 执行特殊插入，仅需要输入表名以及按MySQL语句格式插入的字段名和数据
     *
     * @param tableName     表名
     * @param columnsName   列名
     * @param valuesContent 插入的值,注意字符串要用\'转义单引号
     * @return int
     * @throws SQLException sqlexception异常
     */
    public static int executeSpecialInsert(String tableName, String columnsName , String valuesContent) throws SQLException {
        PreparedStatement pstmt = null;
        Connection connection = null;
        if(columnsName == null){
            columnsName = "";
        }
        StringBuffer sql = new StringBuffer("insert into ").
                append(tableName).append(" ").
                append(columnsName).append(" values ").
                append(valuesContent);
        String toStringSQL = sql.toString();
        int rs;
        try {
            connection = CRUDUtil.getConnection();
            pstmt = connection.prepareStatement(toStringSQL);
            rs = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if(connection!=null){
                connection.close();
            }
        }
        return rs;
    }

    /**
     * 执行一般更新语句
     *
     * @param sql    sql
     * @param params 参数
     * @return int
     * @throws SQLException sqlexception异常
     */
    public static int executeCommonUpdate(String sql, Object... params) throws SQLException {

        PreparedStatement pstmt = null;

        Connection connection = myConnectionPool.getConnection();

        int result;

        try {
        pstmt = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        result = pstmt.executeUpdate();
    } catch (SQLException e) {
            e.printStackTrace();
        throw new RuntimeException(e);
        } finally {
        if (pstmt != null) {
            pstmt.close();
        }
            connection.close();
        }
    return result;
}

    /**
     * 执行特殊更新语句
     *
     * @param tableName     表名
     * @param otherLimits   其他限制，其他限制条件，如 where ... 注意字符串要用\'转义单引号
     * @param updateContent 更新内容，一个一个按名字输入，不用逗号
     * @return int
     * @throws SQLException sqlexception异常
     */
    public static int executeSpecialUpdate(String tableName, String otherLimits, String... updateContent) throws SQLException {
        PreparedStatement pstmt = null;
        Connection connection = null;

        StringBuffer sql = new StringBuffer("update ").append(tableName).append(" set ");
        for (String s : updateContent) {
            sql.append(s).append(",");
        }
        String toStringSQL = sql.deleteCharAt(sql.length() - 1).
                append(" ").
                append(otherLimits).
                toString();
        int rs;
        try {
            connection = CRUDUtil.getConnection();
            pstmt = connection.prepareStatement(toStringSQL);
            rs = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if(connection!=null){
                connection.close();
            }
        }
        return rs;
    }


    /**
     * 执行特殊删除语句
     *
     * @param tableName   表名
     * @param otherLimits 其他限制条件，如 where ... 注意字符串要用\'转义单引号
     * @return int
     * @throws SQLException sqlexception异常
     */
    public static int executeSpecialDelete(String tableName, String otherLimits) throws SQLException {
        PreparedStatement pstmt = null;
        Connection connection = null;

        StringBuffer sql = new StringBuffer("delete from ").
                append(tableName).append(" ").append(otherLimits);

        String toStringSQL = sql.toString();

        int rs;

        try {
            connection = CRUDUtil.getConnection();
            pstmt = connection.prepareStatement(toStringSQL);
            rs = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if(connection!=null){
                connection.close();
            }
        }
        return rs;
    }



    /**
     * 展示结果集
     *
     * @param rs 结果集
     * @throws SQLException sqlexception异常
     */
    public static void showResultSet(ResultSet rs) throws SQLException {

        ResultSetMetaData rsmd = rs.getMetaData();

        int columnCount = rsmd.getColumnCount();

        for(int i =1;i<=columnCount;i++){
            String columnName = rsmd.getColumnName(i);
            System.out.printf("%-16s",columnName);
            if(i<columnCount){
                System.out.print("\t");
            }else{
                System.out.println();
            }
        }
        while ((rs.next())){
            for (int i = 1; i <= columnCount; i++) {
                Object value = rs.getObject(i);
                System.out.printf("%-16s",value);

                if(i<columnCount){
                    System.out.print("\t");
                }else{
                    System.out.println();
                }
            }
        }
    }


    /**
     * 打印全表
     *
     * @param tableName 表名
     * @throws SQLException sqlexception异常
     */
    public static void showTable(String tableName) throws SQLException {
        Connection connection = myConnectionPool.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from " + tableName);
        ResultSet rs = ps.executeQuery();
        CRUDUtil.showResultSet(rs);
        ps.close();
    }

    /**
     * 开始事务
     *
     * @param conn 连接
     * @throws SQLException sqlexception异常
     */
    public static void beginTransaction(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
    }

    /**
     * 提交事务
     *
     * @param conn 连接
     * @throws SQLException sqlexception异常
     */
    public static void commitTransaction(Connection conn) throws SQLException {
        conn.commit();
    }

    /**
     * 回滚事务
     *
     * @param conn 连接
     * @throws SQLException sqlexception异常
     */
    public static void rollbackTransaction(Connection conn) throws SQLException {
        conn.rollback();
    }


}