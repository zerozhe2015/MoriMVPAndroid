package com.moriarty.morimvpandroid.event;


import com.moriarty.morimvpandroid.entity.UserInfo;

/**
 *
 */
public class XEvent {


    /**
     * 用户登录
     */

    public static class UserLogin {
        public UserInfo userInfo;

        public UserLogin(UserInfo userInfo) {

            this.userInfo = userInfo;
        }

    }

    /**
     * 用户登出
     */

    public static class UserLogout {

    }


    /**
     * 用户信息更新
     */

    public static class UserInfoUpdate {
        public UserInfo oldUserInfo;
        public UserInfo newUserInfo;

        public UserInfoUpdate(UserInfo oldUserInfo, UserInfo newUserInfo) {
            this.oldUserInfo = oldUserInfo;
            this.newUserInfo = newUserInfo;
        }
    }

    /**
     * 用户更换
     */

    public static class UserChanged {
    }
}
