package com.example.jingbin.app.stickview;

import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by sj on 15/11/22.
 */
public interface ScrollHolder {

    void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);

    void onScrollViewScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition);

    //void onRecyclerViewScroll(RecyclerView view, int scrollY, int pagePosition, boolean isScrollToTop);
}
