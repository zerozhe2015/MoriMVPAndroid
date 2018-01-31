package com.moriarty.base.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public abstract class SingleFragmentActivity<T extends BaseFragment> extends BaseNoAppBarActivity {


    protected T mFragment;


    protected abstract T createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBaseView();
        FragmentManager fm = getSupportFragmentManager();
        mFragment = (T) fm.findFragmentById(getBaseViewContentContainer().getId());
        if (mFragment == null) {
            mFragment = createFragment();
            fm.beginTransaction().add(getBaseViewContentContainer().getId(), mFragment).commit();
        }
    }


    public T getFragment() {
        return mFragment;
    }
}
