package com.moriarty.base.encryption;

import com.moriarty.base.sc.Keys;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * DesUtils des加解密
 */
public class DesUtils {

    public static String PASSWORD_CRYPT_KEY = Keys.DES_KEY;//"z&-ls0n!";
    public static String PROMOTION_CRYPT_KEY = "bZ4096AKjqR62Np10e3r9Oxv25L1oMPe";
    public static String PROMOTION_CRYPT_KEY_H5 = "46f99e4d567549d1a5035d7cbf450852";
    public static final String ALGORITHM_DES = "DES";
//    private static Key key = null;

    public DesUtils() {
//        try {
//            key = getDESKey(PASSWORD_CRYPT_KEY.getBytes());
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 返回可逆算法DES的密钥
     *
     * @param key 前8字节将被用来生成密钥。
     * @return 生成的密钥
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static Key getDESKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        DESKeySpec des = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
        return keyFactory.generateSecret(des);
    }

    /**
     * 根据指定的密钥及算法，将字符串进行解密。
     *
     * @param data 要进行解密的数据，它是由原来的byte[]数组转化为字符串的结果。
     * @return 解密后的结果。它由解密的byte[]重新创建为String对象。如果解密失败，将返回null。
     */
    public static String decrypt(String cryptKey, String data) {
        Key key = null;
        try {
            key = getDESKey(cryptKey.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            String result = new String(cipher.doFinal(StringKit.hexStringToBytes(data)), "utf8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据指定的密钥及算法，将字符串进行解密。
     *
     * @param data 要进行解密的数据，它是由原来的byte[]数组转化为字符串的结果。
     * @return 解密后的结果。它由解密后的byte[]重新创建为String对象。如果解密失败，将返回null。
     */
    public static String decrypt(String data) {
        Key key = null;

        try {
            key = getDESKey(PASSWORD_CRYPT_KEY.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            String result = new String(cipher.doFinal(StringKit.hexStringToBytes(data)), "utf8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据指定的密钥及算法对指定字符串进行可逆加密。
     *
     * @param data 要进行加密的字符串。
     * @return 加密后的结果将由byte[]数组转换为16进制表示的数组。如果加密过程失败，将返回null。
     */
    public static String encrypt(String data) {
        Key key=null;
        try {
            key = getDESKey(PASSWORD_CRYPT_KEY.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return StringKit.bytesToHexString(cipher.doFinal(data.getBytes("utf8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据指定的密钥及算法对指定字符串进行可逆加密。
     *
     * @param data 要进行加密的字符串。
     * @return 加密后的结果将由byte[]数组转换为16进制表示的数组。如果加密过程失败，将返回null。
     */
    public static String encrypt(String cryptKey,String data) {
        Key key=null;
        try {
            key = getDESKey(cryptKey.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return StringKit.bytesToHexString(cipher.doFinal(data.getBytes("utf8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class StringKit {

        /**
         * byte[]数组转换为16进制的字符串。
         *
         * @param data 要转换的字节数组。
         * @return 转换后的结果。
         */
        public static final String bytesToHexString(byte[] data) {
            StringBuilder valueHex = new StringBuilder();
            for (int i = 0, tmp; i < data.length; i++) {
                tmp = data[i] & 0xff;
                if (tmp < 16) {
                    valueHex.append(0);
                }
                valueHex.append(Integer.toHexString(tmp));
            }
            return valueHex.toString();
        }

        /**
         * 16进制表示的字符串转换为字节数组。
         *
         * @param hexString 16进制表示的字符串
         * @return byte[] 字节数组
         */
        public static byte[] hexStringToBytes(String hexString) {
            if (hexString == null || hexString.equals("")) {
                return null;
            }
            hexString = hexString.toUpperCase();
            char[] hexChars = hexString.toCharArray();
            int length = hexString.length();
            byte[] d = new byte[length >>> 1];
            for (int n = 0; n < length; n += 2) {
                String item = new String(hexChars, n, 2);
                // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
                d[n >>> 1] = (byte) Integer.parseInt(item, 16);
            }
            return d;
        }
    }
}
