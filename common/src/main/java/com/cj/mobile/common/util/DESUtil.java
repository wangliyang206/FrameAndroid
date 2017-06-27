package com.cj.mobile.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加解密工具
 */
public class DESUtil {
    public DESUtil() {
    }

    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * DES加密码
     *
     * @param encryptString value
     * @param encryptKey    key
     * @return 加好密的串
     */
    public static String encryptDES(String encryptString, String encryptKey) {
        try {
//          IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("UTF-8"), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));

            return Base64Util.encode(encryptedData);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * DES解密
     *
     * @param decryptString value
     * @param decryptKey    key
     * @return 解好密的串
     */
    @SuppressWarnings("static-access")
    public static String decryptDES(String decryptString, String decryptKey) {
        try {
            byte[] byteMi = new Base64Util().decode(decryptString);
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
//            IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes("UTF-8"), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte decryptedData[] = cipher.doFinal(byteMi);

            return new String(decryptedData);
        } catch (Exception ex) {
            return "";
        }
    }
}
