package com.moriarty.base.di;


import android.app.Activity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }


    @Provides
    @PerActivity
    Activity provideActivity() {
        return mActivity;
    }
}
