package com.jjx.boot.common.util;

import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class MapBean {

    private static Pattern p = Pattern.compile("[A-Z]");

    private static Pattern p2 = Pattern.compile("[_][A-Z]");

    /**
     * 将map转换为对应的javabean对象
     *
     * @param map       map
     * @param beanClass 对应的javabean
     * @param ca        map中的key是否为驼峰大写
     * @return javabean 的object对象
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass, boolean ca) {
        if (map == null) {
            return null;
        }
        T t;
        try {
            t = beanClass.getDeclaredConstructor().newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                Method setter = property.getWriteMethod();
                if (setter != null) {
                    String name = property.getName();
                    String type = property.getPropertyType().getName();
                    if (ca) {
                        name = str2Upper4Ca(name);
                    }
                    Object value = map.get(name);
                    if (StringUtils.isEmpty(StringTool.o2s(value))) {
                        continue;
                    }
                    if (StringUtils.pathEquals(type, "java.util.Date")) {
                        value = DateConst.getDate(StringTool.o2s(value));
                    }
                    Date date = DateConst.getDate(StringTool.o2s(value));
                    if (StringUtils.pathEquals(type, "java.sql.Date")) {
                        if (date != null) {
                            value = new java.sql.Date(date.getTime());
                        } else {
                            value = null;
                        }
                    }
                    if (StringUtils.pathEquals(type, "java.sql.Timestamp")) {
                        if (date != null) {
                            value = new java.sql.Date(date.getTime());
                        } else {
                            value = null;
                        }
                    }
                    setter.invoke(t, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("mapToObject函数执行错误");
        }
        return t;
    }

    /**
     * 将javabean转为一个对应的map对象
     *
     * @param obj javabean
     * @param ca  生成的map中的key是否驼峰大写
     * @return map
     */
    public static Map<String, Object> objectToMap(Object obj, boolean ca) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map;
        try {
            map = new HashMap<>(16);
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                String type = property.getPropertyType().getName();
                if (key.compareToIgnoreCase("class") != 0) {
                    Method getter = property.getReadMethod();
                    Object value = getter != null ? getter.invoke(obj) : "";
                    if (StringUtils.pathEquals("java.util.Date", type) && value instanceof Date) {
                        value = DateConst.getDateStr((Date) value, DateConst.DATE_MODEL_2);
                    } else if (StringUtils.pathEquals("java.sql.Date", type) && value instanceof java.sql.Date) {
                        value = DateConst.getDateStr((java.sql.Date) value, DateConst.DATE_MODEL_5);
                    } else if (StringUtils.pathEquals("java.sql.Timestamp", type) && value instanceof Timestamp) {
                        value = DateConst.getDateStr((Timestamp) value, DateConst.DATE_MODEL_2);
                    }
                    if (ca) {
                        key = str2Upper4Ca(key);
                    }
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("objectToMap函数执行错误");
        }
        return map;
    }

    /**
     * 将字符串按照驼峰规则转大写
     *
     * @param s 原字符串
     * @return 转换后字符串
     */
    public static String str2Upper4Ca(String s) {
        if (s == null) {
            return null;
        }
        Matcher match = p.matcher(s);
        while (match.find()) {
            String str = match.group();
            s = s.replace(str, "_" + str.toLowerCase());
        }
        return s.toUpperCase();
    }

    /**
     * 将大写字符串按照驼峰规则转小写
     *
     * @param s 原字符串
     * @return 转换后的字符串
     */
    public static String str2Lower4Ca(String s) {
        Matcher m = p2.matcher(s);
        while (m.find()) {
            String str = m.group();
            s = s.replaceFirst(str, String.valueOf(str.toCharArray()[1]).toLowerCase());
        }
        return upperAndLower(s);
    }

    /**
     * 将字符串中的字母的大小写互转
     *
     * @param str 字符串
     * @return 转换后的新串
     */
    public static String upperAndLower(String str) {
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (((int) ch[i] > 96) && ((int) ch[i] < 123)) {
                ch[i] = (char) ((int) ch[i] - 32);
            } else if (((int) ch[i] > 64) && ((int) ch[i] < 91)) {
                ch[i] = (char) ((int) ch[i] + 32);
            }
        }
        return String.valueOf(ch);
    }

}
