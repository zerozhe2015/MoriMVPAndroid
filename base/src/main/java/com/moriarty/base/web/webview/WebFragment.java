package com.moriarty.base.web.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by liuzhe on 2018/1/31.
 */

public class WebFragment extends WebViewFragmentV4 {
    private SetupWebViewCallback mSetupWebViewCallback;


    public interface SetupWebViewCallback {
        void onSetupWebView(BaseWebView webView);
    }

    public void setupWebView(SetupWebViewCallback setupWebViewCallback) {
        BaseWebView webView = getWebView();

        if (webView != null) {
            setupWebViewCallback.onSetupWebView(webView);
            return;
        }
        this.mSetupWebViewCallback = setupWebViewCallback;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View webView = super.onCreateView(inflater, container, savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.addView(webView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frameLayout.addView(getWebView().getProgressBar());
        if (mSetupWebViewCallback != null) {
            mSetupWebViewCallback.onSetupWebView(getWebView());
        }
        return frameLayout;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSetupWebViewCallback = null;
    }
}