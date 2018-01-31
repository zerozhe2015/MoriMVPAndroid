package com.moriarty.base.web.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by liuzhe on 2018/1/31.
 */

public final class BaseWebView extends WebView {


    ProgressBar mProgressBar;

    InnerWebChromeClient mInnerWebChromeClient;

    InnerWebViewClient mInnerWebViewClient;

    boolean mIsShowProgress = true;//默认为true;


    BaseWebView(Context context) {
        super(context);
        setWebView();
    }

//    public BaseWebView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setWebView();
//    }
//
//    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        setWebView();
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        setWebView();
//    }
//
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
//        super(context, attrs, defStyleAttr, privateBrowsing);
//        setWebView();
//    }


    private void setWebView() {


        WebSettings settings = this.getSettings();

        settings.setSupportZoom(true);

        settings.setJavaScriptEnabled(true);

        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        settings.setAllowFileAccess(true);

        // h5 localStorage
        settings.setDomStorageEnabled(true);
        String appCachePath;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            appCachePath = getContext().getExternalCacheDir().getAbsolutePath();
//        } else {
        appCachePath = getContext().getCacheDir().getAbsolutePath();
//        }
        settings.setAppCachePath(appCachePath);
        settings.setAppCacheEnabled(true);
        settings.setUserAgentString(settings.getUserAgentString() + "ebatech");
        /**
         *  Webview在安卓5.0之前默认允许其加载混合网络协议内容
         *  在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        createProgressBar();
        mInnerWebChromeClient = new InnerWebChromeClient();
        super.setWebChromeClient(mInnerWebChromeClient);
        mInnerWebViewClient = new InnerWebViewClient();
        super.setWebViewClient(mInnerWebViewClient);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            try {
//                this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//       ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        } catch (Exception e) {
//
//        }

//        this.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK && BaseWebview.this.canGoBack()) {
//                        BaseWebview.this.goBack(); // 后退
//                        return true; // 已处理
//                    }
//                }
//                return false;
//            }
//        });
    }

    /**
     * 添加progressBar
     */

    private void createProgressBar() {
        mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()) + 0.5f)));
        mProgressBar.setVisibility(View.GONE);
//        mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom));
//        addView(mProgressBar);
//        mProgressBar = (ProgressBar) LayoutInflater.from( getContext()).inflate(R.layout.webview_progressbar, this, false);
//        addView(mProgressBar);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mProgressBar.getParent() == null) {
                    addView(mProgressBar);
                }
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }


    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public void setShowProgressEnable(boolean isShowProgress) {
        this.mIsShowProgress = isShowProgress;
    }

    public void setProgressBarColor(int backgroundColor, int progressColor) {
        ColorDrawable backgroundDrawable = new ColorDrawable(backgroundColor);
        ColorDrawable progressDrawable = new ColorDrawable(progressColor);
        ClipDrawable clipDrawable = new ClipDrawable(progressDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{backgroundDrawable, clipDrawable});
        layerDrawable.setId(0, android.R.id.background);
        layerDrawable.setId(1, android.R.id.progress);
        mProgressBar.setProgressDrawable(layerDrawable);
    }


    /**
     * 如果传入的类型为WebViewClientWrapper
     * 则再次调用则调用之前WebViewClientWrapper.setWrapper
     *
     * @param client
     */


    @Override
    public void setWebViewClient(WebViewClient client) {

//            mInnerWebViewClient.setWrapper(client);
        getDescendantWrapper(mInnerWebViewClient).setWrapper(client);
    }

    private WebViewClientWrapper getDescendantWrapper(WebViewClientWrapper client) {
        WebViewClient wrapper = client.getWrapper();
        if (wrapper != null && wrapper instanceof WebViewClientWrapper) {
            return getDescendantWrapper((WebViewClientWrapper) wrapper);
        }
        return client;
    }


    /**
     * 如果传入的类型为WebChromeClientWrapper
     * 则再次调用则调用之前WebChromeClientWrapper.setWrapper
     *
     * @param client
     */

    @Override
    public void setWebChromeClient(WebChromeClient client) {
//        mInnerWebChromeClient.setWrapper(client);
        getDescendantWrapper(mInnerWebChromeClient).setWrapper(client);
    }

    private WebChromeClientWrapper getDescendantWrapper(WebChromeClientWrapper client) {
        WebChromeClient wrapper = client.getWrapper();
        if (wrapper != null && wrapper instanceof WebChromeClientWrapper) {
            return getDescendantWrapper((WebChromeClientWrapper) wrapper);
        }
        return client;
    }

    private void pageStart() {
        if (mIsShowProgress) {
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void pageFinish() {
        if (mIsShowProgress) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void pageProgressChanged(int progress) {
        if (mIsShowProgress) {
            mProgressBar.setProgress(progress);
            if (progress >= 100) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private class InnerWebViewClient extends WebViewClientWrapper {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pageStart();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pageFinish();
        }

//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return false;
//        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }


    }


    private class InnerWebChromeClient extends JSBridgeWebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view, progress);
            pageProgressChanged(progress);
        }
    }
}