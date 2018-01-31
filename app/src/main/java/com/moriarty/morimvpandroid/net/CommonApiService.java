package com.moriarty.morimvpandroid.net;


import com.moriarty.morimvpandroid.entity.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.POST;

public interface CommonApiService {


    @POST("common/appHomePage")
    Observable<UserInfo> appHomePage();
}
