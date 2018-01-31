package com.moriarty.morimvpandroid.net;


import com.moriarty.base.http.CommonRequestParamsProvider;
import com.moriarty.base.http.RetrofitModule;
import com.moriarty.morimvpandroid.configuration.ConfigurationModule;
import com.moriarty.morimvpandroid.manager.MoriUserManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;


@Module(includes = {RetrofitModule.class, ConfigurationModule.class})
public class ApiServiceModule {


    @Singleton
    @Provides
    static CommonRequestParamsProvider provideCommonRequestParamsProvider(MoriUserManager moriUserManager) {
        return new DomainCommonRequestParamsProvider(moriUserManager);
    }


    @Singleton
    @Provides
    static ApiServiceManager provideAPIServiceManager(@Named("mainServer") Retrofit mainRetrofit
//            , @Named("promotionServer") Retrofit promotionRetrofit
    ) {
        return new ApiServiceManager(mainRetrofit
//                , promotionRetrofit
        );
    }

}
