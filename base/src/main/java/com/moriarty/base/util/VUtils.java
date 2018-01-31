package com.moriarty.base.util;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Callable;


/**
 * 跟View相关的通用方法帮助类
 */

public class VUtils {


    /**
     * 判断AbsListView能否向下滑动
     *
     * @param absListView
     * @return
     */
    public static boolean canScrollDown(AbsListView absListView) {
        boolean canScrollDown;
        int count = absListView.getChildCount();
        canScrollDown = (absListView.getFirstVisiblePosition() + count) < absListView.getCount();

        if (!canScrollDown && count > 0) {
            View child = absListView.getChildAt(count - 1);
            canScrollDown = child.getBottom() > absListView.getBottom() - absListView.getListPaddingBottom();
        }
        return canScrollDown;
    }


    /**
     * 判断RecyclerView能否向下滑动
     *
     * @param recyclerView
     * @return
     */
    public static boolean canScrollDown(RecyclerView recyclerView) {
        boolean canScrollDown;
        int count = recyclerView.getChildCount();
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        double firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        canScrollDown = (firstVisiblePosition + count) < layoutManager.getItemCount();

        if (!canScrollDown && count > 0) {
            View child = recyclerView.getChildAt(count - 1);
            canScrollDown = child.getBottom() > recyclerView.getBottom() - recyclerView.getPaddingBottom();
        }
        return canScrollDown;
    }


    public static void setPassword(EditText et, boolean isPassword) {
        if (isPassword) {
            et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            et.setTransformationMethod(SingleLineTransformationMethod.getInstance());
        }
        Selection.setSelection(et.getText(), et.getText().length());
    }


    public static void updateBtnStatusByInput(TextView[] inputViews, TextView btn) {


        btn.setEnabled(defaultConditionForBtnEnable(inputViews));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                btn.setEnabled(defaultConditionForBtnEnable(inputViews));
            }
        };

        for (TextView inputView : inputViews) {
            if (inputView instanceof CheckBox) {
                ((CheckBox) inputView).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        btn.setEnabled(defaultConditionForBtnEnable(inputViews));
                    }
                });
            } else {
                inputView.addTextChangedListener(textWatcher);
            }
        }
    }


    /**
     * 默认按钮激活条件
     */

    private static boolean defaultConditionForBtnEnable(TextView[] inputViews) {
//        boolean enable = true;
        for (TextView inputView : inputViews) {
            //TextView
            boolean condition = inputView.getText().length() != 0;
            //CheckBox
            if (inputView instanceof CheckBox) {
                condition = ((CheckBox) inputView).isChecked();
            }

//            enable = enable && condition;
            if (!condition) {
                return false;
            }

        }
//        return enable;
        return true;
    }


    public static void updateBtnStatusByInput(TextView[] inputViews, TextView btn, Callable<Boolean> checkCondition) {

        try {
            btn.setEnabled(checkCondition.call());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    btn.setEnabled(checkCondition.call());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        for (TextView inputView : inputViews) {
            if (inputView instanceof CheckBox) {
                ((CheckBox) inputView).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            btn.setEnabled(checkCondition.call());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                inputView.addTextChangedListener(textWatcher);
            }
        }
    }


    public static GradientDrawable getRoundRectDrawable(int solidColor, float cornerRadius, int lineWidth, int lineColor) {
        GradientDrawable roundRectDrawable = new GradientDrawable();
        roundRectDrawable.setShape(GradientDrawable.RECTANGLE);
        roundRectDrawable.setColor(solidColor);
        roundRectDrawable.setStroke(lineWidth, lineColor);
        roundRectDrawable.setCornerRadius(cornerRadius);
        return roundRectDrawable;
    }

    public static GradientDrawable getRoundRectSolidDrawable(int solidColor, float cornerRadius) {
        GradientDrawable roundRectDrawable = new GradientDrawable();
        roundRectDrawable.setShape(GradientDrawable.RECTANGLE);
        roundRectDrawable.setColor(solidColor);
        roundRectDrawable.setCornerRadius(cornerRadius);
        return roundRectDrawable;
    }


    public static GradientDrawable getRoundRectStrokeDrawable(float cornerRadius, int lineWidth, int lineColor) {
        GradientDrawable roundRectDrawable = new GradientDrawable();
        roundRectDrawable.setShape(GradientDrawable.RECTANGLE);
        roundRectDrawable.setCornerRadius(cornerRadius);
        roundRectDrawable.setStroke(lineWidth, lineColor);
        return roundRectDrawable;
    }


    public static void setRoundRectBackground(View v, int solidColor, float cornerRadius, int lineWidth, int lineColor) {
        if (v != null) {
            v.setBackgroundDrawable(getRoundRectDrawable(solidColor, cornerRadius, lineWidth, lineColor));
        }
    }


    public static void setRoundRectSolidBackground(View v, int solidColor, float cornerRadius) {
        if (v != null) {
            v.setBackgroundDrawable(getRoundRectSolidDrawable(solidColor, cornerRadius));
        }
    }

    public static void setRoundRectStrokeBackground(View v, float cornerRadius, int lineWidth, int lineColor) {
        if (v != null) {
            v.setBackgroundDrawable(getRoundRectStrokeDrawable(cornerRadius, lineWidth, lineColor));
        }
    }

}
