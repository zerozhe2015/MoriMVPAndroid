package com.moriarty.morimvpandroid.manager;


import com.moriarty.morimvpandroid.entity.UserInfo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    @Provides
    @Singleton
    static MoriUserManager provideAuthManager(MoriUserManagerImpl authManager) {
        return authManager;
    }


    @Provides
    static UserInfo provideUserInfo(MoriUserManager moriUserManager) {
        return moriUserManager.getUserInfo();
    }



}
