package com.moriarty.morimvpandroid.ui;

import com.moriarty.base.di.ActivityModule;
import com.moriarty.base.di.PerActivity;
import com.moriarty.base.mvp.BasePresenter;
import com.moriarty.base.mvp.StateMVPView;
import com.moriarty.morimvpandroid.AppComponent;

import dagger.Component;
import dagger.Provides;

/**
 * Created by liuzhe on 2017/7/31.
 */

public interface HomeContract {
    interface View extends StateMVPView<Presenter> {

        void updateUI(String msg);
    }

    interface Presenter extends BasePresenter {


        void fetchData();


    }


    @PerActivity
    @Component(dependencies = AppComponent.class, modules = {ActivityModule.class, HomeModule.class})
    interface HomeComponent {
        HomePresenter getPresenter();
    }


    @dagger.Module
    class HomeModule {

        View mView;

        public HomeModule(View view) {
            this.mView = view;
        }

        @PerActivity
        @Provides
        HomeContract.View provideView() {

            return mView;
        }
    }
}
