package com.moriarty.morimvpandroid.net;

import retrofit2.Retrofit;

public class ApiServiceManager {

    private Retrofit mainRetrofit;

    private CommonApiService mCommonApiService;



    ApiServiceManager(Retrofit mainRetrofit) {
        this.mainRetrofit = mainRetrofit;
    }


    public CommonApiService getCommonApiService() {

        if (mCommonApiService == null) {
            mCommonApiService = mainRetrofit.create(CommonApiService.class);
        }

        return mCommonApiService;
    }



}
