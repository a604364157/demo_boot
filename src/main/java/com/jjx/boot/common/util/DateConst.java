package com.jjx.boot.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Administrator
 */
@SuppressWarnings("all")
public class DateConst {
    public static final String DATE_MODEL_1 = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_MODEL_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_MODEL_3 = "yyyy-MM-dd HH:mm";
    public static final String DATE_MODEL_4 = "yyyy-MM-dd HH";
    public static final String DATE_MODEL_5 = "yyyy-MM-dd";
    public static final String DATE_MODEL_6 = "yyyy-MM";

    public static final String DATE_MODEL_7 = "yyyyMMddHHmmss.SSS";
    public static final String DATE_MODEL_77 = "yyyyMMddHHmmssSSS";
    public static final String DATE_MODEL_8 = "yyyyMMddHHmmss";
    public static final String DATE_MODEL_9 = "yyyyMMddHHmm";
    public static final String DATE_MODEL_10 = "yyyyMMddHH";
    public static final String DATE_MODEL_11 = "yyyyMMdd";
    public static final String DATE_MODEL_12 = "yyyyMM";

    public static final String DATE_MODEL_13 = "yyyy/MM/dd HH:mm:ss.SSS";
    public static final String DATE_MODEL_14 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_MODEL_15 = "yyyy/MM/dd HH:mm";
    public static final String DATE_MODEL_16 = "yyyy/MM/dd HH";
    public static final String DATE_MODEL_17 = "yyyy/MM/dd";
    public static final String DATE_MODEL_18 = "yyyy/MM";

    public static final String DATE_MODEL_20 = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String DATE_MODEL_21 = "yyyy年MM月dd日";

    public static final String DATE_MODEL_99 = "yyyy";

    /**
     * 根据日期字符串，判断日期格式。常用于不知道的日期格式判断。
     *
     * @param date 日期字符串
     * @return String
     */
    private static String getType(String date) {
        String result = null;
        if (date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d+")) {
            result = DateConst.DATE_MODEL_1;
        } else if (date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            result = DateConst.DATE_MODEL_2;
        } else if (date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
            result = DateConst.DATE_MODEL_3;
        } else if (date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}")) {
            result = DateConst.DATE_MODEL_4;
        } else if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            result = DateConst.DATE_MODEL_5;
        } else if (date.matches("\\d{4}-\\d{2}")) {
            result = DateConst.DATE_MODEL_6;
        } else if (date.matches("\\d{17}")) {
            result = DateConst.DATE_MODEL_77;
        } else if (date.matches("\\d{14}.\\d+")) {
            result = DateConst.DATE_MODEL_7;
        } else if (date.matches("\\d{14}")) {
            result = DateConst.DATE_MODEL_8;
        } else if (date.matches("\\d{12}")) {
            result = DateConst.DATE_MODEL_9;
        } else if (date.matches("\\d{10}")) {
            result = DateConst.DATE_MODEL_10;
        } else if (date.matches("\\d{8}")) {
            result = DateConst.DATE_MODEL_11;
        } else if (date.matches("\\d{6}")) {
            result = DateConst.DATE_MODEL_12;
        } else if (date.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d+")) {
            result = DateConst.DATE_MODEL_13;
        } else if (date.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            result = DateConst.DATE_MODEL_14;
        } else if (date.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}")) {
            result = DateConst.DATE_MODEL_15;
        } else if (date.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}")) {
            result = DateConst.DATE_MODEL_16;
        } else if (date.matches("\\d{4}/\\d{2}/\\d{2}")) {
            result = DateConst.DATE_MODEL_17;
        } else if (date.matches("\\d{4}/\\d{2}")) {
            result = DateConst.DATE_MODEL_18;
        } else if (date.matches("\\d{4}年\\d{2}月\\d{2}日 \\d{2}时\\d{2}分\\d{2}秒")) {
            result = DATE_MODEL_20;
        } else if (date.matches("\\d{4}年\\d{2}月\\d{2}日")) {
            result = DATE_MODEL_21;
        }
        return result;
    }

    /**
     * 将传入时间串转换为时间
     * @param date 时间串
     * @return date
     */
    public static Date getDate(String date) {
        try {
            String dateType = getType(date);
            SimpleDateFormat format = new SimpleDateFormat(dateType);
            return format.parse(date);
        } catch (Exception e) {
            return new Date();
        }
    }

    private static Date o2Date(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof String) {
            return getDate(obj.toString());
        }
        return null;
    }

    /**
     * 将map中指定的key的参数转换为date类型
     * @param inParam map
     * @param strs 指定key
     */
    public static void changeDateByKey(Map<String, Object> inParam, String... strs) {
        if (inParam != null && !inParam.isEmpty() && strs != null && strs.length != 0) {
            for (String key : strs) {
                inParam.put(key, o2Date(inParam.get(key)));
            }
        }
    }

    /**
     * 将date转换为默认类型的时间串
     * @param date date
     * @return 时间串
     */
    public static String getDefaultDateStr(Date date) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(DATE_MODEL_8);
            return format.format(date);
        }
        return null;
    }

    /**
     * 将时date按指定格式转换为时间串
     * @param date date
     * @param type 时间格式
     * @return 时间串
     */
    public static String getDateStr(Date date, String type) {
        if (date != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(type);
                return format.format(date);
            } catch (Exception e) {
                return getDefaultDateStr(date);
            }
        }
        return null;
    }

    /**
     * 将原时间串转换为默认格式的时间串
     * @param str 原串
     * @return 新串
     */
    public static String dateStr2DefDateStr(String str) {
        Date date = getDate(str);
        return getDefaultDateStr(date);
    }

    /**
     * 将原时间串按指定格式转换为新时间串
     * @param str 原串
     * @param type 时间格式
     * @return 新串
     */
    public static String dateStr2OtherDateStr(String str, String type) {
        Date date = getDate(str);
        return getDateStr(date, type);
    }

}
