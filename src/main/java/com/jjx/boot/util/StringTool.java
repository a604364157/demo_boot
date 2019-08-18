package com.jjx.boot.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class StringTool {

    private final static Pattern P1 = Pattern.compile("^\\d+$");
    private final static Pattern P2 = Pattern.compile("^-?\\d+$");

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.trim().length() > 0;
    }

    public static boolean isContains(String str, String[] args) {
        boolean isContain = false;
        for (String string : args) {
            if (string.equals(str)) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    /**
     * 对象为NULL时，转换为空
     *
     * @param obj 验证对象
     * @return 空对象
     */
    public static String convertEmptyWhenNull(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    /**
     * 验收数字是否为正数
     *
     * @param str 验证对象
     * @return 正数:true,其他:false
     */
    public static boolean isPositiveNum(String str) {
        if (str == null) {
            return false;
        }
        Matcher isNum = P1.matcher(str);
        return isNum.matches();
    }

    /**
     * 验证两个对象是否相等
     *
     * @param obj1 对象一
     * @param obj2 对象二
     * @return 相等 true，不相等：false
     */
    public static boolean isEqual(Object obj1, Object obj2) {
        return obj1 == null && obj2 == null || obj1 != null && obj1.equals(obj2);
    }

    /**
     * 验证对象是否为空或NULl
     *
     * @param str 验证对象
     * @return 处理结果 空/Null:true,否则:false
     */
    public static boolean isEmptyOrNull(Object str) {
        return "".equals(str) || null == str;
    }

    /**
     * 验证对象不为空也不为NULL
     *
     * @param str 验证对象
     * @return 处理结果 空/Null:true,否则:false
     */
    public static boolean isNotEmptyOrNull(Object str) {
        return !"".equals(str) && null != str;
    }

    /**
     * 验证字符串为数据
     *
     * @param str 字符串
     * @return 数字：true，其他：false
     */
    public static boolean isNumeric(String str) {
        if (isEmptyOrNull(str)) {
            return false;
        }
        Matcher isNum = P2.matcher(str);
        return isNum.matches();
    }

    /**
     * 自定义正则表达式进行各类验证
     *
     * @param str   待验证字符串
     * @param regex 正则表达式
     * @return 验证通过：true, 不通过：false
     */
    public static boolean regularValid(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 对象转为字符串
     *
     * @param o 输入参数
     * @return 字符串
     * @author lixiao
     */
    public static String o2s(Object o) {
        if (null == o) {
            o = "";
        }
        return String.valueOf(o);
    }

    /**
     * 对象转为整形
     *
     * @param o object
     * @return int
     * @author lixiao
     */
    public static int o2i(Object o) {
        if (isEmptyOrNull(o)) {
            return 0;
        }
        return Integer.parseInt(String.valueOf(o));
    }

    /**
     * 空判断
     */
    public static boolean isNull(Object... objs) {
        for (Object o : objs) {
            if (null == o) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断参数都为NULL
     *
     * @author jiangjx
     */
    public static boolean isAllNull(Object... objs) {
        Integer size = objs.length;
        Integer num = 0;
        for (Object obj : objs) {
            if (obj == null) {
                num++;
            }
        }
        return num >= size;
    }

    public static long o2l(Object obj) {
        if (isNumeric(o2s(obj))) {
            return Long.parseLong(o2s(obj));
        } else {
            return 0;
        }
    }

    /**
     * 参数为空判断
     *
     * @param o if null == o || "".equals(o) return true;
     * @author lixiao
     */
    public static boolean isEN(Object... o) {
        if (null == o) {
            return true;
        }
        for (Object cell : o) {
            if (null == cell) {
                return true;
            }
            String val = Arrays.toString(o);
            if ("".equals(val)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断数字是否等于0或者为null
     *
     * @param nums 数据
     * @return boolean
     * @author jiangjx
     */
    public static boolean isZerOrNull4Num(Integer... nums) {
        for (Integer i : nums) {
            if (i == null || i == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isZerOrNull4Num(Long... nums) {
        for (Long i : nums) {
            if (i == null || i == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 限制IP参数封装
     *
     * @param keys 限制IP参数
     * @return 返回报文需要的参数
     * @author lixiao
     */
    public static List<Map<String, Object>> converRetMapList(List<Map<String, Object>> mps, String... keys) {
        if (null == mps) {
            return null;
        }
        List<Map<String, Object>> retList = new ArrayList<>();
        for (Map<String, Object> map : mps) {
            retList.add(converRetMap(map, keys));
        }
        return retList;
    }

    /**
     * 出参类型转换，从MAP中取出指定参数
     *
     * @param map  源MAP
     * @param keys 从源MAP 中取出的key 集合
     * @return 过滤后的map
     * @author lixiao
     */
    public static Map<String, Object> converRetMap(Map<String, Object> map, String... keys) {
        if (null == map) {
            return null;
        }
        Map<String, Object> retMap = new HashMap<>(16);
        for (String key : keys) {
            retMap.put(key, map.get(key));
        }
        return retMap;
    }

    /**
     * 判断参数1与后面的所以参数都不相等
     *
     * @param obj  比较值
     * @param objs 被比较值
     * @return boolean
     * @author jaingjx
     */
    public static boolean isNotAllEq(Object obj, Object... objs) {
        for (Object o : objs) {
            if (isEqual(obj, o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取当前字符串字节长度
     * 按照一个汉字2个字节算
     *
     * @param str 参数字符
     * @return 长度
     */
    public static int getWordLen(String str) {
        int len;
        try {
            len = str.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            return 0;
        }
        return len;
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     * 验证字符串为中文
     *
     * @param strName 串
     * @return boolean
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (char c : ch) {
            if (!isChinese(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证value在数组中的索引
     * 若不存在则返回-1
     */
    public static int indexOfList(Object obj, Object[] objs) {
        for (int i = 0; i < objs.length; i++) {
            if (isEqual(obj, objs[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 参数左补零
     *
     * @param s    原字符串
     * @param size 总位数
     * @return 补位后串
     */
    public static String lestAddZero(String s, int size) {
        String zero = "0";
        if (isEmptyOrNull(s)) {
            return s;
        }
        if (s.length() >= size) {
            return s;
        }
        StringBuilder sBuilder = new StringBuilder(s);
        for (int i = 0; i < size - sBuilder.length(); i++) {
            sBuilder.insert(0, zero);
        }
        s = sBuilder.toString();
        return s;
    }

    /**
     * 根据路径获取多层map中的值
     *
     * @param map map
     * @param path 路径
     * @return 值
     */
    public static String getValueByPath(Map<String, Object> map, String path) {
        return o2s(getValueByPath(map, path, String.class));
    }
    @SuppressWarnings("all")
    public static <T> T getValueByPath(Map<String, Object> map, String path, Class<T> clazz) {
        if (path.contains(".")) {
            String[] paths = path.split("[.]");
            Map<String, Object> param = new HashMap<>(map);
            for (int i = 0; i < paths.length; i++) {
                try {
                    Object tmp = param.get(paths[i]);
                    if (i == paths.length - 1) {
                        T t = (T) tmp;
                        param = null;
                        return t;
                    } else {
                        param = (Map<String, Object>) tmp;
                    }
                } catch (Exception e) {
                    param = null;
                    return null;
                }
            }
        }
        return null;
    }
}