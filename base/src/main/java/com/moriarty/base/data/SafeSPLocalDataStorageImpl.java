package com.moriarty.base.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.moriarty.base.BaseApplication;
import com.moriarty.base.encryption.AES;
import com.moriarty.base.sc.Keys;
import com.moriarty.base.util.IOUtil;
import com.moriarty.base.util.TextToolkit;

import java.io.Serializable;
import java.lang.reflect.Type;



public class SafeSPLocalDataStorageImpl implements Serializable, LocalDataStorage {

    static class InstanceHolder {
        static SafeSPLocalDataStorageImpl instance = new SafeSPLocalDataStorageImpl();
    }


    static SafeSPLocalDataStorageImpl defaultInstance() {
        return InstanceHolder.instance;
    }

    public static SafeSPLocalDataStorageImpl newInstance(String fileName) {
        return new SafeSPLocalDataStorageImpl(fileName);
    }

    private static final String DEFAULT_SP_FILE_NAME = Keys.SAFE_SP_LOCAL_DATA_STORAGE_DEFAULT_SP_FILE_NAME;

    private SharedPreferences sharedPreferences;

    private SafeSPLocalDataStorageImpl() {
        this(DEFAULT_SP_FILE_NAME);
    }

    private SafeSPLocalDataStorageImpl(String fileName) {
        sharedPreferences = BaseApplication.getApplication().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }


    @Override
    public void putSerializable(String key, Serializable object) {
        sharedPreferences.edit().putString(key, IOUtil.SerializableToString(object)).apply();
    }

    @Override
    public <T extends Serializable> T getSerializable(String key) {
        return IOUtil.StringToSerializable(sharedPreferences.getString(key, null));
    }


    @Override
    public void putObjectByType(String key, Object o, Type type) {
        String jsonData = new Gson().toJson(o, type);
        putString(key, jsonData);
    }

    @Override
    public <T> T getObjectByType(String key, Type type) {
        String string = getString(key);
        if (TextToolkit.isEmpty(string)) return null;
        return new Gson().fromJson(string, type);
    }


    @Override
    public void putString(String key, String s) {
        sharedPreferences.edit().putString(key, aesEncrypt(s)).apply();
    }

    @Override
    public String getString(String key) {
        return aesDecrypt(sharedPreferences.getString(key, ""));
    }

    @Override
    public void putLong(String key, long l) {
        putString(key, Long.toString(l));
    }

    @Override
    public long getLong(String key, long defaultValue) {
        String s = getString(key);
        if (TextToolkit.isEmpty(s)) {
            return defaultValue;
        }
        return Long.valueOf(s);
    }

    @Override
    public void putInt(String key, int i) {
        putString(key, Integer.toString(i));
    }

    @Override
    public int getInt(String key, int defaultValue) {
        String s = getString(key);
        if (TextToolkit.isEmpty(s)) {
            return defaultValue;
        }
        return Integer.valueOf(s);
    }

    @Override
    public void putFloat(String key, float s) {
        putString(key, Float.toString(s));
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        String s = getString(key);
        if (TextToolkit.isEmpty(s)) {
            return defaultValue;
        }
        return Float.valueOf(s);
    }

    @Override
    public void putBoolean(String key, boolean b) {
        putString(key, Boolean.toString(b));
    }


    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        String s = getString(key);
        if (TextToolkit.isEmpty(s)) {
            return defaultValue;
        }
        return Boolean.valueOf(s);
    }

    @Override
    public void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }


    @Override
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }


    private String aesEncrypt(String originalStr) {
        if (TextToolkit.isEmpty(originalStr)) {
            return originalStr;
        }
        return AES.encrypt(originalStr);
    }


    private String aesDecrypt(String encryptedStr) {
        if (TextToolkit.isEmpty(encryptedStr)) {
            return encryptedStr;
        }
        return AES.decrypt(encryptedStr);
    }
}
