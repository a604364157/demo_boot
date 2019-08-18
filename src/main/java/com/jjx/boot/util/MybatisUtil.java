package com.jjx.boot.util;

import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSON;
import com.jjx.boot.util.mybatis.entity.Field;
import org.apache.commons.dbcp.BasicDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class MybatisUtil {

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://172.21.11.181:3306/iomyf?characterEncoding=utf-8&useSSL=true&amp;zeroDateTimeBehavior=convertToNull";
    private static final String USER_NAME = "iomyf";
    private static final String PASSWORD = "iomyf";

    private List<Field> fields;

    private String tableName;

    public static void main(String[] args) {
        String s = "login_accept, batch_accept, sheetline_id, user_id, deal_flag, deal_result, remarks, \n" +
                "    deal_date, total_date";
        s = s.replaceAll(" ", "").replaceAll("\n", "").toUpperCase();
        createAllWhere(s.split("[,]"));
    }

    private static void createAllWhere(String[] arr) {
        List<String> list = new ArrayList<>();
        list.add("<where>");
        for (String s : arr) {
            String caS = MapBean.str2Lower4Ca(s);
            list.add("\t<if test=\" null != " + caS + "\">AND " + s + " = #{" + caS + "}</if>");
        }
        list.add("</where>");
        for (String s : list) {
            System.out.println(s);
        }
    }

    private void createEntity() {
        File file = createFile(tableName);
        fields.forEach(field -> {
            String fieldName = field.getField();
            String fieldType = field.getType();
            String primary = field.getKey();
        });
    }

    private void createMapper() {

    }

    private void createXml() {

    }

    private File createFile(String fileName) {
        File file = new File("com/jjx/boot/util/mybatis/generator/" + fileName);
        if (!file.exists()) {
            try {
                System.out.println(file.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private String firstToUp(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void qryTableField(String tableName) {
        BasicDataSource dataSource;
        try {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(DRIVER);
            dataSource.setUrl(URL);
            dataSource.setUsername(USER_NAME);
            dataSource.setPassword(PASSWORD);
            dataSource.setInitialSize(1);
            dataSource.setMaxActive(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化数据库连接时，发生异常");
        }
        String sql = "DESC " + tableName;
        try {
            fields = JSON.parseArray(JSON.toJSONString(JdbcUtils.executeQuery(dataSource, sql, new ArrayList<>())), Field.class);
            this.tableName = firstToUp(MapBean.str2Upper4Ca(tableName));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("获取表结构数据时，发生异常");
        }
    }

}
