package com.moriarty.morimvpandroid.configuration;

import android.content.Context;
import android.content.SharedPreferences;

import com.moriarty.base.util.TextToolkit;
import com.moriarty.morimvpandroid.AppApplication;


public class TestConfig implements AppConfiguration {
    @Override
    public String getServerBaseUrl() {
        if (!TextToolkit.isNull(localServerBaseUrl)) {
            return localServerBaseUrl + "/appserver/services/";
        }
        return "https:/www.baidu.com/";
    }

    @Override
    public String getH5PageBaseUrl() {
        if (!TextToolkit.isNull(localServerBaseUrl)) {
            return localServerBaseUrl + "/appserver/test/";
        }

        return "https:/www.baidu.com/";

    }


    public String getServerUrl() {
        if (!TextToolkit.isNull(localServerBaseUrl)) {
            return localServerBaseUrl;
        }
        return "https:/www.baidu.com/";
    }


    private static final String SP_NAME = "ConfigForTest";
    SharedPreferences sp;

    private static final String LOCAL_SERVER_BASE_URL_KEY = "LOCAL_SERVER_BASE_URL_KEY";
    private static final String LOCAL_PROMOTION_SERVER_BASE_URL_KEY = "LOCAL_PROMOTION_SERVER_BASE_URL_KEY";
    private String localServerBaseUrl = null;
    private String localPromotionServerBaseUrl = null;

    public TestConfig() {
        sp = AppApplication.getApplication().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        localServerBaseUrl = sp.getString(LOCAL_SERVER_BASE_URL_KEY, null);
        localPromotionServerBaseUrl = sp.getString(LOCAL_PROMOTION_SERVER_BASE_URL_KEY, null);
    }


    public void setLocalServerBaseUrl(String serverBaseUrl) {
        localServerBaseUrl = serverBaseUrl;
        sp.edit().putString(LOCAL_SERVER_BASE_URL_KEY, serverBaseUrl).apply();
    }

    public void setLocalPromotionServerBaseUrl(String promotionServerBaseUrl) {
        localPromotionServerBaseUrl = promotionServerBaseUrl;
        sp.edit().putString(LOCAL_PROMOTION_SERVER_BASE_URL_KEY, promotionServerBaseUrl).apply();
    }

}
