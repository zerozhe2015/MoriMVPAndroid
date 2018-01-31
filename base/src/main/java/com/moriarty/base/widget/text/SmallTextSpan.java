package com.moriarty.base.widget.text; /**
 * This is a helper class to help adjust the alignment of a section of text, when using SpannableStrings to set text
 * formatting dynamically.
 */

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class SmallTextSpan extends MetricAffectingSpan {

    float ratio = 0.7f;

    public SmallTextSpan() {
    }

    public SmallTextSpan(float ratio) {
        this.ratio = ratio;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        paint.setTextSize(paint.getTextSize() * ratio);
//        paint.baselineShift += (int) (paint.ascent() * ratio);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
//        paint.baselineShift += (int) (paint.ascent() * ratio);
        paint.setTextSize(paint.getTextSize() * ratio);
    }
}