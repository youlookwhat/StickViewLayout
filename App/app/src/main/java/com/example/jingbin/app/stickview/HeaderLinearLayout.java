package com.example.jingbin.app.stickview;

import android.content.Context;
import android.widget.LinearLayout;

public class HeaderLinearLayout extends LinearLayout {

    public HeaderLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mOnSizeChangedListener != null){
            mOnSizeChangedListener.onHeaderSizeChanged(w,h,oldw,oldh);
        }
    }

    private OnSizeChangedListener mOnSizeChangedListener;

    public interface OnSizeChangedListener {
        void onHeaderSizeChanged(int w, int h, int oldw, int oldh);
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        mOnSizeChangedListener = listener;
    }
}
