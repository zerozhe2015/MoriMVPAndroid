package com.moriarty.morimvpandroid.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.moriarty.base.ui.BaseAppBarActivity;
import com.moriarty.morimvpandroid.R;


/**
 * Created by liuzhe on 2017/8/3.
 */

public class HomeActivity extends BaseAppBarActivity {


    HomeFragment homeFragment;

    public static final String HOME_FRAGMENT_TAG = "HOME_FRAGMENT_TAG";
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addBaseView();


        fragmentManager = getSupportFragmentManager();
        setTitle(R.string.app_name);
        homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().
                add(getBaseViewContentContainer().getId(), homeFragment, HOME_FRAGMENT_TAG).commit();


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
