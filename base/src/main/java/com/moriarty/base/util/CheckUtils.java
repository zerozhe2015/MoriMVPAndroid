package com.moriarty.base.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class CheckUtils {

    /**
     * 验证是否为手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        // Pattern p =
        // Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        // Matcher m = p.matcher(mobiles);
        // return m.matches();
        if (!TextUtils.isEmpty(mobiles)) {
            if (mobiles.length() == 11 && mobiles.startsWith("1")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证密码
     *
     * @param pwd
     * @return
     */
    public static boolean isPasswd(String pwd) {
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{8,20}");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    /**
     * 验证身份证号
     *
     * @param certificate_num
     * @return
     */
    public static boolean isComplexCertificate(String certificate_num) {
        //简单验证15位全数字，18位最后一位可以是X的。不是复杂验证，不能验证真实身份证
        Pattern p = Pattern.compile("(^\\d{15}$)|(^\\d{17}(?:\\d|x|X)$)");
        Matcher m = p.matcher(certificate_num);
        return m.matches();
    }

    //
    public static boolean isEmail(String text) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isComplexPwd(String mobiles) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]{6,20}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
