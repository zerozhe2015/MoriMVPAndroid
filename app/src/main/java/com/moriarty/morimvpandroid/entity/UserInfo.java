package com.moriarty.morimvpandroid.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

public class UserInfo implements Serializable {


    @SerializedName("sessionId")
    private String sessionId;


    @SerializedName("account")
    private String account;


    public String getAccount() {
        return account;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface UserId {
    }


}
