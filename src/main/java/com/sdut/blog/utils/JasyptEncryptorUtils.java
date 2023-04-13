package com.sdut.blog.utils;

import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author 24699
 */
public final class JasyptEncryptorUtils {

    private static final String salt = "lzk123@gp.com";

    private static BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();

    static {
        basicTextEncryptor.setPassword(salt);
    }

    private JasyptEncryptorUtils(){}
    /**
     * 明文加密
     * @param plaintext
     * @return
     */
    public static String encode(String plaintext){
        String ciphertext = basicTextEncryptor.encrypt(plaintext);
//        System.out.println("明文字符串：" + plaintext);
//        System.out.println("加密后字符串：" + ciphertext);
        return ciphertext;
    }

    /**
     * 解密
     * @param ciphertext
     * @return
     */
    public static String decode(String ciphertext){
        ciphertext = "ENC(" + ciphertext + ")";
        if (PropertyValueEncryptionUtils.isEncryptedValue(ciphertext)){
            String plaintext = PropertyValueEncryptionUtils.decrypt(ciphertext,basicTextEncryptor);
            return plaintext;
        }
        System.out.println("解密失败");
        return "";
    }
}
