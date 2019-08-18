package com.jjx.boot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class FileUtils {

    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 文件上传工具类
     *
     * @param input    文件输入流
     * @param path     存储路径
     * @param fileName 文件名
     */
    public static void uploadFile(InputStream input, String path, String fileName) {
        if (StringUtils.isEmpty(input)) {
            throw new RuntimeException("上传文件为空");
        }
        if (StringUtils.isEmpty(path)) {
            throw new RuntimeException("上传路径配置错误");
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("保存文件名错误");
        }
        File dir = new File(path);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                log.debug("生成目录成功");
            }
        }
        try (BufferedInputStream bis = new BufferedInputStream(input);
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path + File.separator + fileName))) {
            int len;
            byte[] bytes = new byte[1024];
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件读写失败");
        }
    }

    public static InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    public static byte[] input2byte(InputStream inStream) {
        int one = 1024;
        byte[] buff = new byte[one];
        int rc;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            while ((rc = inStream.read(buff, 0, one)) > 0) {
                bos.write(buff, 0, rc);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

}
