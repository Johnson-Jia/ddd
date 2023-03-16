package com.tbc.ddd.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

/**
 * DES + base64
 *
 * @author Johnson
 */
@Slf4j
public class EncryptUtil {

    /**
     * md5加密
     *
     * @param str
     * @return
     */
    public final static String MD5(String str) {
        try {
            byte[] btInput = str.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < md.length; i++) {
                int val = ((int)md[i]) & 0xff;
                if (val < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 方法名称:TripleDES_CBC_Encrypt 功能描述: 经过封装的三重DES/CBC加密算法，如果包含中文，请注意编码。
     *
     * @param sourceBuf
     *            需要加密内容的字节数组。
     * @param deskey
     *            KEY 由24位字节数组通过SecretKeySpec类转换而成。
     * @param ivParam
     *            IV偏转向量，由8位字节数组通过IvParameterSpec类转换而成。
     * @return 加密后的字节数组
     * @throws Exception
     */
    public static byte[] TripleDES_CBC_Encrypt(byte[] sourceBuf, SecretKeySpec deskey, IvParameterSpec ivParam)
        throws Exception {
        // 使用DES对称加密算法的CBC模式加密
        Cipher encrypt = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, deskey, ivParam);
        byte[] cipherByte = encrypt.doFinal(sourceBuf, 0, sourceBuf.length);
        // 返回加密后的字节数组
        return cipherByte;
    }

    /**
     * 方法名称:TripleDES_CBC_Decrypt 功能描述: 经过封装的三重DES / CBC解密算法
     *
     * @param sourceBuf
     *            需要解密内容的字节数组
     * @param deskey
     *            KEY 由24位字节数组通过SecretKeySpec类转换而成。
     * @param ivParam
     *            IV偏转向量，由6位字节数组通过IvParameterSpec类转换而成。
     * @return 解密后的字节数组
     * @throws Exception
     */
    public static byte[] TripleDES_CBC_Decrypt(byte[] sourceBuf, SecretKeySpec deskey, IvParameterSpec ivParam)
        throws Exception {
        // 获得Cipher实例，使用CBC模式。
        Cipher decrypt = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        // 初始化加密实例，定义为解密功能，并传入密钥，偏转向量
        decrypt.init(Cipher.DECRYPT_MODE, deskey, ivParam);
        byte[] cipherByte = decrypt.doFinal(sourceBuf, 0, sourceBuf.length);
        // 返回解密后的字节数组
        return cipherByte;
    }

    /**
     * 方法名称:DES_CBC_Encrypt 功能描述: 经过封装的DES/CBC加密算法，如果包含中文，请注意编码。
     *
     * @param sourceBuf
     *            需要加密内容的字节数组。
     * @param deskey
     *            KEY 由8位字节数组通过SecretKeySpec类转换而成。
     * @param ivParam
     *            IV偏转向量，由8位字节数组通过IvParameterSpec类转换而成。
     * @return 加密后的字节数组
     * @throws Exception
     */
    public static byte[] DES_CBC_Encrypt(byte[] sourceBuf, SecretKeySpec deskey, IvParameterSpec ivParam)
        throws Exception {
        // 使用DES对称加密算法的CBC模式加密
        Cipher encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, deskey, ivParam);
        byte[] cipherByte = encrypt.doFinal(sourceBuf, 0, sourceBuf.length);
        // 返回加密后的字节数组
        return cipherByte;
    }

    /**
     * 方法名称:DES_CBC_Decrypt 功能描述: 经过封装的DES/CBC解密算法。
     *
     * @param sourceBuf
     *            需要解密内容的字节数组
     * @param deskey
     *            KEY 由8位字节数组通过SecretKeySpec类转换而成。
     * @param ivParam
     *            IV偏转向量，由6位字节数组通过IvParameterSpec类转换而成。
     * @return 解密后的字节数组
     * @throws Exception
     */
    public static byte[] DES_CBC_Decrypt(byte[] sourceBuf, SecretKeySpec deskey, IvParameterSpec ivParam)
        throws Exception {
        // 获得Cipher实例，使用CBC模式。
        Cipher decrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 初始化加密实例，定义为解密功能，并传入密钥，偏转向量
        decrypt.init(Cipher.DECRYPT_MODE, deskey, ivParam);
        byte[] cipherByte = decrypt.doFinal(sourceBuf, 0, sourceBuf.length);
        // 返回解密后的字节数组
        return cipherByte;
    }

    /**
     * 方法名称:MD5Hash 功能描述: MD5，进行了简单的封装，以适用于加，解密字符串的校验。
     *
     * @param buf
     *            需要MD5加密字节数组。
     * @param offset
     *            加密数据起始位置。
     * @param length
     *            需要加密的数组长度。
     * @return
     * @throws Exception
     */
    public static byte[] MD5Hash(byte[] buf, int offset, int length) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(buf, offset, length);
        return md.digest();
    }

    /**
     * 方法名称:byte2hex 功能描述: 字节数组转换为二行制表示
     *
     * @param inStr
     *            需要转换字节数组。
     * @return 字节数组的二进制表示。
     */
    public static String byte2hex(byte[] inStr) {
        String stmp;
        StringBuilder out = new StringBuilder(inStr.length * 2);
        for (int n = 0; n < inStr.length; n++) {
            // 字节做"与"运算，去除高位置字节 11111111
            stmp = Integer.toHexString(inStr[n] & 0xFF);
            if (stmp.length() == 1) {
                // 如果是0至F的单位字符串，则添加0
                out.append("0" + stmp);
            } else {
                out.append(stmp);
            }
        }
        return out.toString();
    }

    /**
     * 方法名称:addMD5 功能描述: MD校验码 组合方法，前16位放MD5Hash码。 把MD5验证码byte[]，加密内容byte[]组合的方法。
     *
     * @param md5Byte
     *            加密内容的MD5Hash字节数组。
     * @param bodyByte
     *            加密内容字节数组
     * @return 组合后的字节数组，比加密内容长16个字节。
     */
    public static byte[] addMD5(byte[] md5Byte, byte[] bodyByte) {
        int length = bodyByte.length + md5Byte.length;
        byte[] resutlByte = new byte[length];
        // 前16位放MD5Hash码
        for (int i = 0; i < length; i++) {
            if (i < md5Byte.length) {
                resutlByte[i] = md5Byte[i];
            } else {
                resutlByte[i] = bodyByte[i - md5Byte.length];
            }
        }
        return resutlByte;
    }

    /**
     * 方法名称:getKeyIV 功能描述:
     *
     * @param encryptKey
     * @param key
     * @param iv
     */
    public static void getKeyIV(String encryptKey, byte[] key, byte[] iv) {
        // 密钥Base64解密
        byte[] buf = Base64.getDecoder().decode(encryptKey);
        // 前8位为key
        int i;
        for (i = 0; i < key.length; i++) {
            key[i] = buf[i];
        }
        // 后8位为iv向量
        for (i = 0; i < iv.length; i++) {
            iv[i] = buf[i + 8];
        }
    }

    /**
     * <li>方法名称:encrypt</li>
     * <li>加密方法
     *
     * @param content
     *            需要加密的消息字符串
     * @return 加密后的字符串
     */
    public static String encryptDES(String key, String content) {
        key = MD5(key);
        // 取需要加密内容的utf-8编码。
        byte[] encrypt = content.getBytes(StandardCharsets.UTF_8);
        if (encrypt == null) {
            return null;
        }
        // 取MD5Hash码，并组合加密数组
        byte[] md5Hasn = null;
        try {
            md5Hasn = MD5Hash(encrypt, 0, encrypt.length);
        } catch (Exception e) {
            log.error("", e);
        }
        if (md5Hasn == null) {
            return null;
        }
        // 组合消息体
        byte[] totalByte = addMD5(md5Hasn, encrypt);

        // 取密钥和偏转向量
        byte[] keyBytes = new byte[8];
        byte[] iv = new byte[8];
        getKeyIV(key, keyBytes, iv);
        SecretKeySpec deskey = new SecretKeySpec(keyBytes, "DES");
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        // 使用DES算法使用加密消息体
        byte[] temp = null;
        try {
            temp = DES_CBC_Encrypt(totalByte, deskey, ivParam);
        } catch (Exception e) {
            log.error("", e);
        }

        // 使用Base64加密后返回
        return new String(Base64.getEncoder().encode(temp));
    }

    /**
     * <li>方法名称:decrypt</li>
     * <li>功能描述:
     *
     * <pre>
     * 解密方法
     * </pre>
     *
     * </li>
     *
     * @param content
     *            需要解密的消息字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decryptDES(String key, String content) throws Exception {
        key = MD5(key);
        // base64解码
        byte[] encBuf = Base64.getDecoder().decode(content);
        // 取密钥和偏转向量
        byte[] keyBytes = new byte[8];
        byte[] iv = new byte[8];
        getKeyIV(key, keyBytes, iv);
        SecretKeySpec deskey = new SecretKeySpec(keyBytes, "DES");
        IvParameterSpec ivParam = new IvParameterSpec(iv);

        // 使用DES算法解密
        byte[] temp = null;
        try {
            temp = DES_CBC_Decrypt(encBuf, deskey, ivParam);
        } catch (Exception e) {
            log.error("", e);
        }
        if (temp == null) {
            return null;
        }
        // 进行解密后的md5Hash校验
        byte[] md5Hash = null;
        try {
            md5Hash = MD5Hash(temp, 16, temp.length - 16);
        } catch (Exception e) {
            log.error("", e);
        }
        if (md5Hash == null) {
            return null;
        }
        // 进行解密校检
        for (int i = 0; i < md5Hash.length; i++) {
            if (md5Hash[i] != temp[i]) {
                // System.out.println(md5Hash[i] + "MD5校验错误。" + temp[i]);
                throw new Exception("MD5校验错误。");
            }
        }
        // 返回解密后的数组，其中前16位MD5Hash码要除去。
        return new String(temp, 16, temp.length - 16, StandardCharsets.UTF_8);
    }

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) {
        String str = null;
        try {
            byte[] decode = Base64.getDecoder().decode(key);
            str = new String(decode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("", e);
        }
        return str;
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key) {
        String encode = null;
        try {
            byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
            encode = new String(Base64.getEncoder().encode(bytes));
        } catch (Exception e) {
            log.error("", e);
        }
        return encode;
    }

    /**
     * <li>方法名称:addMD5</li>
     * <li>功能描述:
     * <p>
     * MD校验码 组合方法，前16位放MD5Hash码。 把MD5验证码byte[]，加密内容byte[]组合的方法。
     *
     * @return 组合后的字节数组，比加密内容长16个字节。
     */
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }

    public static String SHA(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }

    public static void main(String[] args) throws Exception {}
}
