package com.jjx.boot.common.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Administrator
 */
public class AesTool {

    /**
     * 偏移量（向量）
     * AES 为16bytes. DES 为8bytes
     */
    private static final String VIPARA = "1234567876543210";
    /**
     * 编码方式
     */
    private static final String CODE_TYPE = "UTF-8";
    /**
     * 填充类型
     */
    private static final String AES_TYPE = "AES/CBC/PKCS5Padding";
    /**
     * 密钥
     * AES固定格式为128/192/256 bits.即：16/24/32bytes
     */
    private static final String AES_KEY = "1111222233334444";

    /**
     * 加密
     *
     * @param cleartext 明文
     * @return 密文
     */
    public static String encrypt(String cleartext) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(cleartext.getBytes(CODE_TYPE));
            return new String(Base64.encodeBase64(encryptedData));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密
     *
     * @param encrypted 密文
     * @return 明文
     */
    public static String decrypt(String encrypted) {
        try {
            byte[] byteMi = Base64.decodeBase64(encrypted);
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte[] decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData, CODE_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
