package com.moriarty.base.ui.interfaces;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.moriarty.base.ui.dialog.LoadingDialog;


public interface LoadingHandler {

    void showLoading();

    void dismissLoading();

    /**
     * 默认实现的以laoding dialog的方式展示loading
     */

    class DialogLoadingHandler implements LoadingHandler {
        private Activity mActivity;
        private Dialog mLoadingDialog;

//        DialogFragment dialogFragment;

        public DialogLoadingHandler(Activity activity) {
            this(activity, true);
        }

        public DialogLoadingHandler(Activity activity, boolean cancelable) {
            this.mActivity = activity;
            mLoadingDialog = createLoadingDialog(activity);
            //如果可取消，则只能点后退键取消，不能通过点击外面取消
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setCancelable(cancelable);
//            dialogFragment = new DialogFragment() {
//                @Override
//                public Dialog onCreateDialog(Bundle savedInstanceState) {
//                    return mLoadingDialog;
//                }
//            };
        }

        /**
         * 创建对话框
         * 此类子类可以重写此方法
         */
        protected Dialog createLoadingDialog(Activity activity) {
            return new LoadingDialog(activity);
        }


        public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            mLoadingDialog.setOnCancelListener(onCancelListener);
        }


        @Override
        public void showLoading() {
            if (!mLoadingDialog.isShowing()) {
                mLoadingDialog.show();
//                dialogFragment.show(mActivity.getFragmentManager(), "loading");
            }
        }

        @Override
        public void dismissLoading() {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
//                dialogFragment.dismiss();
            }
        }
    }
}
