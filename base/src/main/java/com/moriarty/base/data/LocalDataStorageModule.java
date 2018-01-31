package com.moriarty.base.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalDataStorageModule {


    @Provides
    @Singleton
    @LocalDataStorage.Default
    static LocalDataStorage provideDefaultLocalDataStorage() {
        return SafeSPLocalDataStorageImpl.defaultInstance();
    }

    @Provides
    static LocalDataStorage provideNewLocalDataStorage(@LocalDataStorage.LocalFileName String fileName) {
        return SafeSPLocalDataStorageImpl.newInstance(fileName);
    }


}
