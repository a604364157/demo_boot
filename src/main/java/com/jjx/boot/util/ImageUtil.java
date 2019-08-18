package com.jjx.boot.util;

import org.springframework.util.Base64Utils;

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

}
