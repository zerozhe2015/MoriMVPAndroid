package com.moriarty.base.exception;

import com.moriarty.base.http.domain.FailedResultError;

import java.net.ConnectException;
import java.net.SocketTimeoutException;


public class ErrorMessageFactory {

    public static String getErrorMsg(Throwable error) {

        if (error instanceof FailedResultError) {      //返回结果code不为0
            return getFailedResultErrorMessage((FailedResultError) error);
        }
//        else if (error instanceof NullResultDataError) {  //结果为空
//            return "返回结果为空";
//        }
//        else if (error instanceof NoConnectionError) {
//            return "没有网络连接";
//        } else if (error instanceof NetworkError) {  //网络错误
////            return "您的网络有问题哦~";
//            return "连接服务器失败~";
//        }
        else if (error instanceof SocketTimeoutException) { //请求超时
            return "网络超时,请重试";
        }
//         else if (error instanceof ParseError) {  //解析数据失败
//            return "解析服务器返回数据出错";
//        } else if (error instanceof ServerError) { //服务器返回5xx 4xx等
//            return "很抱歉，服务器出错了";
//        } else if (error instanceof AuthFailureError) {
//            return "认证失败";
//        }
        else if (error instanceof ConnectException) { //请求超时
            return "无法连接至服务器";
        }
        return "很抱歉，出错了，请稍后再试~";
    }

    public static String getFailedResultErrorMessage(FailedResultError error) {
        return error.getDomainResult().message;
    }
}
