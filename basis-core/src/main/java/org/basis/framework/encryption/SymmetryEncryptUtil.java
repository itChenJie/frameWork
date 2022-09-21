package org.basis.framework.encryption;

import org.apache.commons.codec.binary.Base64;
import org.basis.framework.error.ServiceException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * @Description 对称加密(单密钥加密 AES（比较常用）、DES、、3DES、DESX、Blowfish、IDEA、RC4、RC5、RC6)
 *
 * 优点：
 *  生成密钥的算法公开；
 *  计算量小；
 *  加密速度快、加密效率高；
 *  密钥较短；
 * 缺点：
 *  密钥需要传递，难以确保密钥安全性；
 *  不能作为身份验证，密钥发放困难，安全性得不到保证；
 * @Author ChenWenJie
 * @Data 2022/09/19 1:23 下午
 **/
public class SymmetryEncryptUtil {
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加密/解密算法-工作模式-填充模
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES 加密操作
     * @param password 加密密码
     * @param key      密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(final String password, final String key) {
        return encrypt(password, key,DEFAULT_CIPHER_ALGORITHM,KEY_ALGORITHM);
    }

    /**
     * 加密操作
     * @param password 加密密码
     * @param key 密钥
     * @param encryptAlgorithm 加密算法
     * @param keyAlgorithm 密钥算法
     * @return
     */
    public static String encrypt(final String password, final String key,String encryptAlgorithm,String keyAlgorithm){
        try {
            //根据指定的加密算法 创建密码器
            final Cipher cipher = Cipher.getInstance(encryptAlgorithm);
            //获取要加密内容的字节
            final byte[] byteContent = password.getBytes(StandardCharsets.UTF_8);
            //利用加密密钥将密码器对象初始化：参数一:加密/解密模式，参数二：加密后的钥匙（密钥）
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), keyAlgorithm));
            //加密
            final byte[] result = cipher.doFinal(byteContent);
            //防止乱码，使用Base64编码
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("加密 error ");
        }
    }

    /**
     * AES 解密操作
     * @param password 解密密码
     * @param key      密钥
     * @return 明文
     */
    public static String decrypt(String password, String key) {
        return decrypt(password,key,DEFAULT_CIPHER_ALGORITHM,KEY_ALGORITHM);
    }

    /**
     * 解密操作
     * @param password 解密密码
     * @param key 密钥
     * @param encryptAlgorithm 加密算法
     * @param keyAlgorithm 密钥算法
     * @return
     */
    public static String decrypt(String password, String key,String encryptAlgorithm,String keyAlgorithm) {
        try {
            //创建密码器
            final Cipher cipher = Cipher.getInstance(encryptAlgorithm);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), keyAlgorithm));
            //执行操作
            final byte[] result = cipher.doFinal(Base64.decodeBase64(password));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("解密 error");
        }
    }

    /**
     * 获取AES秘钥
     *
     * @return
     */
    public static String getKey() {
        return getKey(KEY_ALGORITHM);
    }

    /**
     * 获取AES秘钥
     * @param keyAlgorithm 密钥算法
     * @return
     */
    public static String getKey(String keyAlgorithm) {
        try {
            final KeyGenerator kg = KeyGenerator.getInstance(keyAlgorithm);
            kg.init(128);
            final SecretKey sk = kg.generateKey();
            final byte[] b = sk.getEncoded();
            return byteToHexString(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServiceException("加密算法不存在");
        }
    }

    /**
     * byte数组转化为16进制字符串
     *
     * @param bytes
     * @return
     */
    private static String byteToHexString(final byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (final byte aByte : bytes) {
            final String strHex = Integer.toHexString(aByte);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0").append(strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String key = getKey();
        String encryptPassword = encrypt("深圳佛山东莞213s//-za1-'\\", key);
        String decryptPassword = decrypt(encryptPassword, key);
        System.out.println("密钥:" + key);
        System.out.println("AES加密:" + encryptPassword);
        System.out.println("AES解密:" + decryptPassword);
    }
}
