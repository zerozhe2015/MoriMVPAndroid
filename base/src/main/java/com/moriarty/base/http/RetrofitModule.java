package com.moriarty.base.http;


import com.moriarty.base.http.domain.DomainInterceptor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module
public class RetrofitModule {

    @Singleton
    @Provides
    @Named("mainServer")
    static OkHttpClient provideMainOkHttpClient( CommonRequestParamsProvider commonRequestParamsProvider) {
        return RetrofitHelper.createOkHttpClient(new DomainInterceptor(commonRequestParamsProvider));
    }


    @Singleton
    @Provides
    @Named("mainServer")
    static Retrofit provideMainRetrofit(@Named("serverBaseUrl") String baseUrl,    @Named("mainServer") OkHttpClient okHttpClient) {
        return RetrofitHelper.createRetrofit(baseUrl, okHttpClient);
    }


}
