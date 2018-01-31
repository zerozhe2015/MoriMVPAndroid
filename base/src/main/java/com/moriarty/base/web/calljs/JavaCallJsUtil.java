package com.moriarty.base.web.calljs;

import android.os.Build;
import android.os.SystemClock;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.webkit.WebView;

import com.moriarty.base.web.BridgeResult;
import com.moriarty.base.web.JSBridge;
import com.moriarty.base.web.jscalljava.IJsCallMethod;
import com.moriarty.base.web.jscalljava.JSCallbackApplier;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by liuzhe on 2018/1/31.
 */

public class JavaCallJsUtil {


    static Map<Long, Callback> callBacks = new ArrayMap<>();


    public static interface Callback {
        void onJsReturnValue(BridgeResult result);
    }


    static {
        JSBridge.registerJsCallMethod(new IJsCallMethod() {
            @Override
            public String getMethodName() {
                return "nativeCallJsReturn";
            }

            @Override
            public BridgeResult execute(WebView webView, String params, JSCallbackApplier callback) throws Exception {
                try {
                    JSONObject jsonObject = new JSONObject(params);
                    long callbackId = jsonObject.getLong("callbackId");
                    BridgeResult result = BridgeResult.JsResult.parseJson(jsonObject.getJSONObject("result"));
                    nativeCallJsReturn(callbackId, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return BridgeResult.NativeResult.success("");

            }
        });

    }


    private static synchronized long registerCallback(Callback callback) {
        long callbackId = generateCallbackId();
        callBacks.put(callbackId, callback);
        return callbackId;
    }


    private static synchronized void invokeCallback(long callbackId, BridgeResult result) {
        Callback callback = callBacks.get(callbackId);
        if (null != callback) {
            callback.onJsReturnValue(result);
        }
        callBacks.remove(callbackId);
    }

    private static long generateCallbackId() {
        return   SystemClock.elapsedRealtime();
    }

    /**
     */
    public static void javaCallJsByBridge(WebView webView, String methodName, String params, Callback callback) {
        long callbackId;
        if (callback != null) {
            callbackId = registerCallback(callback);
        } else {
            callbackId = JSBridge.NO_CALLBACK;
        }
        String jsCode = String.format("window.JSBridge.nativeCallJsByBridge(\"%s\",%s,%d);", methodName, params, callbackId);

        Log.i("JSBridge", jsCode);

        invokeJs(webView, jsCode);
    }


    private static void nativeCallJsReturn(long callbackId, BridgeResult result) throws Exception {
        invokeCallback(callbackId, result);
    }


    /**
     * 执行js
     *
     * @param webView
     * @param jsCode
     */

    public static void invokeJs(WebView webView, String jsCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(jsCode, null);
        } else {
            webView.loadUrl("javascript:" + jsCode);
        }
    }
}
