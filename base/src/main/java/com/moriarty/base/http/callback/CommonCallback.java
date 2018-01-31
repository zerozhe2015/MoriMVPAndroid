package com.moriarty.base.http.callback;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.moriarty.base.exception.ErrorHandler;
import com.moriarty.base.ui.interfaces.LoadingHandler;
import com.moriarty.base.ui.notify.UiTip;


/**
 * 最常用的一些网络请求回调方式的实现
 */
public abstract class CommonCallback<T> extends SimpleCallback<T> {


    private LoadingHandler mLoadingHandler;

    private ErrorHandler mErrorHandler;




    public CommonCallback(Activity activity) {
        this(activity, new LoadingHandler.DialogLoadingHandler(activity), new ErrorHandler.DialogErrorHandler(activity));
    }

    public CommonCallback(Activity activity, boolean loadingHandlerCancelable) {
        this(activity, new LoadingHandler.DialogLoadingHandler(activity, loadingHandlerCancelable), new ErrorHandler.DialogErrorHandler(activity));
    }

    public CommonCallback(Activity activity, LoadingHandler loadingHandler) {
        this(activity, loadingHandler, new ErrorHandler.DialogErrorHandler(activity));
    }

    public CommonCallback(Activity activity, ErrorHandler errorHandler) {
        this(activity, new LoadingHandler.DialogLoadingHandler(activity), errorHandler);
    }


    public CommonCallback(Fragment fragment) {
        this(fragment, new LoadingHandler.DialogLoadingHandler(fragment.getActivity()), new ErrorHandler.DialogErrorHandler(fragment.getActivity()));
    }

    public CommonCallback(Fragment fragment, boolean loadingHandlerCancelable) {
        this(fragment, new LoadingHandler.DialogLoadingHandler(fragment.getActivity(), loadingHandlerCancelable), new ErrorHandler.DialogErrorHandler(fragment.getActivity()));
    }

    public CommonCallback(Fragment fragment, LoadingHandler loadingHandler) {
        this(fragment, loadingHandler, new ErrorHandler.DialogErrorHandler(fragment.getActivity()));
    }

    public CommonCallback(Fragment fragment, ErrorHandler errorHandler) {
        this(fragment, new LoadingHandler.DialogLoadingHandler(fragment.getActivity()), errorHandler);
    }


    public CommonCallback(Object owner, LoadingHandler loadingHandler, ErrorHandler errorHandler) {
        super(owner);
        init(loadingHandler, errorHandler);
    }


    private void init(LoadingHandler loadingHandler, ErrorHandler errorHandler) {

        this.mLoadingHandler = loadingHandler;
        this.mErrorHandler = errorHandler;

        // 绑定DialogLoadingHandler 的loadingDialog 取消时 取消掉该请求
        if (loadingHandler instanceof LoadingHandler.DialogLoadingHandler) {
            ((LoadingHandler.DialogLoadingHandler) loadingHandler).setOnCancelListener((dialogInterface) -> {
                cancel();
            });
        }
    }


    public LoadingHandler getLoadingHandler() {
        return mLoadingHandler;
    }

    public void setLoadingHandler(LoadingHandler loadingHandler) {
        this.mLoadingHandler = loadingHandler;
    }

    public ErrorHandler getErrorHandler() {
        return mErrorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.mErrorHandler = errorHandler;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (mLoadingHandler != null) {
                mLoadingHandler.showLoading();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        try {
            if (mLoadingHandler != null) {
                mLoadingHandler.dismissLoading();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    @Override
    public void onFault(Throwable e) {
        super.onFault(e);

        try {
            if (mErrorHandler != null) {
                mErrorHandler.handError(e);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void onNoNetwork() {
        super.onNoNetwork();
        try {
            UiTip.toast("无网络连接");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void onCancel() {
        super.onCancel();
        try {
            if (mLoadingHandler != null) {
                mLoadingHandler.dismissLoading();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
