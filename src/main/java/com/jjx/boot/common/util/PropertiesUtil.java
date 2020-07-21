package com.jjx.boot.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class PropertiesUtil {

    /**
     * 加载配置文件
     * @param url 文件路径
     * @param propName 文件名称
     * @return prop
     * @throws IOException IO异常
     */
    public static Properties loadProperties(String url, String propName) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(url));
        return prop;
    }

    /**
     * 获取指定配置文件的指定属性值
     * @param fileName 文件名称（运行时绝对路径）
     * @param key 属性名称
     * @return 属性值
     */
    public static String getProperty(String fileName, String key) {
        Properties prop = getProperties(fileName);
        return prop.getProperty(key);
    }

    /**
     * 加载配置文件
     * @param fileName 文件名（运行时绝对路径）
     * @return prop
     */
    public static Properties getProperties(String fileName) {
        Properties prop = new Properties();
        InputStream in = PropertiesUtil.class.getResourceAsStream("/" + fileName);
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getProperties("common.properties"));
        System.out.println(loadProperties("/", "common.properties"));
    }
}
