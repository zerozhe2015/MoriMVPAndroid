package com.moriarty.base.web.jscalljava;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.moriarty.base.web.BridgeResult;
import com.moriarty.base.web.calljs.JavaCallJsUtil;

import java.lang.ref.WeakReference;


/**
 * Created by liuzhe on 2018/1/31.
 */

public class JSCallbackApplier {
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static final String CALLBACK_JS_FORMAT = "JSBridge.invokeJsCallback('%s', %s);";
    private String mId;
    private WeakReference<WebView> mWebViewRef;

    boolean applied = false;

    public JSCallbackApplier(WebView view, String id) {
        mWebViewRef = new WeakReference<>(view);
        mId = id;
    }


    public void apply(BridgeResult result) {
        final String execJs = String.format(CALLBACK_JS_FORMAT, mId, result.toJson().toString());
        if (mWebViewRef != null && mWebViewRef.get() != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    executeJs(mWebViewRef.get(), execJs);
                }
            });
        }
        applied = true;
    }

    public boolean isApplied() {
        return applied;
    }

    private void executeJs(WebView webView, String jsCode) {
        JavaCallJsUtil.invokeJs(webView, jsCode);
    }
}
