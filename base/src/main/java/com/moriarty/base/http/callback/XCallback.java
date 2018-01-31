package com.moriarty.base.http.callback;


/**
 * Created by xu on 2015/8/6.
 */
public interface XCallback<T> {
    /**
     * 在执行请求前执行
     */
    void onStart();

    /**
     * 请求结束后执行，包括执行成功和失败
     */
    void onFinish();

    /**
     * 成功返回并解析数据
     */
    void onSuccess(T data);

    /**
     * 包括所有请求失败情况
     * 网络请求出错，响应码4xx， 5xx，解析数据出错，以及包括code不等于0的情况
     */

    void onFault(Throwable throwable);


    /**
     * 无网络
     */
    void onNoNetwork();


    void onCancel();

}
