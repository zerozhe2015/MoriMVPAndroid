package com.moriarty.base.http.domain;


/**
 * 约定的通用结果返回格式
 *
 * @param <T> data 的类型
 */
public class DomainResult<T> {

    public static final String CODE_INVALIDATE_SESSION_ID = "800";//SESSION_ID失效；
    public static final String CODE_OTHER_DEVICE_LOGIN = "900";//其他设备登录
    public static final String CODE_FORCE_UPDATE = "1000";//强制升级
    public static final String CODE_SUCCESS = "200";//成功


    public static final String CODE_TRANS_PWD_ERR = "5000112";//交易密码错误
    public static final String CODE_TRANS_PWD_LOCK = "5000115";//交易密码锁定

    public String code;

    public String message;

    public String dataJsonStr;

    public T data;

    public boolean isSuccess() {
        return CODE_SUCCESS.equals(code);
    }
}
