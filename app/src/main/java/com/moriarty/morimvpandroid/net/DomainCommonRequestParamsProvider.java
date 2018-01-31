package com.moriarty.morimvpandroid.net;

import com.moriarty.base.http.BasicCommonRequestParamsProvider;
import com.moriarty.base.util.TextToolkit;
import com.moriarty.morimvpandroid.manager.MoriUserManager;

import java.util.Map;


public class DomainCommonRequestParamsProvider extends BasicCommonRequestParamsProvider {

    MoriUserManager moriUserManager;

    public DomainCommonRequestParamsProvider(MoriUserManager moriUserManager) {
        this.moriUserManager = moriUserManager;
    }


    @Override
    public Map<String, String> provideCommonRequestParams() {
        Map<String, String> commonRequestParams = super.provideCommonRequestParams();
        String sessionId = moriUserManager.getSessionId();
        if (!TextToolkit.isNull(sessionId)) {
            commonRequestParams.put("sessionId", sessionId);
        }
        return commonRequestParams;
    }
}
