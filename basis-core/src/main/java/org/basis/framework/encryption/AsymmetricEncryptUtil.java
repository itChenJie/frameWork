package org.basis.framework.encryption;

import org.apache.commons.codec.binary.Base64;
import org.basis.framework.error.ServiceException;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 非对称加密 (RSA（比较常用）、ECC、Diffie-Hellman、El Gamal、DSA)
 *优点：
 * 安全高(几乎很难破解)
 *
 *缺点：
 * 加解密相对速度慢；
 * 密钥长；
 * 计算量大、效率低；
 * @Author ChenWenJie
 * @Data 2022/09/19 1:23 下午
 **/
public class AsymmetricEncryptUtil {
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    /**
     * RSA加密明文最大长度
     */
    private static final int MAX_ENCRYPT_LENGTH = 1024;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * 生成密钥对(公钥和私钥)
     */
    public static Map<String, Object> genKeyPair() {
        return genKeyPair(KEY_ALGORITHM);
    }

    private static String CHARSET_NAME = "UTF-8";

    /**
     * 生成密钥对(公钥和私钥)
     * @param keyAlgorithm 加密算法
     */
    public static Map<String, Object> genKeyPair(String keyAlgorithm) {
        final Map<String, Object> keyMap = new HashMap<>(2);
        try {
            final KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(keyAlgorithm);
            //加密明文最大长度
            keyPairGen.initialize(MAX_ENCRYPT_LENGTH);
            final KeyPair keyPair = keyPairGen.generateKeyPair();
            final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServiceException("生成公钥和私钥失败,算法不存在");
        }
        return keyMap;
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return 加密密文
     */
    public static String encryptByPublicKey(String data, String publicKey){
       return encryptByPublicKey(data,publicKey,KEY_ALGORITHM);
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @param keyAlgorithm 加密算法
     * @return 加密密文
     */
    public static String encryptByPublicKey(String data, String publicKey,String keyAlgorithm){
        try {
            byte[] encryptData =  data.getBytes(Charset.forName(CHARSET_NAME));
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            PublicKey pubKey = KeyFactory.getInstance(keyAlgorithm).generatePublic(new X509EncodedKeySpec(keyBytes));
            Cipher cipher = Cipher.getInstance(pubKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            int inputLen = encryptData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptData, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return Base64.encodeBase64String(encryptedData);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("公钥加密失败！");
        }
    }

    /**
     * 私钥解密
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return 明文
     */
    public static String decryptByPrivateKey(String data, String privateKey){
        return decryptByPrivateKey(data,privateKey,KEY_ALGORITHM);
    }
    /**
     * 私钥解密
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @param keyAlgorithm 加密算法
     * @return 明文
     */
    public static String decryptByPrivateKey(String data, String privateKey,String keyAlgorithm){
        try {
            byte[] decryptData = Base64.decodeBase64(data.getBytes("UTF-8"));
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            PrivateKey privKey = KeyFactory.getInstance(keyAlgorithm).generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
            Cipher cipher = Cipher.getInstance(privKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            int inputLen = decryptData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(decryptData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(decryptData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("私钥解密失败！");
        }
    }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return 加密密文
     */
    public static String encryptByPrivateKey(String data, String privateKey){
        return encryptByPrivateKey(data,privateKey,KEY_ALGORITHM);
    }
    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @param keyAlgorithm 加密算法
     * @return 加密密文
     */
    public static String encryptByPrivateKey(String data, String privateKey,String keyAlgorithm){
        try {
            byte[] encryptData = data.getBytes(Charset.forName(CHARSET_NAME));
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = encryptData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptData, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return Base64.encodeBase64String(encryptedData);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("私钥加密失败！");
        }
    }

    /**
     * 公钥解密
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return 明文
     */
    public static String decryptByPublicKey(String data, String publicKey)  {
        return decryptByPublicKey(data, publicKey, KEY_ALGORITHM);
    }

    /**
     * 公钥解密
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param keyAlgorithm 加密算法
     * @return 明文
     */
    public static String decryptByPublicKey(String data, String publicKey,String keyAlgorithm) {
       try {
           byte[] decryptData = Base64.decodeBase64(data);
           byte[] keyBytes = Base64.decodeBase64(publicKey);
           X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
           KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
           Key publicK = keyFactory.generatePublic(x509KeySpec);
           Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
           cipher.init(Cipher.DECRYPT_MODE, publicK);
           int inputLen = decryptData.length;
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           int offSet = 0;
           byte[] cache;
           int i = 0;
           while (inputLen - offSet > 0) {
               if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                   cache = cipher.doFinal(decryptData, offSet, MAX_DECRYPT_BLOCK);
               } else {
                   cache = cipher.doFinal(decryptData, offSet, inputLen - offSet);
               }
               out.write(cache, 0, cache.length);
               i++;
               offSet = i * MAX_DECRYPT_BLOCK;
           }
           byte[] decryptedData = out.toByteArray();
           out.close();
           return new String(decryptedData);
       }catch (Exception e){
           e.printStackTrace();
           throw new ServiceException("解密失败！");
       }
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return 签名
     */
    public static String sign(String data, final String privateKey) {
        return sign(data,privateKey,KEY_ALGORITHM,SIGNATURE_ALGORITHM);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @param keyAlgorithm  加密算法
     * @param signatureAlgorithm 签名算法
     * @return 签名
     */
    public static String sign(String data, final String privateKey,String keyAlgorithm,String signatureAlgorithm) {
        byte[] signData = Base64.decodeBase64(data);
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(keyAlgorithm);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initSign(privateK);
            signature.update(signData);
            return Base64.encodeBase64String(signature.sign());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServiceException("密钥算法不存在！");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new ServiceException("无效的密钥规范！");
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new ServiceException("签名异常！");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new ServiceException("无效key异常！");
        }
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return boolean
     */
    public static boolean verify(String data, String publicKey, String sign)  {
        byte[] verifyData = Base64.decodeBase64(data);
        Signature signature = null;
        try {
            signature = getSignatureByDataAndPublicKey(verifyData, publicKey);
            return signature.verify(Base64.decodeBase64(sign));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServiceException("密钥算法不存在！");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new ServiceException("无效的密钥规范！");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new ServiceException("无效key异常！");
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new ServiceException("签名异常！");
        }
    }

    /**
     * 通过数据和公钥获取签名
     * @param data
     * @param publicKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static Signature getSignatureByDataAndPublicKey(byte[] data, String publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        return getSignatureByDataAndPublicKey(data, publicKey,KEY_ALGORITHM,SIGNATURE_ALGORITHM);
    }


    public static Signature getSignatureByDataAndPublicKey(byte[] data, String publicKey,String keyAlgorithm,String signatureAlgorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initVerify(publicK);
        signature.update(data);
        return signature;
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return 私钥
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return 公钥
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static void main(String[] args)  {
        String data = "13fafasf4";
        Map<String, Object> merKeyMap = AsymmetricEncryptUtil.genKeyPair();
        String merPublicKey = AsymmetricEncryptUtil.getPublicKey(merKeyMap);
        String merPrivateKey = AsymmetricEncryptUtil.getPrivateKey(merKeyMap);
        System.out.println("公钥: " + merPublicKey);
        System.out.println("私钥： " + merPrivateKey);
        String encryptByPublic = AsymmetricEncryptUtil.encryptByPublicKey(data, merPublicKey);
        String decryptByPrivate = AsymmetricEncryptUtil.decryptByPrivateKey(encryptByPublic, merPrivateKey);
        System.out.println("公钥加密：" + encryptByPublic);
        System.out.println("私钥解密：" + decryptByPrivate);
        String encryptByPrivate = AsymmetricEncryptUtil.encryptByPrivateKey(data, merPrivateKey);
        String decryptByPublic = AsymmetricEncryptUtil.decryptByPublicKey(encryptByPrivate, merPublicKey);
        System.out.println("私钥加密：" + encryptByPublic);
        System.out.println("公钥解密：" + decryptByPublic);
//        这里只演示单方向的加密 实现数字签名
        String signature = AsymmetricEncryptUtil.sign(encryptByPrivate, merPrivateKey);
        boolean verify = AsymmetricEncryptUtil.verify(encryptByPrivate, merPublicKey, signature);
        System.out.println("私钥对加密信息生成数字签名：" + signature);
        System.out.println("校验结果：" + verify);
    }
}
