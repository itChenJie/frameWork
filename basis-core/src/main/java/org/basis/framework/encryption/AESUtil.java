package org.basis.framework.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @Description AES 工具类
 * @Author ChenWenJie
 * @Data 2023/11/15 2:34 下午
 **/
public class AESUtil {

    /**
     * 生成密钥对象
     * @param customKey 密钥
     * @return
     */
    public static SecretKey generateAESKey(String customKey) {
        byte[] keyData = customKey.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "AES");
        return secretKeySpec;
    }

    /**
     * 使用随机16位的iv 加明文 生成加密串 在使用base64 进行转码
     * @param text 明文
     * @param customKey 密钥
     * @return
     */
    public static String encrypt(String text, String customKey) {
        // 生成16字节的随机IV
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        try {
            // 使用AES算法和随机生成的IV进行加密
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, generateAESKey(customKey), ivParameterSpec);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            // 将IV和密文合并
            byte[] combined  = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            return Base64.getEncoder().encodeToString(combined);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AES加密失败！");
        }
    }

    /**
     * 通过base 64 进行转码 截取密文前16位的iv 和密钥对密文进解密，
     * @param encryptedText
     * @param customKey
     * @return
     */
    public static String decrypt(String encryptedText, String customKey) {
        byte[] decryptedBytes = null;
        try {
            byte[] encryptedData  = Base64.getDecoder().decode(encryptedText);
            byte[] iv = new byte[16];
            System.arraycopy(encryptedData, 0, iv, 0, 16);
            byte[] encryptedContent = new byte[encryptedData.length - 16];
            System.arraycopy(encryptedData, 16, encryptedContent, 0, encryptedContent.length);
            SecretKeySpec secretKeySpec = new SecretKeySpec(customKey.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            decryptedBytes =  cipher.doFinal(encryptedContent);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AES解密失败！");
        }
        return new String(decryptedBytes);
    }
}
