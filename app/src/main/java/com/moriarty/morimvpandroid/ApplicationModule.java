package com.moriarty.morimvpandroid;

import android.app.Application;

import com.moriarty.base.di.BaseApplicationModule;

import dagger.Module;

/**
 */
@Module
public class ApplicationModule extends BaseApplicationModule {
    public ApplicationModule(Application application) {
        super(application);
    }



}
