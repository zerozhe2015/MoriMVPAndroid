package com.moriarty.morimvpandroid.manager;

import android.content.Context;

import com.moriarty.morimvpandroid.entity.UserInfo;
import com.moriarty.morimvpandroid.event.XEvent;

import io.reactivex.functions.Consumer;

public interface MoriUserManager {

    interface LoginCallback {
        void onLoginSuccess(UserInfo user);

        void onLoginCancel();
    }


    UserInfo getUserInfo();


    boolean isLogin();

    void saveUserInfo(UserInfo userInfo);

    void localLogout();


    void checkLogin(Context context, LoginCallback callback);


    void checkLogin(Context context, Consumer<UserInfo> action);

    /**
     * 当session失效时,强制登录一次,不取本地存储的信息
     */


    void notifyLoginSuccess(UserInfo user);

    void notifyLoginCancel();

    void setSessionId(String session);

    String getSessionId();

    boolean isSessionInvalidated();


    /**
     * 登录事件
     *
     * @param event
     */
    void postUserLoginedEvent(XEvent.UserLogin event);

    /**
     * logout 事件
     *
     * @param event
     */
    void postUserLogoutEvent(XEvent.UserLogout event);

    /**
     * 用户信息更新事件
     *
     * @param event
     */
    void postUserInfoUpdateEvent(XEvent.UserInfoUpdate event);

    /**
     * 用户更换
     *
     * @param event
     */
    void postUserChangedEvent(XEvent.UserChanged event);


}
