package com.moriarty.base.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;

import com.moriarty.base.di.HasComponentActivity;
import com.moriarty.base.http.RetrofitHelper;
import com.moriarty.base.log.L;
import com.moriarty.base.ui.interfaces.StateView;


public class BaseFragment extends Fragment implements StateView {
    private static final boolean logFragmentInfo = false;


    final String classSimpleName = getClass().getSimpleName() + hashCode();

    static final int NO_LAYOUT_RES = -1;

    int mLayoutResId = NO_LAYOUT_RES;

    private LayoutInflater mLayoutInflater;

    Runnable mOnPostViewCreateCallback;
    Runnable mOnStartCallback;
    boolean mViewDestroyed = false;

//    public LayoutInflater getLayoutInflater() {
//        if (mLayoutInflater == null) {
//            mLayoutInflater = LayoutInflater.from(getContext());
//        }
//        return mLayoutInflater;
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (logFragmentInfo) {
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onAttach");
        }
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (logFragmentInfo) {
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onAttachFragment");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (logFragmentInfo) {
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onCreate");
            L.logBundle(classSimpleName + " Arguments:", getArguments());
            L.logBundle(classSimpleName + " SavedInstanceState: ", savedInstanceState);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (logFragmentInfo)
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onCreateView");

        if (mLayoutInflater == null) {
            mLayoutInflater = inflater;
        }
        if (getLayoutResId() != NO_LAYOUT_RES) {
            return inflater.inflate(getLayoutResId(), container, false);
        }
        mViewDestroyed = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (logFragmentInfo)
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onViewCreated");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (logFragmentInfo)
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onActivityCreated");
        if (mOnPostViewCreateCallback != null) {
            mOnPostViewCreateCallback.run();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (logFragmentInfo) L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onStart");
        if (mOnStartCallback != null) {
            mOnStartCallback.run();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (logFragmentInfo)
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        if (logFragmentInfo) L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (logFragmentInfo) L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onStop");
    }

    @Override
    public void onDestroyView() {
        RetrofitHelper.cancelRequest(this);
        super.onDestroyView();
        if (logFragmentInfo)
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onDestroyView");
        mViewDestroyed = true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (logFragmentInfo)
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onDestroy");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (logFragmentInfo)
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onDetach");
    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        if (logFragmentInfo) L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onCreateAnimation");
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        if (logFragmentInfo) L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        if (logFragmentInfo) L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onViewStateRestored");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (logFragmentInfo) {
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onConfigurationChanged");
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (logFragmentInfo) {
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onHiddenChanged hidden = " + hidden);
        }
    }


    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        if (logFragmentInfo) {
            L.d("LifeCycle", getClass().getSimpleName() + hashCode() + " onInflate ");
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (logFragmentInfo) {
            L.e("LifeCycle", getClass().getSimpleName() + hashCode() + " onLowMemory ");
        }
    }


    /**
     * 必须在 onCreateView 生命周期之前的方法调用
     *
     * @param layoutResId
     */
    public void setContentView(int layoutResId) {
        mLayoutResId = layoutResId;
    }

    protected int getLayoutResId() {
        return mLayoutResId;
    }


    public <T extends View> T findViewById(int id) {
        return (T) getView().findViewById(id);
    }


    /**
     * Gets a component for dependency injection by its type.
     * 为了方便在Fragment调用在Activity中实例化的component
     * 如果所在的activity没有实现HasComponent接口，请勿调用此方法
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {

        return componentType.cast(((HasComponentActivity<C>) getActivity()).getComponent());
    }

    @Override
    public boolean isViewCreated() {
        return getView() != null;
    }

    @Override
    public boolean isViewLayoutCompleted() {
        View fragmentView = getView();
        return fragmentView != null && (fragmentView.getWidth() != 0 || fragmentView.getHeight() != 0);
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
        View fragmentView = getView();
        if (fragmentView != null && !isViewLayoutCompleted()) {
            fragmentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    r.run();
                    fragmentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }
    }

    @Override
    public void setOnStartCallback(Runnable r) {
        mOnStartCallback = r;
    }
}
