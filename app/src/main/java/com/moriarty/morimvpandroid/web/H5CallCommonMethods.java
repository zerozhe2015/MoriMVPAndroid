package com.moriarty.morimvpandroid.web;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.webkit.WebView;

import com.moriarty.base.http.BasicCommonRequestParamsProvider;
import com.moriarty.base.http.domain.DomainInterceptor;
import com.moriarty.base.http.domain.DomainResult;
import com.moriarty.base.http.domain.FailedResultError;
import com.moriarty.base.log.L;
import com.moriarty.base.ui.dialog.LoadingDialog;
import com.moriarty.base.ui.notify.UiTip;
import com.moriarty.base.util.PackageUtils;
import com.moriarty.base.util.TextToolkit;
import com.moriarty.base.web.BridgeResult;
import com.moriarty.base.web.jscalljava.JSCallbackApplier;
import com.moriarty.base.web.jscalljava.JsCallMethodAnnotation;
import com.moriarty.base.web.jscalljava.ParamErrorException;
import com.moriarty.morimvpandroid.CommonErrorInterceptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;


public class H5CallCommonMethods {


    /**
     * 加密H5执行网络请求参数
     */

    @JsCallMethodAnnotation(methodName = "encryptRequestParams")
    public static BridgeResult encryptRequestParams(WebView webView, String params, JSCallbackApplier callbackApplier) throws Exception {

        Map<String, String> paramMap = new ArrayMap<>();
//        Map<String, String> paramMap = new DomainCommonRequestParamsProvider(DIHelper.getAppComponent().getUserManager()).provideCommonRequestParams();


        if (!TextToolkit.isNull(params)) {
            //将params从json转化成Map
            try {
                JSONObject jsonObject = new JSONObject(params);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    paramMap.put(key, jsonObject.getString(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                throw new ParamErrorException(e);
            }
        }
        String encryptResult = DomainInterceptor.encryptRequestParams(paramMap);
        return BridgeResult.NativeResult.success(encryptResult);
    }

    @JsCallMethodAnnotation(methodName = "encryptRequestParamsWithCommonParams")
    public static BridgeResult encryptRequestParamsWithCommonParams(WebView webView, String params, JSCallbackApplier callbackApplier) throws Exception {

//        Map<String, String> paramMap = new ArrayMap<>();
//        Map<String, String> paramMap = new DomainCommonRequestParamsProvider(DIHelper.getAppComponent().getUserManager()).provideCommonRequestParams();
        Map<String, String> paramMap = new BasicCommonRequestParamsProvider().provideCommonRequestParams();


        if (!TextToolkit.isNull(params)) {
            //将params从json转化成Map
            try {
                JSONObject jsonObject = new JSONObject(params);
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    paramMap.put(key, jsonObject.getString(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                throw new ParamErrorException(e);
            }
        }
        String encryptResult = DomainInterceptor.encryptRequestParams(paramMap);
        return BridgeResult.NativeResult.success(encryptResult);
    }


    @JsCallMethodAnnotation(methodName = "closePage")
    public static BridgeResult closePage(WebView webView, String params, JSCallbackApplier callbackApplier) {
        Context webViewContext = webView.getContext();
        if (webViewContext != null && webViewContext instanceof Activity) {
            ((Activity) webViewContext).finish();
        }
        BridgeResult result = BridgeResult.NativeResult.success((String) null);
        return result;
    }


    private static LoadingDialog loadingDialog;

    @JsCallMethodAnnotation(methodName = "showLoading")
    public static BridgeResult showLoading(WebView webView, String params, JSCallbackApplier callbackApplier) {

        if (loadingDialog == null || loadingDialog.getContext() != webView.getContext()) {
            loadingDialog = new LoadingDialog(webView.getContext());
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }

        BridgeResult result = BridgeResult.NativeResult.success((String) null);
        return result;
    }

    @JsCallMethodAnnotation(methodName = "dismissLoading")
    public static BridgeResult dismissLoading(WebView webView, String params, JSCallbackApplier callbackApplier) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        BridgeResult result = BridgeResult.NativeResult.success((String) null);
        return result;
    }

    @JsCallMethodAnnotation(methodName = "nativeAlert")
    public static BridgeResult nativeAlert(WebView webView, String params, JSCallbackApplier callbackApplier) {
        try {
            L.d("params = " + params);
            String msg = new JSONObject(params).getString("msg");
            UiTip.showTipAlertDialog(webView.getContext(), msg, "确定");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ParamErrorException();
        }
        return BridgeResult.NativeResult.success((String) null);
    }

    @JsCallMethodAnnotation(methodName = "showToastBottom")
    public static BridgeResult showToastBottom(WebView webView, String params, JSCallbackApplier callbackApplier) {
        try {
            String msg = new JSONObject(params).getString("msg");
            UiTip.toast(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ParamErrorException();
        }
        return BridgeResult.NativeResult.success((String) null);
    }

    @JsCallMethodAnnotation(methodName = "showToastTop")
    public static BridgeResult showToastTop(WebView webView, String params, JSCallbackApplier callbackApplier) {
        try {
            String msg = new JSONObject(params).getString("msg");
            UiTip.toastMsgTop(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ParamErrorException();
        }
        return BridgeResult.NativeResult.success((String) null);
    }


    @JsCallMethodAnnotation(methodName = "interceptSpecialErrorResult")
    public static BridgeResult interceptSpecialErrorResult(WebView webView, String params, JSCallbackApplier callbackApplier) {

        JSONObject resultDataJson = null;

        try {
            L.d("interceptSpecialErrorResult is called  params=" + params);
            JSONObject paramsJson = new JSONObject(params);
            JSONObject resultJson = paramsJson.getJSONObject("result");

            DomainResult domainResult = new DomainResult();
            domainResult.code = resultJson.getString("code");
            domainResult.message = resultJson.getString("message");
            domainResult.dataJsonStr = resultJson.optString("data", null);

            boolean isIntercepted = new CommonErrorInterceptor().interceptError(new FailedResultError(resultJson.toString(), domainResult));
            resultDataJson = new JSONObject();
            resultDataJson.put("isIntercepted", isIntercepted);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ParamErrorException();
        }
        BridgeResult result = BridgeResult.NativeResult.success((resultDataJson));
        return result;
    }


    @JsCallMethodAnnotation(methodName = "getAppVersion")
    public static BridgeResult getAppVersion(WebView webView, String params, JSCallbackApplier callbackApplier) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appVersion", PackageUtils.getCurrentVersionName());

        BridgeResult result = BridgeResult.NativeResult.success(jsonObject);
        L.d(result.toJsonString());
        return result;
    }


    @JsCallMethodAnnotation(methodName = "sendTDMessage")
    public static BridgeResult sendTDMessage(WebView webView, String params, JSCallbackApplier callbackApplier) throws Exception {

        try {

            JSONObject paramsJson = new JSONObject(params);
            String event = paramsJson.getString("fromActivity");
            //AnalyticsFactory.getInstance().onEvent(webView.getContext(), event);

        } catch (Exception e) {
            e.printStackTrace();
        }
        BridgeResult result = BridgeResult.NativeResult.success((String) null);
        return result;
    }


}
