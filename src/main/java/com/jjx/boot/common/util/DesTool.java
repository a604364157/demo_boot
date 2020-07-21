package com.jjx.boot.common.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 新增DES加解密函数
 * <p>
 * 备注：该加解密方式采用CBC模式PKCS5Padding
 *
 * @author jiangjx
 */
public class DesTool {

    private static String def = "si-te-th";
    private static byte[] iv1 = def.getBytes();

    public static String decrypt(String decryptString, String decryptKey) {
        IvParameterSpec iv = new IvParameterSpec(iv1);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return new String(cipher.doFinal(Base64.decodeBase64(decryptString.getBytes())));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException("DES加密异常，可能原因：密钥与向量位数异常！");
        }
    }

    public static String encrypt(String encryptString, String encryptKey) {
        IvParameterSpec iv = new IvParameterSpec(iv1);
        DESKeySpec dks;
        try {
            dks = new DESKeySpec(encryptKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return new String(Base64.encodeBase64(cipher.doFinal(encryptString.getBytes()), false));
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException e) {
            throw new RuntimeException("DES解密异常，可能原因：密钥错误！");
        }
    }

    public static void setDef(String def) {
        DesTool.def = def;
    }

    public static void main(String[] args) {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String key = "a0d0m0in";
        String b = encrypt(a, key);
        String c = decrypt(b, key);
        System.out.println(b);
        System.out.println(c);
    }

}
