package com.moriarty.morimvpandroid.configuration;


import com.moriarty.morimvpandroid.util.IDEBuildUtils;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfigurationModule {

    @Singleton
    @Provides
    static AppConfiguration provideAppConfiguration() {
//        return new DebugConfig();
        return IDEBuildUtils.getConfigByBuildType();
//        return new TestConfig();
    }

    @Singleton
    @Provides
    @Named("serverBaseUrl")
    static String provideServerBaseUrl(AppConfiguration configuration) {
        return configuration.getServerBaseUrl();
    }

}
