package com.moriarty.base.eventbus;

//import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

import static dagger.internal.Preconditions.checkNotNull;


/**
 * 使用rxJava 封装的eventBus
 * <p>
 * 怎么去使用？
 * 在需要发送消息的地方
 * <p>
 * RxBus.getInstance().post("SomeChange");
 * 未封装的调用方式如下 （已添加register和unRegister方法）
 * 在需要接收消息的地方
 * <p>
 * Subscription mSubscription = RxBus.getInstance().toObserverable(String.class).subscribe(new Action1<String>() {
 *
 * @Override public void call(String s) {
 * <p>
 * }
 * });
 * 不要忘了在适当的地方去取消这个订阅（以免发生内存泄漏）
 * mSubscription.unsubscribe();
 */

public class RxBus {

    private final FlowableProcessor<Object> bus;
    private final Map<IListener<?>, Disposable> map = new HashMap<>();

    private static class RxBusHolder {
        private static final RxBus INSTANCE = new RxBus();
    }

    private RxBus() {
        bus = PublishProcessor.create().toSerialized();
    }

    public static synchronized RxBus getInstance() {
        return RxBusHolder.INSTANCE;
    }

    public void post(Object o) {
        bus.onNext(o);
    }

    public <T> Flowable<T> toFlowable(Class<T> eventType) {
        return bus.ofType(eventType);
    }


    /**
     * 订阅事件
     *
     * @param eventType 事件类型class
     * @param iListener 监听事件回调
     * @param <T>
     * @return
     */
    public <T> Disposable register(@NonNull Class<T> eventType, @NonNull IListener<T> iListener) {
        return register(eventType, iListener, AndroidSchedulers.mainThread());
    }


    /**
     * 订阅事件并指定执行线程
     *
     * @param eventType 事件类型class
     * @param listener  监听事件回调
     * @param scheduler observeOn的scheduler
     * @param <T>
     * @return
     */
    public <T> Disposable register(@NonNull Class<T> eventType, @NonNull IListener<T> listener, @Nullable Scheduler scheduler) {
        checkNotNull(eventType, "eventType cannot be null ");
        checkNotNull(listener, "listener cannot be null ");

        Flowable<T> flowable = toFlowable(eventType);
        if (null != scheduler) {
            flowable = flowable.observeOn(scheduler);
        }
//        Disposable disposable = flowable.subscribe(listener::onReceiveEvent);

        //先简单处理异常。在调用的时候不适用并发
        Disposable disposable = flowable.subscribe(listener::onReceiveEvent, (Throwable throwable) -> {
                    throwable.printStackTrace();
                    if (map.get(listener) != null) {
                        register(eventType, listener, scheduler);
                    }
                }
        );
        map.put(listener, disposable);
        return disposable;
    }


    public <T> void unRegister(@NonNull IListener<T> listener) {
        checkNotNull(listener, "listener cannot be null ");

        Disposable disposable = map.get(listener);
        if (null != disposable) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
            map.remove(listener);
        }
    }
}