package com.moriarty.base.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.moriarty.base.R;
import com.moriarty.base.ui.interfaces.LoadingHandler;


/**
 */
public class BaseViewFragment extends BaseFragment implements LoadingHandler {


    private ViewGroup mFragmentContentView;
    private RelativeLayout mLoadingLayout;
    private View mContentView;
    private LayoutInflater mLayoutInflater;

    /**
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mLayoutInflater == null) {
            mLayoutInflater = inflater;
        }
        View baseView = inflater.inflate(R.layout.fragment_base, container, false);
        mFragmentContentView = (ViewGroup) baseView.findViewById(R.id.fragment_content);
        mLoadingLayout = (RelativeLayout) baseView.findViewById(R.id.loading_layout);

//        mLoadingLayout.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                return false;
//            }
//        });

          mContentView = onCreateContentView(inflater, container, savedInstanceState);

        if (mContentView != null) {

            mFragmentContentView.addView(mContentView, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        return baseView;
    }


    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutResId() != NO_LAYOUT_RES) {
            return inflater.inflate(getLayoutResId(), container, false);
        }
        return null;
    }


    public  View getContentView(){
        return mContentView;
    }

    @Override
    public void showLoading() {
        mLoadingLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void dismissLoading() {
        mLoadingLayout.setVisibility(View.GONE);
    }
}
