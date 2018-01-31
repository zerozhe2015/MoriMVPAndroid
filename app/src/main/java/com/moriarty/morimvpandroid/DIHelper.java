package com.moriarty.morimvpandroid;


public class DIHelper {

    static AppComponent appComponent;

    static void init() {
        appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(AppApplication.getApplication()))
                .build();


    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }


}
