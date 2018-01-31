package com.moriarty.morimvpandroid;

import android.app.Application;

import com.moriarty.base.data.LocalDataStorage;
import com.moriarty.base.data.LocalDataStorageModule;
import com.moriarty.morimvpandroid.configuration.AppConfiguration;
import com.moriarty.morimvpandroid.configuration.ConfigurationModule;
import com.moriarty.morimvpandroid.entity.UserInfo;
import com.moriarty.morimvpandroid.manager.MoriUserManager;
import com.moriarty.morimvpandroid.manager.UserModule;
import com.moriarty.morimvpandroid.net.ApiServiceManager;
import com.moriarty.morimvpandroid.net.ApiServiceModule;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ConfigurationModule.class, LocalDataStorageModule.class//application相关
        , UserModule.class//, LocalDataStorageModule.class//user相关
        , ApiServiceModule.class // ,ConfigurationModule.class apiService相关
})
public abstract class AppComponent {

    /*
      app基本
     */

    public abstract Application getApplication();

    public abstract AppConfiguration getAppConfiguration();

    /*
       默认的   LocalDataStorage
     */
    @LocalDataStorage.Default
    public abstract LocalDataStorage getDefaultLocalDataStorage();

      /*
        用户模块相关
       */

    public abstract MoriUserManager getUserManager();

    public abstract Provider<UserInfo> getUserInfoProvider();



    /*
      网络数据相关
     */


    public abstract ApiServiceManager getApiServiceManager();


}
