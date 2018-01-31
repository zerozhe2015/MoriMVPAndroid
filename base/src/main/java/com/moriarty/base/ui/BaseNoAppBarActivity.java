package com.moriarty.base.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.moriarty.base.R;


public abstract class BaseNoAppBarActivity extends BaseActivity {
    LayoutInflater mLayoutInflater;
    FrameLayout mContentViewContainer;


    //    View mTopDividerLine;
    Toolbar mToolbar;
    TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutInflater = getLayoutInflater();
    }


    @Override
    public void setContentView(int layoutResID) {
        addBaseView();
        mLayoutInflater.inflate(layoutResID, mContentViewContainer);

    }


    @Override
    public void setContentView(View view) {
        addBaseView();
        mContentViewContainer.addView(view);

    }


    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        addBaseView();
        mContentViewContainer.addView(view, params);

    }


    protected void addBaseView() {
        super.setContentView(R.layout.activity_base);
        findAndSetActivityTopBar();
        mContentViewContainer = (FrameLayout) findViewById(R.id.contentViewContainer);
    }


    private void findAndSetActivityTopBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.GONE);
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
//            mToolbar.setNavigationIcon(R.drawable.icon_navi_back);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_navi_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mTitleTextView = (TextView) findViewById(R.id.topTitle);
        if (null != mTitleTextView) {
            mTitleTextView.setText(getTitle());
        }

//        mTopDividerLine = findViewById(R.id.topDividerLine);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
//        new Thread(() -> new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)).start();
        return true;
    }


    public void hideAppBar(boolean isHide) {
        mToolbar.setVisibility(isHide ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (null != mTitleTextView) {
            mTitleTextView.setText(title);
        }
    }

    public void setContentViewNoBase(int layoutResID) {
        super.setContentView(layoutResID);
        findAndSetActivityTopBar();
    }

    public void setContentViewNoBase(View view) {
        super.setContentView(view);
        findAndSetActivityTopBar();
    }

    public void setContentViewNoBase(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        findAndSetActivityTopBar();
    }

    public FrameLayout getBaseViewContentContainer() {
        return mContentViewContainer;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

//    public View getTopDividerLine() {
//        return mTopDividerLine;
//    }
}
