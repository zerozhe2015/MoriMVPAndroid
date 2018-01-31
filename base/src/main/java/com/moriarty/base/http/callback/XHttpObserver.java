package com.moriarty.base.http.callback;

import com.moriarty.base.exception.ErrorInterceptor;
import com.moriarty.base.http.RetrofitHelper;
import com.moriarty.base.log.L;
import com.moriarty.base.util.DeviceInfoUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public abstract class XHttpObserver<T> implements Observer<T>, XCallback<T> {


    private Disposable mDisposable;

    private int mOwnerId;

    private boolean mResultDataCanBeNull = false;


    public XHttpObserver(Object owner) {
        this.mOwnerId = getOwnerId(owner);
    }


    @Override
    public final void onSubscribe(@NonNull Disposable d) {
        L.d("onSubscribe");

        if (!DeviceInfoUtils.isNetworkAvailable()) {
            onNoNetwork();
            d.dispose();
            return;
        }
        mDisposable = d;
        addDisposable(this);
        onStart();
    }

    @Override
    public final void onNext(@NonNull T t) {
        L.d("onNext");
        if (!mDisposable.isDisposed()) {
            onSuccess(t);
        }
    }

    @Override
    public final void onError(@NonNull Throwable e) {
        L.d("onError");
        removeDisposable(this);
        if (!mDisposable.isDisposed()) {

//由于rxJava 2.0 强制不能发送null，发送null会强制抛 NullPointerException  lift操作也不行  具体异常详见下面  故在这里处理业务允许结果为空 视为成功的情况
//   java.lang.NullPointerException: Null is not a valid element
//   at io.reactivex.internal.queue.SpscLinkedArrayQueue.offer(SpscLinkedArrayQueue.java:69)

// at io.reactivex.internal.operators.observable.ObservableMap$MapObserver.onNext(ObservableMap.java:59)
            if (mResultDataCanBeNull && e instanceof NullPointerException
                    && ("Null is not a valid element".equals(e.getMessage()) || "The mapper function returned a null value.".equals(e.getMessage()))) {
                onSuccess(null);
                onFinish();
                return;
            }


            if (RetrofitHelper.commonErrorInterceptors != null) {
                for (ErrorInterceptor errorInterceptor : RetrofitHelper.commonErrorInterceptors) {
                    if (errorInterceptor.interceptError(e)) {
                        onFinish();
                        return;
                    }
                }
            }

            onFault(e);
            onFinish();
        }
    }

    @Override
    public final void onComplete() {
        L.d("onCompleted");
        removeDisposable(this);
        if (!mDisposable.isDisposed()) {
            onFinish();
        }
    }


    public final boolean isCanceled() {
        return mDisposable.isDisposed();
    }


    public final void cancel() {
        cancel(true);
    }


    private void cancel(boolean removeInList) {
        mDisposable.dispose();
        onCancel();
        if (removeInList) {
            removeDisposable(this);
        }
    }


    public XHttpObserver<T> setResultDataCanBeNull(boolean resultDataCanBeNull) {
        this.mResultDataCanBeNull = resultDataCanBeNull;
        return this;
    }


    private static int getOwnerId(Object owner) {
        return System.identityHashCode(owner);
    }










    /*--------------------------------------------------------------------------------------------*/


    private static List<XHttpObserver> currentXHttpObservers = new ArrayList<>();


    private static void addDisposable(XHttpObserver disposable) {
        if (!currentXHttpObservers.contains(disposable)) {
            currentXHttpObservers.add(disposable);
        }
    }

    private static void removeDisposable(XHttpObserver disposable) {
        currentXHttpObservers.remove(disposable);
    }


    /**
     * 取消所有的request
     *
     * @param owner
     */

    public static void cancel(Object owner) {
        Iterator<XHttpObserver> iterator = currentXHttpObservers.iterator();
        while (iterator.hasNext()) {
            XHttpObserver xHttpObserver = iterator.next();
            if (xHttpObserver.mOwnerId == getOwnerId(owner)) {
                if (xHttpObserver.isCanceled()) {
                    iterator.remove();
                } else {
                    xHttpObserver.cancel(false);
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 取消该宿主相关的请求request
     */
    public static void cancelAll() {
        Iterator<XHttpObserver> iterator = currentXHttpObservers.iterator();
        while (iterator.hasNext()) {
            XHttpObserver xHttpObserver = iterator.next();

            if (xHttpObserver.isCanceled()) {
                iterator.remove();
            } else {
                xHttpObserver.cancel(false);
                iterator.remove();
            }
        }
    }

}
