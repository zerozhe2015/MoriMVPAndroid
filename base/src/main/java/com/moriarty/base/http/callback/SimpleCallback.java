package com.moriarty.base.http.callback;


import com.moriarty.base.log.L;

/**
 * 简单的回调，里面什么都没做，一些用户不可见的网络请求可以使用此类
 */
public abstract class SimpleCallback<T> extends XHttpObserver<T> {


//    public SimpleCallback() {
//
//    }

    public SimpleCallback(Object mOwner) {
        super(mOwner);
    }

    @Override
    public void onStart() {
        L.d("onStart   is called");
    }

    @Override
    public void onFinish() {
        L.d("onFinish   is called");
    }


    @Override
    public void onFault(Throwable throwable) {
        L.d("onFault   is called");
        throwable.printStackTrace();
    }


    @Override
    public void onNoNetwork() {
        L.d("onNoNetwork   is called");
    }

    @Override
    public void onCancel() {
        L.d("onCancel   is called");
    }
}
