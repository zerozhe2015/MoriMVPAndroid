package com.moriarty.morimvpandroid;

import android.app.Dialog;

import com.moriarty.base.BaseApplication;
import com.moriarty.base.exception.ErrorInterceptor;
import com.moriarty.base.http.domain.DomainResult;
import com.moriarty.base.http.domain.FailedResultError;
import com.moriarty.base.ui.BaseActivity;


public class CommonErrorInterceptor implements ErrorInterceptor {

    @Override
    public boolean interceptError(Throwable throwable) {

        if (throwable instanceof FailedResultError) {
            FailedResultError failedResultError = (FailedResultError) throwable;
            //sessionId 失效
            if (DomainResult.CODE_FORCE_UPDATE.equals(failedResultError.getDomainResult().code)) {
                onForceUpdate(failedResultError);
                return true;
            }
        }

        return false;
    }


    Dialog mSessionIdInvalidatedDialog;


    /**
     * 关闭登录后才显示的页面
     */

    private void finishLoginedPage() {
        BaseApplication.clearOtherActivityUnless(MainActivity.class);
//        BaseApplication.clearAllActivity();
//        startActivity(new Intent(this, homeActivityClass));

    }


    private void onForceUpdate(FailedResultError failedResultError) {
        BaseActivity activity = BaseApplication.getTopActivity();
        if (activity != null) {
//            AppUpdateMSg appUpdateMSg = null;
//            try {
//                appUpdateMSg = new Gson().fromJson(failedResultError.getDomainResult().dataJsonStr, AppUpdateMSg.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (appUpdateMSg != null) {
//                new AppUpdateDialog(activity, appUpdateMSg,R.style.Dialog).show();
//            }
        }
    }


}
