package com.moriarty.base.exception;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;

import com.moriarty.base.ui.notify.UiTip;


public interface ErrorHandler {

    void handError(Throwable e);

    class DialogErrorHandler implements ErrorHandler {

        Activity mActivity;
        boolean mIsFinshActivty = false; //点击确定是否关闭Activity;
        DialogInterface.OnClickListener mOkBtnClickListener;

        public DialogErrorHandler(Activity mActivity) {
            this(mActivity, false);
        }

        public DialogErrorHandler(Activity mActivity, boolean isFinshActivty) {
            this(mActivity, isFinshActivty, null);
        }


        public DialogErrorHandler(Activity mActivity, DialogInterface.OnClickListener okBtnClickListener) {
            this(mActivity, false, okBtnClickListener);
        }

        public DialogErrorHandler(Activity mActivity, boolean isFinshActivty, DialogInterface.OnClickListener okBtnClickListener) {
            this.mActivity = mActivity;
            this.mIsFinshActivty = isFinshActivty;
            this.mOkBtnClickListener = okBtnClickListener;
        }


        @Override
        public void handError(Throwable e) {
            if (mActivity == null || mActivity.isFinishing()
                    || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
                return;
            }


            Dialog tipAlertDialog = UiTip.showTipAlertDialog(mActivity, ErrorMessageFactory.getErrorMsg(e), "确定", (dialog, which) -> {
                onOkBtnClick(dialog, which);
                if (mIsFinshActivty) {
                    mActivity.finish();
                }
            });
            tipAlertDialog.setOnCancelListener(dialog -> {
                if (mIsFinshActivty) {
                    mActivity.finish();
                }
            });
        }

        protected void onOkBtnClick(DialogInterface dialogInterface, int which) {
            if (null != mOkBtnClickListener) {
                mOkBtnClickListener.onClick(dialogInterface, which);
            }
        }

    }

    class ToastErrorHandler implements ErrorHandler {
        @Override
        public void handError(Throwable volleyError) {
            UiTip.toast(ErrorMessageFactory.getErrorMsg(volleyError));
        }
    }


}
