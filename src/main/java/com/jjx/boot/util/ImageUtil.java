package com.jjx.boot.util;

import org.springframework.util.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class ImageUtil {

    public static String image2Base(String imgPath) {
        byte[] data = new byte[0];
        try (InputStream is = new FileInputStream(imgPath)) {
            data = FileUtils.input2byte(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64Utils.encodeToString(data);
    }

    public static boolean base2Image(String imgStr, String outPath) {
        try (OutputStream out = new FileOutputStream(outPath)) {
            byte[] data = Base64Utils.decodeFromString(imgStr);
            out.write(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
   /**
     * 压缩图片
     * 本函数会对图片的分辨率和像素双重压缩,以达到合适的图片大小
     * 理论上不会让图片失真
     * 主要降低图片质量,次要降低图片大小
     *
     * @param imageBytes  原图片流
     * @param desFileSize 目标大小,单位KB
     * @param imageId     图片ID(没什么用,做日志追踪)
     * @return 压缩后图片流
     * @throws IOException IO异常
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize, String imageId) throws IOException {
        if (imageBytes == null || imageBytes.length <= 0 || imageBytes.length < desFileSize * 1024) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / 1024);
        while (imageBytes.length > desFileSize * 1024) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
            Thumbnails.of(inputStream).scale(accuracy).outputQuality(accuracy).toOutputStream(outputStream);
            imageBytes = outputStream.toByteArray();
        }
        log.info("【图片压缩】imageId={} | 图片原大小={}kb | 压缩后大小={}kb", imageId, srcSize / 1024, imageBytes.length / 1024);
        return imageBytes;
    }

    /**
     * 根据图片大小返回建议的压缩比
     *
     * @param size 图片大小
     * @return 压缩比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

}
