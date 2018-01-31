package com.moriarty.base.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 */
@Module
public class BaseApplicationModule {

    Application mApplication;

    public BaseApplicationModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    protected Application provideApplication() {
        return mApplication;
    }


}
