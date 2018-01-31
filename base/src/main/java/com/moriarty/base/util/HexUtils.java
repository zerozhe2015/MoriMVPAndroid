package com.moriarty.base.util;

public class HexUtils {


    private static String myByteToHex(byte[] byteArray) {
        char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] charArray = new char[byteArray.length << 1];
        for (int i = 0, k = 0; i < byteArray.length; i++) {
            charArray[k++] = digit[(byteArray[i] >>> 4) & 0xf];
            charArray[k++] = digit[byteArray[i] & 0xf];
        }
        String result = new String(charArray);
        return result;
    }

    public static String byteHEX(byte ib) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data
     *            要转换的字节数组。
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
     * @param hexString
     *            16进制表示的字符串
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

    private static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    private static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    private static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

}
