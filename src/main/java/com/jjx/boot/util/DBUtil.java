package com.jjx.boot.util;

import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.poi.ss.formula.functions.T;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 使用JDBC方式创建数据库连接
 * <p>
 * 这里的配置是生产环境的配置
 * <p>
 * 上代码到生产使用该配置
 * <p>
 * 其他环境请修改代码后重新编译上传
 * <p>
 * 否则应用会启动失败或者出现未知的异常
 *
 * @author Administrator
 */
public class DBUtil {

    private static BasicDataSource dataSource;


    /*
     * 静态创建数据库连接池
     */
    static {
        try {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://ip:port/db?serverTimezone=Asia/Shanghai");
            dataSource.setUsername("username");
            dataSource.setPassword("password");
            dataSource.setInitialSize(1);
            dataSource.setMaxActive(3);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化数据库连接时，发生异常");
        }
    }

    public static List<T> qryDataForList(String sql, List<Object> params, Class<T> clazz) {
        try {
            return JSON.parseArray(JSON.toJSONString(JdbcUtils.executeQuery(dataSource, sql, params)), clazz);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Map<String, Object>> qryDataForList(String sql, List<Object> params) {
        try {
            return JdbcUtils.executeQuery(dataSource, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}











