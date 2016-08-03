package com.example.jingbin.app.stickview.ptr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jingbin.app.R;

import java.util.Date;

public class PtrClassicDefaultHeader extends FrameLayout implements PtrUIHandler {

    private final static String KEY_SharedPreferences = "cube_ptr_classic_last_update";
    private int mRotateAniTime = 150;
    private TextView mTitleTextView;
    private ImageView mRotateView;
    private View mProgressBar;
    private AnimationDrawable animationDrawable;
    private String mLastUpdateTimeKey;


    public PtrClassicDefaultHeader(Context context) {
        super(context);
        initViews(null);
    }

    public PtrClassicDefaultHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public PtrClassicDefaultHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.PtrClassicHeader, 0, 0);
        if (arr != null) {
            mRotateAniTime = arr.getInt(R.styleable.PtrClassicHeader_ptr_rotate_ani_time, mRotateAniTime);
        }
        View header = LayoutInflater.from(getContext()).inflate(R.layout.cube_ptr_classic_default_header, this);

        mRotateView = (ImageView)header.findViewById(R.id.ptr_classic_header_rotate_view);
        mRotateView.setImageResource(R.drawable.kaws_anim);
        //小鹿的动画
        animationDrawable = (AnimationDrawable) mRotateView.getDrawable();

        mTitleTextView = (TextView) header.findViewById(R.id.ptr_classic_header_rotate_view_header_title);
        mProgressBar = header.findViewById(R.id.ptr_classic_header_rotate_view_progressbar);

        resetView();
    }


    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mLastUpdateTimeKey = key;
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        setLastUpdateTimeKey(object.getClass().getName());
    }


    private void resetView() {
        hideRotateView();
        mProgressBar.setVisibility(INVISIBLE);
    }

    private void hideRotateView() {
        mRotateView.clearAnimation();
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        mRotateView.setVisibility(INVISIBLE);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        resetView();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

        mProgressBar.setVisibility(INVISIBLE);

        mRotateView.setVisibility(VISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        if (frame.isPullToRefresh()) {
            mTitleTextView.setText(getResources().getString(R.string.cube_ptr_pull_down_to_refresh));
        } else {
            mTitleTextView.setText(getResources().getString(R.string.cube_ptr_pull_down));
        }
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
//        hideRotateView();
        mProgressBar.setVisibility(INVISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(R.string.cube_ptr_refreshing);
        animationDrawable.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

//        hideRotateView();
        mProgressBar.setVisibility(INVISIBLE);

        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(getResources().getString(R.string.cube_ptr_refresh_complete));

        // update last update time
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(KEY_SharedPreferences, 0);
        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
            long mLastUpdateTime = new Date().getTime();
            sharedPreferences.edit().putLong(mLastUpdateTimeKey, mLastUpdateTime).commit();
        }
    }



    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
                if (mRotateView != null) {
                    if (animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                    mRotateView.clearAnimation();
                }
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
                if (mRotateView != null) {
                    if (animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                    mRotateView.clearAnimation();
                }
            }
        }
    }

    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            mTitleTextView.setVisibility(VISIBLE);
            animationDrawable.start();
            mRotateView.setVisibility(VISIBLE);
            mTitleTextView.setText(R.string.cube_ptr_release_to_refresh);
        }
    }

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        mTitleTextView.setVisibility(VISIBLE);
        if (frame.isPullToRefresh()) {
            mTitleTextView.setText(getResources().getString(R.string.cube_ptr_pull_down_to_refresh));
        } else {
            mTitleTextView.setText(getResources().getString(R.string.cube_ptr_pull_down));
        }
    }

}
