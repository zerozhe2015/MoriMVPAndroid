package com.moriarty.morimvpandroid.manager;

import android.content.Context;
import android.text.TextUtils;

import com.moriarty.base.data.LocalDataStorage;
import com.moriarty.base.eventbus.RxBus;
import com.moriarty.base.sc.Keys;
import com.moriarty.base.util.IOUtil;
import com.moriarty.morimvpandroid.entity.UserInfo;
import com.moriarty.morimvpandroid.event.XEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;


@Singleton
public class MoriUserManagerImpl implements MoriUserManager {


    public static final String USERINFO_SP_KEY = Keys.USER_INFO_SP_KEY;
    public static final String SESSION_SP_KEY = Keys.SESSION_SP_KEY;


    private UserInfo mUserInfo;

    private String mSession;

    private LoginCallback mLoginCallback;


    private LocalDataStorage mLocalDataStorage;


    @Inject
    MoriUserManagerImpl(@LocalDataStorage.Default LocalDataStorage localDataStorage) {
        mLocalDataStorage = localDataStorage;
        this.mUserInfo = mLocalDataStorage.getSerializable(USERINFO_SP_KEY);
        this.mSession = mLocalDataStorage.getString(SESSION_SP_KEY);
    }

    @Override
    public UserInfo getUserInfo() {
        return mUserInfo;
    }


    @Override
    public boolean isLogin() {
        return mUserInfo != null && !TextUtils.isEmpty(mSession);
    }

    @Override
    public void saveUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        UserInfo oldUserInfo = mLocalDataStorage.getSerializable(USERINFO_SP_KEY);

        //登录
        if (oldUserInfo == null && userInfo != null) {
            postUserLoginedEvent(new XEvent.UserLogin(userInfo));
            mLocalDataStorage.putSerializable(USERINFO_SP_KEY, userInfo);
        }
        //登出
        else if (oldUserInfo != null && userInfo == null) {
            postUserLogoutEvent(new XEvent.UserLogout());
            mLocalDataStorage.putSerializable(USERINFO_SP_KEY, userInfo);
        } else if (oldUserInfo != null && userInfo != null && !equalsUserInfo(oldUserInfo, userInfo)) {
            //用户信息更新
            if (oldUserInfo.getAccount().equals(userInfo.getAccount())) {
                postUserInfoUpdateEvent(new XEvent.UserInfoUpdate(oldUserInfo, userInfo));
            }
            //切换用户
            else {
                postUserChangedEvent(new XEvent.UserChanged());
            }
            mLocalDataStorage.putSerializable(USERINFO_SP_KEY, userInfo);
        }
    }


    private boolean equalsUserInfo(UserInfo userInfo1, UserInfo userInfo2) {

        if (userInfo1 == null) {
            return userInfo2 == null;
        }
        if (userInfo2 == null) {
            return userInfo1 == null;
        }

        String sessionId1 = userInfo1.getSessionId();
        userInfo1.setSessionId(null);
        String sessionId2 = userInfo2.getSessionId();
        userInfo2.setSessionId(null);

        boolean equals = IOUtil.SerializableToString(userInfo1).equals(IOUtil.SerializableToString(userInfo2));

        userInfo1.setSessionId(sessionId1);
        userInfo1.setSessionId(sessionId2);

        return equals;
    }

    @Override
    public void localLogout() {
        setSessionId(null);
        saveUserInfo(null);
    }

    @Override
    public void checkLogin(Context context, LoginCallback callback) {
        if (isLogin()) {
            callback.onLoginSuccess(mUserInfo);
        } else {
            this.mLoginCallback = callback;
            // TODO: 2018/1/2
            //Navigator.toLogin(context);
        }

    }


    @Override
    public void checkLogin(Context context, Consumer<UserInfo> action) {
        checkLogin(context, new LoginCallback() {
            @Override
            public void onLoginSuccess(UserInfo user) {
                try {
                    action.accept(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoginCancel() {
            }
        });
    }


    @Override
    public void notifyLoginSuccess(UserInfo user) {
        if (mLoginCallback != null) {
            mLoginCallback.onLoginSuccess(user);
            mLoginCallback = null;
        }
    }

    @Override
    public void notifyLoginCancel() {
        if (mLoginCallback != null) {
            mLoginCallback.onLoginCancel();
            mLoginCallback = null;
        }
    }

    @Override
    public void setSessionId(String session) {

//        session = "3736ef2c-93dd-423f-a05a-8e1079f2e464";
        this.mSession = session;
        mLocalDataStorage.putString(SESSION_SP_KEY, session);
    }

    @Override
    public String getSessionId() {
        return mSession;
    }

    @Override
    public boolean isSessionInvalidated() {
        return false;
    }






/*
    @Override
    public void checkRealNameAuth(Context context, Runnable callback) {

        throw new IllegalStateException("not support in this version");
    }

    @Override
    public void checkSetTransPwd(Context context, Runnable callback) {
        throw new IllegalStateException("not support in this version");
    }
*/

//    @Override
//    public void checkRiskEval(Context context, Runnable callback) {
//
//    }


    @Override
    public void postUserLoginedEvent(XEvent.UserLogin event) {
        RxBus.getInstance().post(event);
    }

    @Override
    public void postUserInfoUpdateEvent(XEvent.UserInfoUpdate event) {
        RxBus.getInstance().post(event);
    }

    @Override
    public void postUserChangedEvent(XEvent.UserChanged event) {
        RxBus.getInstance().post(event);
    }


    @Override
    public void postUserLogoutEvent(XEvent.UserLogout event) {
        RxBus.getInstance().post(event);
    }


}
