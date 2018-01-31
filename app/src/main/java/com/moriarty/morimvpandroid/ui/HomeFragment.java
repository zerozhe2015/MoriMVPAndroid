package com.moriarty.morimvpandroid.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moriarty.base.di.ActivityModule;
import com.moriarty.base.ui.BaseFragment;
import com.moriarty.morimvpandroid.DIHelper;
import com.moriarty.morimvpandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liuzhe on 2017/7/31.
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {

    @BindView(R.id.tvMsg)
    TextView tvMsg;


    HomeContract.Presenter mPresenter;
    private Unbinder unbinder;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HomeContract.HomeComponent component = DaggerHomeContract_HomeComponent.builder().appComponent(DIHelper.getAppComponent())
                .homeModule(new HomeContract.HomeModule(this)).activityModule(new ActivityModule(getActivity())).build();
        HomeContract.Presenter presenter = component.getPresenter();
        mPresenter = presenter;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        mPresenter.fetchData();
        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void updateUI(String msg) {
        tvMsg.setText(msg);
    }
}
