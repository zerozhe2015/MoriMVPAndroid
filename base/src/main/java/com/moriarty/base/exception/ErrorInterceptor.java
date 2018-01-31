package com.moriarty.base.exception;

public interface ErrorInterceptor {

    boolean interceptError(Throwable throwable);
}
