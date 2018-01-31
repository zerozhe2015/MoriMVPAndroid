package com.moriarty.base.di;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;


@Module
public class FragmentModule {
    Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        this.mFragment = fragment;
    }


    @Provides
    @PerActivity
    Fragment provideFragment() {
        return mFragment;
    }

}
