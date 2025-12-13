package com.github.axinger.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.util.ObjectUtils;

/**
 * 加解密工具类
 */
public class EncrypDecryptUtil {

    /**
     * 加密
     *
     * @param password 加密时使用的密码
     * @param value    需要加密的值
     * @return
     */
    public static String encypt(String password, String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        return encryptor.encrypt(value);
    }

    /**
     * 解密
     *
     * @param password 解密时使用的密码
     * @param value    需要解密的值
     * @return
     */
    public static String decypt(String password, String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setPassword(password);
        return decryptor.decrypt(value);
    }

    public static void main(String[] args) {


    }

}
