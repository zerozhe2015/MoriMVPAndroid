package com.moriarty.base.util;


import android.text.SpannableString;
import android.text.TextUtils;
import android.widget.TextView;

import com.moriarty.base.widget.text.SmallTextSpan;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 常用文本操作
 */
public class TextToolkit {


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 判断TextView输入是否为空
     */
    public static boolean isTextEmpty(TextView tv) {
        return TextUtils.isEmpty(tv.getText());
    }


    /**
     * 判断字符串是否为Null，""， "null"的大小写变体
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return isEmpty(str) || str.equalsIgnoreCase("null");
    }


    /**
     * 手机号码验证
     *
     * @param mobile
     * @return
     */
    public static boolean isMobilePhoneNo(String mobile) {
        return mobile.matches("1\\d{10}");
    }

    /**
     * 检查字符串是否是邮箱
     *
     * @param text
     * @return
     */
    public static boolean isEmail(String text) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    /**
     * 密码规则验证
     * 6-16位数字和字母的组合
     */
    public static boolean verifyPassword(String pwd) {
        return pwd.matches("\\w{6,16}");  // && pwd.matches(".*\\d+.*") && pwd.matches(".*[a-zA-Z]+.*");
    }


    /**
     * 校验银行卡卡号
     *
     * @return
     */
    public static boolean checkBankCardNo(String bankCardNo) {
        if (bankCardNo == null || bankCardNo.length() == 0 || !bankCardNo.matches("\\d+")) {
            return false;
        }
        //银行卡号为16 或19位
        if (bankCardNo.length() < 16 || bankCardNo.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCardNo.substring(0, bankCardNo.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCardNo.charAt(bankCardNo.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0 || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 把 银行卡号 4位 空格
     *
     * @param cardNo
     * @return
     */
    public static String blankCardNo(String cardNo) {
        if (cardNo == null || cardNo.length() == 0) {
            return null;
        }

        cardNo = cardNo.trim().replace(" ", "");
        int len = cardNo.length();
        int size = len / 4;
        int remaind = len % 4;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(cardNo.substring(i * 4, 4 * (i + 1)));
            if (i != size - 1 || remaind != 0) {
                stringBuilder.append(" ");
            }
        }
        String endNo = cardNo.substring(len - remaind);
        stringBuilder.append(endNo);
        return stringBuilder.toString();
    }


    public static String getBankCardLastFour(String bankNo) {

        if (isNull(bankNo)) return "";

        if (bankNo.length() < 4) return bankNo;

        return bankNo.substring(bankNo.length() - 4);

    }

    /**
     * 隐藏手机
     */
    public static String hidePhone(String phoneNo) {
        return phoneNo.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }


    /**
     * 隐藏手机
     */

/*    public static String hidePhone(String phone) {
        if (phone == null || "".equals(phone))
            return "";
        phone = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
        return phone;
    }*/

    /**
     * 隐藏真实姓名
     */
    public static String hideRealName(String realName) {
        if (realName == null || "".equals(realName))
            return "";
        realName = realName.substring(0, 1) + "**";
        return realName;
    }

    /**
     * 隐藏身份证号
     */
    public static String hideCardNo(String cardNo) {
        if (cardNo == null || "".equals(cardNo))
            return "";
//        cardNo = cardNo.substring(0, 4) + "*****" + cardNo.substring(cardNo.length() - 3);
        cardNo = cardNo.substring(0, 1) + cardNo.substring(1, cardNo.length() - 1).replaceAll(".", "*") + cardNo.substring(cardNo.length() - 1);
        return cardNo;
    }

    /**
     * 隐藏EMAIL
     */
    public static String hideEmail(String email) {
        if (email == null || "".equals(email))
            return "";
        email = email.substring(0, 3) + "***" + email.substring(email.indexOf("@"));
        return email;
    }

    /**
     * 隐藏呢称
     */
    public static String hideNickName(String nickName) {
        if (nickName == null || "".equals(nickName))
            return "";
        nickName = nickName.substring(0, 3) + "**";
        return nickName;
    }

    /**
     * 格式化银行卡号，如：6226*****2621
     */
    public static String hideBankCardNo(String bankCardNo) {
        if (bankCardNo == null || bankCardNo.trim().length() == 0)
            return "";

        String sRet = bankCardNo.trim();
        if (sRet.length() > 4)
            sRet = sRet.substring(0, 4) + "*****" + sRet.substring(sRet.length() - 4);
        else
            sRet = sRet.substring(0, 4) + "*****" + sRet;
        return sRet;
    }

    /**
     * 身份证号验证
     *
     * @param s
     * @return
     */

    public static boolean isIDCardNumber(String s) {
//        Pattern mIdCheckPattern = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
//        return mIdCheckPattern.matcher(s).matches();
        return IDCardCheckUtil.isIDCard(s);
    }


    /***
     * 是否一个字符串都是一样的字符
     *
     * @param s
     * @return
     */
    public static boolean isAllCharSame(String s) {
        if (s.isEmpty()) {
            return false;
        }
        char[] chars = s.toCharArray();
        char char1 = chars[0];
        for (char c : chars) {
            if (c != char1) {
                return false;
            }
        }

        return true;
    }

    /***
     * 是否一个字符串为连续数字
     *
     * @param s
     * @return
     */

    public static boolean isContinuousNumber(String s) {

        if (!s.matches("\\d{2,}")) return false;

        char[] chars = s.toCharArray();
        char char0 = chars[0];
        for (int i = 1; i < s.length(); i++) {
            if (chars[i] != char0 + i) return false;
        }
        return true;
    }


    /**
     * 获取 url 中的 参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> getParamMapByUrl(String url) {
        if (url != null && url.length() != 0 && url.contains("?")) {
            String paramStr = url.substring(url.indexOf("?") + 1);
            if (paramStr.contains("#")) {
                paramStr = paramStr.substring(0, paramStr.indexOf("#"));
            }
            return getParamsMapFromFromUrlEncoded(paramStr);

        }

        return Collections.emptyMap();


//
//        Map<String, String> paramMap = new HashMap<>();
//        if (url == null || url.length() == 0) {
//            return paramMap;
//        }
//        String paramStr = url.substring(url.indexOf("?") + 1);
//        String[] params = paramStr.split("&");
//        for (String param : params) {
//            if (param.indexOf("=") == -1) {
//                continue;
//            }
//            String[] mParams = param.split("=");
//            if (mParams == null || mParams.length != 2) {
//                continue;
//            }
//            paramMap.put(mParams[0], mParams[1]);
//        }
//        return paramMap;
    }

    /**
     * 从FromUrlEncoded数据中获取键值对放入map中
     *
     * @param formData
     * @return
     */

    public static Map<String, String> getParamsMapFromFromUrlEncoded(String formData) {
        Map<String, String> params = new LinkedHashMap<>();
        String[] nameValuePairs = formData.split("&");

        for (String nameValuePair : nameValuePairs) {
            if (nameValuePair.contains("=")) {
                int equalSignIndex = nameValuePair.indexOf("=");
                String name = URLDecoder.decode(nameValuePair.substring(0, equalSignIndex));
                String value = "";
                if (equalSignIndex != nameValuePair.length() - 1) {
                    value = URLDecoder.decode(nameValuePair.substring(equalSignIndex + 1, nameValuePair.length()));
                }
                params.put(name, value);
            }
        }
        return params;
    }


    /**
     * @param text  显示的文字
     * @param from  要缩小部分的开始index(包含)
     * @param end   要缩小部分的结束index(不包含)
     * @param ratio 对应的缩小比例
     */


    public static SpannableString getPartSmallText(String text, int[] from, int[] end, float[] ratio) {
        if (from == null || end == null || from.length != end.length || from.length != ratio.length) {
            throw new IllegalArgumentException();
        }
        SpannableString contentAmount = new SpannableString(text);
        for (int i = 0; i < from.length; i++) {
            contentAmount.setSpan(new SmallTextSpan(ratio[i]),
                    from[i], end[i], SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return contentAmount;
    }


}
