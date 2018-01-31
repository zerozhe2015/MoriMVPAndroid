package com.moriarty.base.web.webview;

import android.webkit.JsPromptResult;
import android.webkit.WebView;

import com.moriarty.base.web.JSBridge;


/**
 * Created by liuzhe on 2018/1/31.
 */

public class JSBridgeWebChromeClient extends WebChromeClientWrapper {

    private String CONSTRAINT_DATA_PREFIX = "jsBridgeData:";

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {

        if (isSafeUrl(url) && message.startsWith(CONSTRAINT_DATA_PREFIX)) {
            result.confirm(JSBridge.jsCallJava(view, message.substring(CONSTRAINT_DATA_PREFIX.length())).toJsonString());
            return true;
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    protected boolean isSafeUrl(String url) {
        return true;
    }

}