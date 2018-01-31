package com.moriarty.morimvpandroid.web;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.moriarty.base.log.L;
import com.moriarty.base.ui.BaseAppBarActivity;
import com.moriarty.base.ui.dialog.LoadingDialog;
import com.moriarty.base.util.ImageUtil;
import com.moriarty.base.web.BridgeResult;
import com.moriarty.base.web.JSBridge;
import com.moriarty.base.web.jscalljava.IJsCallMethod;
import com.moriarty.base.web.jscalljava.JSCallbackApplier;
import com.moriarty.base.web.jscalljava.ParamErrorException;
import com.moriarty.base.web.webview.BaseWebView;
import com.moriarty.base.web.webview.WebChromeClientWrapper;
import com.moriarty.base.web.webview.WebViewClientWrapper;
import com.moriarty.morimvpandroid.R;

import org.json.JSONException;
import org.json.JSONObject;



public class EbWebActivity extends BaseAppBarActivity {



    String title;

    boolean hasFixedTitle = false; //是否标题固定


    boolean jsBridgeSetTitle = false;
    protected EbWebFragment mWebFragment;
    protected BaseWebView mWebView;


    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("initialUrl");
        setContentView(R.layout.web_activity);
        mWebFragment = (EbWebFragment) getSupportFragmentManager().findFragmentById(R.id.webViewFragment);
        mWebView = mWebFragment.getWebView();
        JSBridge.registerJsCallMethod(setPageTitleJsCallMethod);
//        JSBridge.registerJsCallMethod(showShareButtonJsCallMethod);
        setTitle(title);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        mWebView.setWebChromeClient(new WebChromeClientWrapper() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!hasFixedTitle
                        && !jsBridgeSetTitle
                        ) {
                    L.d("url=" + view.getUrl() + " title=" + title);

                    if (!view.getUrl().contains(title)) {
                        setTitle(title);
                    } else {
                        setTitle("");
                    }
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClientWrapper() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        });

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (result != null) {
                    int type = result.getType();
                    if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                        String imgurl = result.getExtra();

                        ImageUtil.showSaveImageToAlbum(EbWebActivity.this, imgurl);
                    }
                }
                return false;
            }
        });
        if (null != url) {
            mWebView.loadUrl(url);
        }
    }


    public void setHasFixedTitle(boolean fixedTitle) {
        this.hasFixedTitle = fixedTitle;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSBridge.unRegisterJsCallMethod(setPageTitleJsCallMethod.getMethodName());
//        JSBridge.unRegisterJsCallMethod(showShareButtonJsCallMethod.getMethodName());
    }

    IJsCallMethod setPageTitleJsCallMethod = new IJsCallMethod() {
        @Override
        public String getMethodName() {
            return "setPageTitle";
        }

        @Override
        public BridgeResult execute(WebView webView, String paramsJsonStr, JSCallbackApplier jsCallbackApplier) throws Throwable {
            try {
                JSONObject jsonObject = new JSONObject(paramsJsonStr);
                String title = jsonObject.getString("title");
                jsBridgeSetTitle = true;
                setTitle(title);

            } catch (JSONException e) {
                e.printStackTrace();
                throw new ParamErrorException();
            }
            BridgeResult result = BridgeResult.NativeResult.success((String) null);
            return result;
        }
    };

    String shareParams;

//    IJsCallMethod showShareButtonJsCallMethod = new IJsCallMethod() {
//        @Override
//        public String getMethodName() {
//            return "showShareButton";
//        }
//
//        @Override
//        public BridgeResult execute(WebView webView, String paramsJsonStr, JSCallbackApplier jsCallbackApplier) throws Throwable {
//
//            try {
//                L.e("IJsCallMethod showShareButton");
//                shareParams = paramsJsonStr;
//                JSONObject jsonObject = new JSONObject(shareParams);
//                String showFlag = jsonObject.getString("showFlag");
//                if (showFlag.equals("0")) {
//                    hiddenShareMenu();
//                } else if (showFlag.equals("1")) {
//                    showShareMenu();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new ParamErrorException();
//            }
//            BridgeResult result = BridgeResult.NativeResult.success((String) null);
//            return result;
//        }
//    };


    @Override
    public void onBackPressed() {
        if (mWebFragment != null && mWebFragment.onBackPressed()) return;
        super.onBackPressed();
    }


}
