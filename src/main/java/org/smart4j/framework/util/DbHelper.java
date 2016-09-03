package org.smart4j.framework.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Mcfell on 16/8/30.
 */
public class DbHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbHelper.class);
    private static final String URL;
    private static final String DRIVER;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final QueryRunner QUERY_RUNNER;
    //线程池
    private static final BasicDataSource DATA_SOURCE = new BasicDataSource();
    static {
        QUERY_RUNNER = new QueryRunner();
        Properties properties = PropUtils.loadProps("config.properties");
        URL = PropUtils.getString(properties,"jdbc.url");
        DRIVER = PropUtils.getString(properties,"jdbc.driver");
        USERNAME = PropUtils.getString(properties, "jdbc.username");
        PASSWORD = PropUtils.getString(properties, "jdbc.password");
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        initDS(URL);
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver", e);
        }
    }

    /**
     * 创建数据源，除了数据库外，都使用硬编码默认参数；
     *
     * @param connectURI
     *            数据库
     * @return
     */
    private static void initDS(String connectURI) {
        initDS(connectURI, USERNAME, PASSWORD, DRIVER, 5, 100,
                30, 10000, 1);
    }
    /**
     * 指定所有参数连接数据源
     *
     * @param connectURI
     *            数据库
     * @param username
     *            用户名
     * @param pswd
     *            密码
     * @param driverClass
     *            数据库连接驱动名
     * @param initialSize
     *            初始连接池连接个数
     * @param maxtotal
     *            最大活动连接数
     * @param maxIdle
     *            最大连接数
     * @param maxWaitMillis
     *            获得连接的最大等待毫秒数
     * @param minIdle
     *            最小连接数
     * @return
     */
    private static void initDS(String connectURI, String username, String pswd,
                              String driverClass, int initialSize, int maxtotal, int maxIdle,
                              int maxWaitMillis , int minIdle) {
        DATA_SOURCE.setDriverClassName(driverClass);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(pswd);
        DATA_SOURCE.setUrl(connectURI);
        DATA_SOURCE.setInitialSize(initialSize); // 初始的连接数；
        DATA_SOURCE.setMaxTotal(maxtotal);
        DATA_SOURCE.setMaxIdle(maxIdle);
        DATA_SOURCE.setMaxWaitMillis(maxWaitMillis);
        DATA_SOURCE.setMinIdle(minIdle);
    }

    private static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get Connnection Failure",e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    private static void closeConnection(){
        Connection conn = CONNECTION_HOLDER.get();
        if (conn != null) {
            //LOGGER.debug("Connect pools num :" + DATA_SOURCE.getNumIdle() + "," + DATA_SOURCE.getNumActive());
            CONNECTION_HOLDER.remove();
        }
    }
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = null;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
        } finally {
            closeConnection();
        }
        return  entityList;
    }

    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T obj = null;
        try {
            Connection conn = getConnection();
            obj = QUERY_RUNNER.query(conn,sql,new BeanHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
        } finally {
            closeConnection();
        }
        return  obj;
    }

    /**
     * 执行更新语句
     * @param sql
     * @param params
     * @return
     */
    public static int excuteUpdate(String sql, Object... params) {
        Connection conn = getConnection();
        int result = 0;
        try {
            result = QUERY_RUNNER.update(conn,sql,params);
        } catch (SQLException e) {
            LOGGER.error("update entity failure", e);
        } finally {
            closeConnection();
        }
        return result;
    }

    public static <T> int insertEntity(Class<T> entityClass, Map<String,Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not insert empty fieldMap");
            return -1;
        }

        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");

        for (String fieldName:fieldMap.keySet()) {
            columns.append(fieldName).append(",");
            values.append("?, ");
        }

        columns.replace(columns.lastIndexOf(","),columns.length(),")");
        values.replace(values.lastIndexOf(","),values.length(),")");

        sql += columns + " VALUES " + values;

        Object[] params = fieldMap.values().toArray();
        return excuteUpdate(sql, params);
    }

    public static <T> int updateEntity(Class<T> entityClass, int id, Map<String,Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not update empty fieldMap");
            return -1;
        }

        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0,columns.lastIndexOf(", ")) + " WHERE id=?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return excuteUpdate(sql, params);
    }

    public static <T> int deleteEntity(Class<T> entityCLass, int id) {
        LOGGER.debug(getTableName(entityCLass));
        String sql = "DELETE FROM " + getTableName(entityCLass) + " where id = ?";
        return excuteUpdate(sql, id);
    }
    private static <T> String getTableName(Class<T> entityClass) {
        return entityClass.getSimpleName().toLowerCase();
    }
}
