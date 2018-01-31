package com.moriarty.base;

import android.app.Application;

import com.moriarty.base.http.RetrofitHelper;
import com.moriarty.base.log.L;
import com.moriarty.base.ui.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



public abstract class BaseApplication extends Application {
    private static BaseApplication mApplication;
    private static List<WeakReference<BaseActivity>> activitylist = new LinkedList<>();



    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initializeLeakDetection();
//        AnalyticsFactory.getInstance().initInApplication(this);

//        MsImage.initImageFramework(this);
        //初始化全局异常
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }

    public static Application getApplication() {
        return mApplication;
    }


    private void initializeLeakDetection() {
//        if (BuildConfig.DEBUG) {
//            RefWatcher refWatcher = LeakCanary.install(this);
//        }
    }


    public static void addActivityToList(BaseActivity baseActivity) {
        activitylist.add(new WeakReference<>(baseActivity));
    }


    public static void removeActivityFromList(BaseActivity baseActivity) {
        Iterator<WeakReference<BaseActivity>> iterator = activitylist.iterator();
        while (iterator.hasNext()) {
            WeakReference<BaseActivity> reference = iterator.next();
            BaseActivity a = reference.get();
            if (a != null && a == baseActivity) {
                iterator.remove();
                return;
            }
        }
    }


    public static void closeAllActivity() {

        Iterator<WeakReference<BaseActivity>> iterator = activitylist.iterator();
        while (iterator.hasNext()) {
            WeakReference<BaseActivity> reference = iterator.next();
            BaseActivity activity = reference.get();
            iterator.remove();
            if (activity != null && !activity.isFinishing()) {
                activity.finishNoAnimation();
            }

        }
        activitylist.clear();
    }

    public static void clearOtherActivityUnless(Class... activityClasses) {
        List<Class> classList = Arrays.asList(activityClasses);
        Iterator<WeakReference<BaseActivity>> iterator = activitylist.iterator();
        while (iterator.hasNext()) {
            WeakReference<BaseActivity> reference = iterator.next();
            BaseActivity activity = reference.get();
            if (activity == null) {
                iterator.remove();
            } else if (!classList.contains(activity.getClass())) {
                iterator.remove();
                if (!activity.isFinishing()) {
                    activity.finishNoAnimation();
                }
            }
        }

    }

    public static <T> void closeActivity(Class<T> clazz) {
        Iterator<WeakReference<BaseActivity>> iterator = activitylist.iterator();
        while (iterator.hasNext()) {
            WeakReference<BaseActivity> reference = iterator.next();
            BaseActivity activity = reference.get();
            if (activity == null) {
                iterator.remove();
            } else if (activity.getClass() == clazz) {
                iterator.remove();
                if (!activity.isFinishing()) {
                    activity.finishNoAnimation();
                }
            }
        }

    }

    public static BaseActivity getTopActivity() {
        if (activitylist == null || activitylist.isEmpty()) return null;
        for (int i = activitylist.size() - 1; i >= 0; i--) {
            BaseActivity activity = activitylist.get(i).get();
            if (activity == null || activity.isFinishing()) {
                continue;
            }
            L.d("topActivity : " + activity.getClass().getSimpleName());
            return activity;
        }
        return null;
    }

    public static void exit() {
        RetrofitHelper.cancelAllRequest();
        closeAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
