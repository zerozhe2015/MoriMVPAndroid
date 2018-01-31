package com.moriarty.base.ui.notify;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.moriarty.base.BaseApplication;
import com.moriarty.base.R;
import com.moriarty.base.ui.dialog.CommonAlertDialog;
import com.moriarty.base.util.ResUtils;


/**
 */
public class UiTip {


    /**
     * 以toast的形式提示消息
     *
     * @param msg
     */
    public static void toast(CharSequence msg) {
        Toast.makeText(BaseApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();


    }

    /**
     * 以toast的形式提示消息
     *
     * @param msgResId
     */
    public static void toast(int msgResId) {
        Toast.makeText(BaseApplication.getApplication(), msgResId, Toast.LENGTH_SHORT).show();
    }


    /**
     * 以toast的形式提示消息
     *
     * @param msg
     */
    public static void toastLong(CharSequence msg) {
        Toast.makeText(BaseApplication.getApplication(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 以toast的形式提示消息
     *
     * @param msgResId
     */
    public static void toastLong(int msgResId) {
        Toast.makeText(BaseApplication.getApplication(), msgResId, Toast.LENGTH_LONG).show();
    }

    /**
     * 以toast的形式提示消息,显示在窗口中间
     *
     * @param msg
     */
    public static void toastMsgMiddle(CharSequence msg) {
        Toast toast = Toast.makeText(BaseApplication.getApplication(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 以toast的形式提示消息，显示在窗口中间
     */
    public static void toastMsgMiddle(int msgResId) {
        Toast toast = Toast.makeText(BaseApplication.getApplication(), msgResId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 以toast的形式提示消息,显示在窗口中间
     *
     * @param msg
     */
    public static void toastMsgTop(CharSequence msg) {
        Toast toast = Toast.makeText(BaseApplication.getApplication(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, ResUtils.getDimensionPixelOffset(R.dimen.top_bar_height));
        toast.show();
    }

    /**
     * 以toast的形式提示消息，显示在窗口中间
     */
    public static void toastMsgTop(int msgResId) {
        Toast toast = Toast.makeText(BaseApplication.getApplication(), msgResId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, ResUtils.getDimensionPixelOffset(R.dimen.top_bar_height));
        toast.show();
    }


    public void snakbar(View v, CharSequence msg) {


    }


    /**
     * 无点击事件处理，只是提示，底部一个按钮
     *
     * @param context
     * @param msg
     */
    public static Dialog showTipAlertDialog(Context context, CharSequence msg, CharSequence okText) {
//        AlertDialog alertDialog = new AlertDialog.Builder(context).setMessage(msg).setPositiveButton(okText, null).create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
//        return alertDialog;
        CommonAlertDialog dialog = new CommonAlertDialog(context).setMessage(msg).setPositiveButton(okText, null);
        dialog.show();
        return dialog;
    }


    /**
     * 底部一个按钮,一个点击事件
     *
     * @param context
     * @param msg
     */
    public static Dialog showTipAlertDialog(Context context, CharSequence msg, CharSequence okText, DialogInterface.OnClickListener clickListener) {
//        AlertDialog alertDialog = new AlertDialog.Builder(context).setMessage(msg).setPositiveButton(okText, clickListener).create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
//        return alertDialog;
        CommonAlertDialog dialog = new CommonAlertDialog(context).setMessage(msg).setPositiveButton(okText, clickListener);
        dialog.show();
        return dialog;
    }


    public static Dialog showTipAlertDialog(Context context, View customView, CharSequence okText, DialogInterface.OnClickListener clickListener) {
        CommonAlertDialog dialog = new CommonAlertDialog(context).setView(customView).setPositiveButton(okText, clickListener);
        dialog.show();
        return dialog;
    }


    public static Dialog showConfirmAlertDialog(Context context, CharSequence msg, CharSequence okText, DialogInterface.OnClickListener okOnClickListener,
                                                CharSequence cancelText, DialogInterface.OnClickListener cancelOnClickListener) {
//        AlertDialog alertDialog = new AlertDialog.Builder(context).setMessage(msg)
//                .setPositiveButton(okText, okOnClickListener).setNegativeButton(cancelText, cancelOnClickListener)
//                .create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
//        return alertDialog;

        CommonAlertDialog dialog = new CommonAlertDialog(context).setMessage(msg).setPositiveButton(okText, okOnClickListener).setNegativeButton(cancelText, cancelOnClickListener);
        dialog.show();
        return dialog;

    }


    public static Dialog showConfirmAlertDialog(Context context, View customView, CharSequence okText, DialogInterface.OnClickListener okOnClickListener,
                                                CharSequence cancelText, DialogInterface.OnClickListener cancelOnClickListener) {

//        AlertDialog alertDialog = new AlertDialog.Builder(context).setView(customView)
//                .setPositiveButton(okText, okOnClickListener).setNegativeButton(cancelText, cancelOnClickListener)
//                .create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
//        return alertDialog;
        CommonAlertDialog dialog = new CommonAlertDialog(context).setView(customView).setPositiveButton(okText, okOnClickListener).setNegativeButton(cancelText, cancelOnClickListener);
        dialog.show();
        return dialog;

    }


    public void showNotification() {

    }
}