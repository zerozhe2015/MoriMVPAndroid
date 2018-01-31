package com.moriarty.base.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.moriarty.base.BaseApplication;
import com.moriarty.base.di.ActivityModule;
import com.moriarty.base.http.RetrofitHelper;
import com.moriarty.base.log.L;
import com.moriarty.base.ui.interfaces.StateView;


public class BaseActivity extends AppCompatActivity implements StateView {
    private static final boolean logActivityInfo = false;
    final String classSimpleName = getClass().getSimpleName();

    boolean mViewCreated = false;
    boolean mViewDestroyed = false;
    Runnable mOnPostViewCreateCallback;
    Runnable mOnStartCallback;

    boolean mUseActivityAnalytics = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        super.onCreate(savedInstanceState);

        BaseApplication.addActivityToList(this);
        if (logActivityInfo) {
            L.logIntent(classSimpleName + " intent:", getIntent());
            L.logBundle(classSimpleName + " savedInstanceState: ", savedInstanceState);
        }
        mViewDestroyed = false;
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mViewCreated = true;
        if (mOnPostViewCreateCallback != null) {
            mOnPostViewCreateCallback.run();
        }
    }


    @Override
    protected void onDestroy() {
        RetrofitHelper.cancelRequest(this);
        BaseApplication.removeActivityFromList(this);
        super.onDestroy();
        mViewCreated = false;
        mViewDestroyed = true;
    }

    public void finishNoAnimation() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mUseActivityAnalytics) {
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mUseActivityAnalytics) {
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mOnStartCallback != null) {
            mOnStartCallback.run();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }

    /**
     * Get an Activity module for dependency injection.
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }


    /**
     * finish自己，返回data结果，并且设置结果为RESULT_OK
     *
     * @param data
     */
    protected void finishActivityAndSetResultOK(Intent data) {
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * finish自己，设置结果为RESULT_CANCELED
     */
    protected void finishActivityAndSetResultCanceled() {
        setResult(RESULT_CANCELED, null);
        finish();
    }


    public View getContentView() {
        return findViewById(android.R.id.content);
    }

    @Override
    public boolean isViewCreated() {
        return mViewCreated;
    }


    @Override
    public boolean isViewLayoutCompleted() {
        View contentView = getContentView();
        return contentView.getHeight() != 0 || contentView.getWidth() != 0;
    }

    @Override
    public boolean isViewDestroyed() {
        return mViewDestroyed;
    }

    @Override
    public void setOnPostViewCreateCallback(Runnable r) {
        mOnPostViewCreateCallback = r;
    }

    @Override
    public void setOnViewFirstLayoutCallback(Runnable r) {
        View contentView = getContentView();
        if (contentView != null && !isViewLayoutCompleted()) {
            contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    r.run();
                    contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }
    }


    @Override
    public void setOnStartCallback(Runnable r) {
        mOnStartCallback = r;
    }

    protected void setUseActivityAnalytics(boolean useActivityAnalytics) {
        this.mUseActivityAnalytics = useActivityAnalytics;
    }
}
