package com.jjx.boot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
public class MailUtil {

    private static Logger log = LoggerFactory.getLogger(MailUtil.class);

    private static final String HOST = "smtp.si-tech.com.cn";
    private static final Integer PORT = 25;
    private static final String USERNAME = "jiangjx@si-tech.com.cn";
    private static final String PASSWORD = "jjx5824000";
    private static final String EMAIL_FORM = "jiangjx@si-tech.com.cn";
    private static final String TIMEOUT = "1000";
    private static final String PERSONAL = "jiangjx";
    private static JavaMailSenderImpl mailSender = createMailSender();

    /**
     * 邮件发送器
     *
     * @return 配置好的工具
     */
    private static JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.TIMEOUT", TIMEOUT);
        p.setProperty("mail.smtp.auth", "false");
        sender.setJavaMailProperties(p);
        return sender;
    }

    /**
     * 邮件发送
     *
     * @param to      接收邮箱地址
     * @param html    邮件内容
     * @param subject 邮件主题
     * @param files   附件
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static void sendMail(String to, String html, String subject, List<File> files) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAIL_FORM, PERSONAL);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        if (files != null && !files.isEmpty()) {
            files.forEach(item -> {
                try {
                    messageHelper.addAttachment(item.getName(), item);
                } catch (MessagingException e) {
                    log.error("添加附件失败");
                }
            });
        }
        mailSender.send(mimeMessage);
    }

    public static void main(String[] args) {
        Date begin = DateConst.getDate("20190629175959");
        Date end = DateConst.getDate("20190630180000");
        String sql = "SELECT PARTY_ID, ORG_NAME FROM PARTY_ORG WHERE ORG_USCC IS NOT NULL AND CREATE_DATE BETWEEN ? AND ?";
        List<Object> param = new ArrayList<>();
        param.add(begin);
        param.add(end);
        List<Map<String, Object>> ret = DBUtil.qryDataForList(sql, param);
        if (ret != null) {
            param.clear();
            List<String> param2 = ret.stream().map(item -> StringTool.o2s(item.get("PARTY_ID"))).collect(Collectors.toList());
            StringBuilder param3 = new StringBuilder();
            for (String s : param2) {
                param3.append(",").append(s);
            }
            String param4 = "("+param3.toString().substring(1)+")";
            param.add(param4);
            String sql2 = "SELECT CUST_ID FROM customer WHERE PARTY_ID IN ?";
            List<Map<String, Object>> ret2 = DBUtil.qryDataForList(sql2, param);
            System.out.println(ret2);
        }






        List<File> files = new ArrayList<>();
        files.add(ExcelUtil.workBook2File(ExcelUtil.createExcel2007(DBUtil.qryDataForList(sql, new ArrayList<>()), false), "数据统计.xls"));
        files.add(new File("E:\\WorkSpaces\\boot\\voice\\1560129660037.mp3"));
/*        try {
            sendMail("jiangjx@si-tech.com.cn", "你好，统计数据见附件(本邮件通过定时任务自动发送，请勿回复)", "工单数据统计", files);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }

}
