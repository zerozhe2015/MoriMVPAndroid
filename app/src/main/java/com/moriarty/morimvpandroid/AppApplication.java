package com.moriarty.morimvpandroid;


import com.moriarty.base.BaseApplication;
import com.moriarty.base.exception.ErrorInterceptor;
import com.moriarty.base.http.RetrofitHelper;

public class AppApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initializeInjector();

        addCommonErrorInterceptor();


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 初始化全局appconment
     */
    private void initializeInjector() {
        DIHelper.init();
    }


    /**
     * 增加全局错误拦截器
     */
    private void addCommonErrorInterceptor() {
        ErrorInterceptor errorInterceptor = new CommonErrorInterceptor();
        RetrofitHelper.addCommonErrorInterceptor(errorInterceptor);
    }

}


