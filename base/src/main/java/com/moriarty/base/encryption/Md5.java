package com.moriarty.base.encryption;


import com.moriarty.base.sc.Keys;

public class Md5 {
    public static String getMd5(String s) {
//        byte[] digest = null;
//        try {
//            digest = MessageDigest.getInstance("md5").digest(s.getBytes());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return new BigInteger(1, digest).toString(16).toLowerCase();

        return getMD5(s.getBytes());
    }


    public static String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


    /**
     * 加盐的md5值
     */
    public static String encodeByMd5AndSalt(String s) {
        String privateKey = Keys.SIGN_MD5_KEY;
        StringBuilder query = new StringBuilder(privateKey);
        query.append(s);
        query.append(privateKey);
        return getMd5(query.toString());
    }
}
