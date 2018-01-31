package com.moriarty.morimvpandroid.ui;

import android.app.Activity;

import com.moriarty.base.di.PerActivity;
import com.moriarty.base.http.RetrofitHelper;
import com.moriarty.base.http.callback.CommonCallback;
import com.moriarty.morimvpandroid.entity.UserInfo;
import com.moriarty.morimvpandroid.net.ApiServiceManager;

import javax.inject.Inject;

import io.reactivex.Observable;


/**
 * Created by liuzhe on 2017/7/31.
 */

@PerActivity
public class HomePresenter implements HomeContract.Presenter {


    private HomeContract.View mView;
    private Activity mActivity;
    ApiServiceManager mApiServiceManager;


    @Inject
    public HomePresenter(Activity mActivity, HomeContract.View mView, ApiServiceManager apiServiceManager) {
        this.mActivity = mActivity;
        this.mView = mView;
        mApiServiceManager = apiServiceManager;
        mView.setPresenter(this);
    }


    @Override
    public void fetchData() {

        mView.updateUI("欢迎来到Moriarty的MVP项目demo");

        //net();
    }

    private void net() {
        Observable<UserInfo> observable = mApiServiceManager.getCommonApiService().appHomePage();
        RetrofitHelper.enqueue(observable, new CommonCallback<UserInfo>(mActivity) {
            @Override
            public void onSuccess(UserInfo data) {
                // mView.updateUI(data);
            }
        });
    }


}
