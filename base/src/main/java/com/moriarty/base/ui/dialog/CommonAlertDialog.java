package com.moriarty.base.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moriarty.base.R;
import com.moriarty.base.util.ResUtils;
import com.moriarty.base.util.VUtils;


public class CommonAlertDialog extends Dialog {

    protected LinearLayout dialogParentView;
    protected TextView messageTv;
    protected FrameLayout customViewLayout;
    protected LinearLayout dialogButtonBar;
    protected View dialogButtonBarHorizontalLine;
    protected Button cancelBtn;
    protected View dialogButtonBarVerticalLine;
    protected Button okBtn;

    protected View closeView;


    public CommonAlertDialog(@NonNull Context context) {
        super(context, R.style.NoFrameDialog);
        setContentView(R.layout.common_alert_dialog);
        this.dialogParentView = (LinearLayout) findViewById(R.id.dialogContentView);
        this.messageTv = (TextView) findViewById(R.id.messageTv);
        this.customViewLayout = (FrameLayout) findViewById(R.id.customViewLayout);
        this.dialogButtonBar = (LinearLayout) findViewById(R.id.dialogButtonBar);
        this.dialogButtonBarHorizontalLine = findViewById(R.id.dialogButtonBarHorizontalLine);
        this.cancelBtn = (Button) findViewById(R.id.cancelBtn);
        this.dialogButtonBarVerticalLine = findViewById(R.id.dialogButtonBarVerticalLine);
        this.okBtn = (Button) findViewById(R.id.okBtn);
        closeView = findViewById(R.id.closeView);

        closeView.setOnClickListener(v -> dismiss());
        VUtils.setRoundRectSolidBackground(dialogParentView, ResUtils.getColorRes(context, R.color.white), ResUtils.getDimensionPixelSize(context, R.dimen.dis2));
        dialogButtonBar.setVisibility(View.GONE);
        dialogButtonBarVerticalLine.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);
        setCanceledOnTouchOutside(false);
    }


    public CommonAlertDialog setMessage(CharSequence msg) {
        messageTv.setText(msg);
        return this;
    }

    public CommonAlertDialog setMessage(int msgId) {
        messageTv.setText(msgId);
        return this;
    }

    public CommonAlertDialog setView(View v) {
        messageTv.setVisibility(View.GONE);
        customViewLayout.addView(v);
        return this;
    }

    public CommonAlertDialog setView(int layoutId) {
        messageTv.setVisibility(View.GONE);
        LayoutInflater.from(getContext()).inflate(layoutId, customViewLayout, true);
//        customViewLayout.addView(v);
        return this;
    }

    public FrameLayout getCustomViewLayout() {
        return customViewLayout;
    }

    public CommonAlertDialog setPositiveButton(int positiveButtonText,
                                               OnClickListener listener) {
        setPositiveButton(ResUtils.getStringRes(getContext(), positiveButtonText), listener);
        return this;
    }


    public CommonAlertDialog setPositiveButton(CharSequence positiveButtonText,
                                               OnClickListener listener) {
        setPositiveButton(positiveButtonText, listener, true);
        return this;
    }

    public CommonAlertDialog setPositiveButton(CharSequence positiveButtonText,
                                               OnClickListener listener, boolean dismissDialog) {

        if (dialogButtonBar.getVisibility() != View.VISIBLE) {
            dialogButtonBar.setVisibility(View.VISIBLE);
        }
        okBtn.setText(positiveButtonText);
        okBtn.setOnClickListener(v -> {
            if (null != listener) {
                listener.onClick(this, DialogInterface.BUTTON_POSITIVE);
            }
            if (dismissDialog) {
                dismiss();
            }
        });

        return this;
    }


    public CommonAlertDialog setNegativeButton(int negativeButtonText,
                                               OnClickListener listener) {

        setNegativeButton(ResUtils.getStringRes(getContext(), negativeButtonText), listener);
        return this;
    }

    public CommonAlertDialog setNegativeButton(CharSequence negativeButtonText,
                                               OnClickListener listener) {
        setNegativeButton(negativeButtonText, listener, true);

        return this;
    }

    public CommonAlertDialog setNegativeButton(CharSequence negativeButtonText,
                                               OnClickListener listener, boolean dismissDialog) {
        if (dialogButtonBarVerticalLine.getVisibility() != View.VISIBLE) {
            dialogButtonBarVerticalLine.setVisibility(View.VISIBLE);
        }
        if (cancelBtn.getVisibility() != View.VISIBLE) {
            cancelBtn.setVisibility(View.VISIBLE);
        }
        cancelBtn.setText(negativeButtonText);
        cancelBtn.setOnClickListener(v -> {
            if (null != listener) {
                listener.onClick(this, DialogInterface.BUTTON_NEGATIVE);
            }
            if (dismissDialog) {
                dismiss();
            }
        });
        return this;
    }

    public CommonAlertDialog setShowCloseView(boolean showCloseView) {

        if (showCloseView) {
            closeView.setVisibility(View.VISIBLE);
        } else {
            closeView.setVisibility(View.GONE);
        }

        return this;
    }
}


