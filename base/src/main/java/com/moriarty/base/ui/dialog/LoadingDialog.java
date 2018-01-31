package com.moriarty.base.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.moriarty.base.R;


public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.NoFrameDialog);
        setContentView(R.layout.loading);
        getWindow().getAttributes().dimAmount = 0;
        setCanceledOnTouchOutside(false);
    }


}
