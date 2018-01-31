package com.moriarty.morimvpandroid.web;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.moriarty.base.log.L;
import com.moriarty.base.util.ResUtils;
import com.moriarty.base.web.JSBridge;
import com.moriarty.base.web.webview.BaseWebView;
import com.moriarty.base.web.webview.WebChromeClientWrapper;
import com.moriarty.base.web.webview.WebFragment;
import com.moriarty.base.web.webview.WebViewClientWrapper;
import com.moriarty.morimvpandroid.BuildConfig;
import com.moriarty.morimvpandroid.R;


public class EbWebFragment extends WebFragment {

    static {
        JSBridge.registerJsCallMethod(H5CallCommonMethods.class);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseWebView webView = getWebView();

        webView.setProgressBarColor(ResUtils.getColorRes(getContext(), R.color.transparent)
                , ResUtils.getColorRes(getContext(), R.color.skyBlue_6CCDF7));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClientWrapper() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.d("shouldOverrideUrlLoading url: " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }


            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                L.d("onLoadResource url: " + url);
            }

//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                L.d("shouldInterceptRequest url: " + request.getUrl().toString());
//                L.d("shouldInterceptRequest  Headers: " + request.getRequestHeaders().toString());
//                return super.shouldInterceptRequest(view, request);
//            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (getActivity() instanceof ShareMenuMange) {

                    ((ShareMenuMange) getActivity()).hiddenShareMenu();
                }
                L.d("onPageStarted url: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                L.d("onPageFinished url: " + url);
            }

        });


        webView.setWebChromeClient(new WebChromeClientWrapper() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                L.d("onReceivedTitle title: " + title);
            }

            @Override
            public void onCloseWindow(WebView view) {
                super.onCloseWindow(view);
                Context webViewContext = webView.getContext();
                if (webViewContext != null && webViewContext instanceof Activity) {
                    ((Activity) webViewContext).finish();
                }
            }
        });

//        webView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//                    if (webView.canGoBack()) {
//                        webView.goBack();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
    }


    public boolean onBackPressed() {
        BaseWebView webView = getWebView();
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

}
