package com.moriarty.base.http;

import com.moriarty.base.log.L;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        L.d("HTTP", message);
    }
}
