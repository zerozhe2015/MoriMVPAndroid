package com.moriarty.base.web.jscalljava;

/**
 * Created by liuzhe on 2018/1/31.
 */

public class ParamErrorException extends RuntimeException {


    public ParamErrorException() {
    }

    public ParamErrorException(Throwable throwable) {
        super(throwable);
    }

    public ParamErrorException(String detailMessage) {
        super(detailMessage);
    }

    public ParamErrorException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}