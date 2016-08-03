package com.example.jingbin.app.stickview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.jingbin.app.R;


/**
 * Created by jingbin on 16/08/03.
 */
public class PlaceHoderHeaderLayout extends FrameLayout {

    private View placeHolderView;
    private View mScrollItemView;

    public View getmScrollItemView (){
        return mScrollItemView;
    }
    private int mScrollViewId;
    private boolean mIsRegisterScrollListener;

    private StickHeaderViewPagerManager mStickHeaderViewPagerManager;
    private int mPosition;

    public PlaceHoderHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StickHeaderLayout);
        if (typedArray != null) {
            mScrollViewId = typedArray.getResourceId(R.styleable.StickHeaderLayout_scrollViewId, mScrollViewId);
            typedArray.recycle();
        }
    }

    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("only can host 1 elements");
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (onAttachedToWindowListener != null) {
            onAttachedToWindowListener.onAttachedToWindow(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mScrollItemView = mScrollViewId != 0 ? findViewById(mScrollViewId) : getChildAt(0);

        if (mScrollItemView == null) {
            return;
        }

        if (mScrollItemView instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) mScrollItemView;
            View contentView = scrollView.getChildAt(0);
            scrollView.removeView(contentView);
            LinearLayout childLayout = new LinearLayout(getContext());
            childLayout.setOrientation(LinearLayout.VERTICAL);
            placeHolderView = new View(getContext());
            childLayout.addView(placeHolderView, ViewGroup.LayoutParams.MATCH_PARENT, 0);
            childLayout.addView(contentView);
            scrollView.addView(childLayout);
        } else if (mScrollItemView instanceof ListView) {
            ListView listView = (ListView) mScrollItemView;
            placeHolderView = new View(getContext());
            listView.addHeaderView(placeHolderView);
        } else if (mScrollItemView instanceof WebView) {
            removeView(mScrollItemView);
            NestingWebViewScrollView scrollView = new NestingWebViewScrollView(getContext());
            LinearLayout childLayout = new LinearLayout(getContext());
            childLayout.setOrientation(LinearLayout.VERTICAL);
            placeHolderView = new View(getContext());
            childLayout.addView(placeHolderView, ViewGroup.LayoutParams.MATCH_PARENT, 0);
            childLayout.addView(mScrollItemView);
            scrollView.addView(childLayout);
            mScrollItemView = scrollView;
            addView(scrollView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public void updatePlaceHeight(final int placeHoderHeight, final StickHeaderViewPagerManager stickHeaderViewPagerManager, final int position) {

        this.mStickHeaderViewPagerManager = stickHeaderViewPagerManager;
        this.mPosition = position;


        if (placeHolderView != null && placeHoderHeight != 0) {
            placeHolderView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = placeHolderView.getLayoutParams();
                    if (params != null) {
                        params.height = placeHoderHeight;
                        placeHolderView.setLayoutParams(params);
                    }
                }
            });

            if (!mIsRegisterScrollListener) {
                if (mScrollItemView instanceof NotifyingListenerScrollView) {
                    ((NotifyingListenerScrollView) mScrollItemView).setOnScrollChangedListener(new NotifyingListenerScrollView.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                            stickHeaderViewPagerManager.onScrollViewScroll(who, l, t, oldl, oldt, position);
                        }
                    });
                } else if (mScrollItemView instanceof ListView) {
                    ((ListView) mScrollItemView).setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) { }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            stickHeaderViewPagerManager.onListViewScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, position);
                        }
                    });
                } else if (mScrollItemView instanceof NestingWebViewScrollView) {
                    ((NestingWebViewScrollView) mScrollItemView).setOnScrollChangedListener(new NotifyingListenerScrollView.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                            stickHeaderViewPagerManager.onScrollViewScroll(who, l, t, oldl, oldt, position);
                        }
                    });
                }
                mIsRegisterScrollListener = true;
            }
        }
    }


    int mScrollHeight;
    int mHeaderHeight;

    public void adjustScroll(int scrollHeight, int headerHeight, boolean isPageSelected) {
        this.mScrollHeight = scrollHeight;
        this.mHeaderHeight = headerHeight;
        if (mScrollItemView == null) return;

        if (mScrollItemView instanceof ListView) {
            if (scrollHeight == 0 && ((ListView) mScrollItemView).getFirstVisiblePosition() >= 1) {
                return;
            }
            ((ListView) mScrollItemView).setSelectionFromTop(1, scrollHeight);
        } else if (mScrollItemView instanceof ScrollView) {
            ((ScrollView) mScrollItemView).scrollTo(0, headerHeight - scrollHeight);
        }
    }

    OnAttachedToWindowListener onAttachedToWindowListener;

    public void setOnAttachedToWindowListener(OnAttachedToWindowListener onAttachedToWindowListener) {
        this.onAttachedToWindowListener = onAttachedToWindowListener;
    }

    public interface OnAttachedToWindowListener {
        void onAttachedToWindow(PlaceHoderHeaderLayout placeHoderHeaderLayout);
    }
}
