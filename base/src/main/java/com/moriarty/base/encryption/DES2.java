package com.moriarty.base.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class DES2 {
    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    public static String encryptDES(String encryptString, String encryptKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("UTF-8"), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));
        return Base64.encode(encryptedData);
    }

    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        byte[] byteMi = Base64.decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes("UTF-8"), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData, "UTF-8");
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return String
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

        String str = "{\"sid\":\"1121121\",\"mobile\":\"13901010101\",\"token\":\"kadflaf232r4lfklajdgfuawhla56y78890\"}";
        String value = DES2.encryptDES(str, "p2o_happ");

        System.out.println(value);
        String end = DES2.decryptDES(value, "p2o_happ");
        System.out.println(end);
//		Map<String,Object> map=com.msyd.wireless.common.StringUtil.parseJSON2Map(end);
//		System.out.println(map);

        //为了防止加号和空格在网络传输被转码，请将加密串执行以下语句再放在参数里
        value = value.replaceAll("\\+", "%2B");
        value = value.replaceAll("\\ ", "+");
        //System.out.println(value);
        String requestURL = "https://m.msyidai.com/userInvest/loanList?wap=true&partner=O2O&pars=" + value;
        System.out.println(requestURL);
    }
}
