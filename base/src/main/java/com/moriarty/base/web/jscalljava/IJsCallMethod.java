package com.moriarty.base.web.jscalljava;

import android.webkit.WebView;

import com.moriarty.base.web.BridgeResult;


/**
 * Created by liuzhe on 2018/1/31.
 */

public interface IJsCallMethod {
    String getMethodName();

    //    String getMethodVersion();
    BridgeResult execute(WebView webView, String paramsJsonStr, JSCallbackApplier jsCallbackApplier) throws Throwable;
}
