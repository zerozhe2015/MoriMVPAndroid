package com.moriarty.base.data;

import java.io.Serializable;
import java.lang.reflect.Type;

import javax.inject.Qualifier;

public interface LocalDataStorage {
    void putSerializable(String key, Serializable object);

    <T extends Serializable> T getSerializable(String key);

    void putObjectByType(String key, Object o, Type type);

    <T> T getObjectByType(String key, Type type);

    void putString(String key, String s);

    String getString(String key);

    void putLong(String key, long s);

    long getLong(String key, long defaultValue);

    void putInt(String key, int s);

    int getInt(String key, int defaultValue);

    void putFloat(String key, float s);

    float getFloat(String key, float defaultValue);

    void putBoolean(String key, boolean b);

    boolean getBoolean(String key, boolean defaultValue);

    void remove(String key);

    void clear();


    @Qualifier
    @interface Default {

    }

    @Qualifier
    @interface LocalFileName {

    }
}
