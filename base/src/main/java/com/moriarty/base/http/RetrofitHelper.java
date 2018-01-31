package com.moriarty.base.http;

import com.moriarty.base.exception.ErrorInterceptor;
import com.moriarty.base.http.callback.SimpleCallback;
import com.moriarty.base.http.callback.XHttpObserver;
import com.moriarty.base.http.domain.DomainGsonConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitHelper {

    public static OkHttpClient createOkHttpClient(Interceptor interceptor) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor).addNetworkInterceptor(loggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).retryOnConnectionFailure(false)
                .build();
        return okHttpClient;
    }

    public static Retrofit createRetrofit(String baseUrl, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl)
                .addConverterFactory(DomainGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }


    /**
     * 执行网络请求 获取数据
     *
     * @param observable
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> void enqueue(Observable<T> observable, XHttpObserver<T> callback) {
        enqueue(observable, callback, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    /**
     * 执行网络请求 获取数据
     * callback传null，则什么错误都不处理，也不拦截错误。如果要不拦截错误，则callback 传null
     *
     * @param observable
     * @param observer
     * @param <T>
     * @return
     */
    public static <T> void enqueue(Observable<T> observable, XHttpObserver<T> observer,
//                                   boolean canResultDataBeNull,
                                   Scheduler subscribeOn, Scheduler observeOn) {

        if (observer == null) {
            observer = new SimpleCallback<T>(null) {
                @Override
                public void onSuccess(T data) {
                }
            };
        }


        //结果是否能为空处理
//        if (canResultDataBeNull) {
//        observable = processNullResult(observable);
//        }


        observable
//                .map((t) -> {
//                    SystemClock.sleep(1500);
//                    return t;
//                })
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .subscribe(observer);


    }

/*    @NonNull
    private static <T> Observable<T> processNullResult(Observable<T> resultDataObservable) {
        return resultDataObservable
                .lift(observer -> new Observer<T>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        observer.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull T t) {
                        observer.onNext(t);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
//                        java.lang.NullPointerException: Null is not a valid element
//                          at io.reactivex.internal.queue.SpscLinkedArrayQueue.offer(SpscLinkedArrayQueue.java:69)
                        if (e instanceof NullPointerException && "Null is not a valid element".equals(e.getMessage())) {
                            observer.onNext(null);
                            observer.onComplete();
                            return;
                        }

                        observer.onError(e);

                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }*/

/*    @NonNull
    private static <T> Observable<T> processNullResult(Observable<T> resultDataObservable) {
        return resultDataObservable
                .lift(new Operator<T, T>() {
                    @Override
                    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
                        return new Subscriber<T>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(T t) {
                                if (t != null) {
                                    subscriber.onNext(t);
                                } else {
                                    subscriber.onError(new NullResultDataError());
                                }
                            }
                        };
                    }
                });
    }*/


/*    */
    /**
     * 执行网络请求 获取数据
     *//*
    public static <T> Subscription enqueueForConstrainedResult(Observable<ConstrainedResult<T>> observable, XCallback<T> callback) {
        return enqueue(liftConstrainedResult(observable), callback);
    }

    @NonNull
    private static <T> Observable<T> liftConstrainedResult(Observable<ConstrainedResult<T>> observable) {
        return observable
                .lift(new Operator<T, ConstrainedResult<T>>() {
                    @Override
                    public Subscriber<? super ConstrainedResult<T>> call(Subscriber<? super T> subscriber) {
                        return new Subscriber<ConstrainedResult<T>>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(ConstrainedResult<T> constrainedResult) {
                                if (constrainedResult.isSuccess()) {
                                    subscriber.onNext(constrainedResult.results);
                                } else {
                                    subscriber.onError(new FailedResultError(constrainedResult));
                                }
                            }
                        };
                    }
        });
    }*/


    public static List<ErrorInterceptor> commonErrorInterceptors;

    public static void addCommonErrorInterceptor(ErrorInterceptor errorInterceptor) {

        if (commonErrorInterceptors == null) {
            commonErrorInterceptors = new ArrayList<>();
        }
        commonErrorInterceptors.add(errorInterceptor);
    }

    public static void removeCommonErrorInterceptor(ErrorInterceptor errorInterceptor) {

        if (commonErrorInterceptors != null && commonErrorInterceptors.contains(errorInterceptor)) {
            commonErrorInterceptors.remove(errorInterceptor);
        }
    }


    public static void cancelRequest(Object owner) {
        XHttpObserver.cancel(owner);
    }

    public static void cancelAllRequest() {
        XHttpObserver.cancelAll();
    }

}
