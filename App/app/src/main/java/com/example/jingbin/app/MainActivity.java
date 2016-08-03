package com.example.jingbin.app;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jingbin.app.childpager.GoodsDetailEvaluListView;
import com.example.jingbin.app.childpager.GoodsDetailWebListView;
import com.example.jingbin.app.stickview.PlaceHoderHeaderLayout;
import com.example.jingbin.app.stickview.StickHeaderLayout;
import com.example.jingbin.app.stickview.StickHeaderViewPagerManager;
import com.example.jingbin.app.stickview.ptr.PtrClassicFrameLayout;
import com.example.jingbin.app.stickview.ptr.PtrDefaultHandler;
import com.example.jingbin.app.stickview.ptr.PtrFrameLayout;
import com.example.jingbin.app.stickview.ptr.PtrHandler;
import com.example.jingbin.app.stickview.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout llDetailTab;
    private NoScrollViewPager mViewPager;
    private ArrayList<ViewPagerBean> viewList;
    private StickHeaderViewPagerManager manager;
    private GoodsDetailWebListView photoDetailWebView;
    private GoodsDetailWebListView productParameterWebView;
    private GoodsDetailEvaluListView evaluateListView;
    private TextView tv_refresh_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置成竖屏禁止转屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);
        setContentView(R.layout.activity_main);

        initView();
        initTabData();
    }

    private void initView() {
        mViewPager = (NoScrollViewPager) findViewById(R.id.viewPager_scroll);
        tv_refresh_data = (TextView) findViewById(R.id.tv_refresh_data);
    }


    /**
     * 图文详情 产品参数 用户评价
     */
    private void initTabData() {
        viewList = new ArrayList<ViewPagerBean>();
        //主控件
        StickHeaderLayout shl_root = (StickHeaderLayout) findViewById(R.id.shl_root);
        //去掉顶部布局的滚动条
        shl_root.getHeaderScrollView().setVerticalScrollBarEnabled(false);
        //viewPager
        // 设置缓存页面，当前页面的相邻N各页面都会被缓存
        mViewPager.setOffscreenPageLimit(3);
        // 可滑动
//        mViewPager.setHeaderView(shl_root.getStickHeaderView());
        // 关键类
        manager = new StickHeaderViewPagerManager(shl_root, mViewPager);

        //图文详情
        photoDetailWebView = new GoodsDetailWebListView(this, "description");
        viewList.add(new ViewPagerBean(photoDetailWebView, photoDetailWebView.getPlaceHoderHeaderLayout()));
        //产品参数
        productParameterWebView = new GoodsDetailWebListView(this, "specs");
        viewList.add(new ViewPagerBean(productParameterWebView, productParameterWebView.getPlaceHoderHeaderLayout()));
        //用户评价
        evaluateListView = new GoodsDetailEvaluListView(this);
        viewList.add(new ViewPagerBean(evaluateListView, evaluateListView.getPlaceHoderHeaderLayout()));

        mViewPager.setAdapter(pagerAdapter);
        initTabBar();
        initStickHeaderInfo(shl_root);
    }

    class ViewPagerBean {
        // root是FrameLayout
        View root;
        PlaceHoderHeaderLayout placeHoderHeaderLayout;

        public ViewPagerBean(View root, PlaceHoderHeaderLayout placeHoderHeaderLayout) {
            this.root = root;
            this.placeHoderHeaderLayout = placeHoderHeaderLayout;
        }
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // root: new 的 FrameLayout();
            container.addView(viewList.get(position).root);
            // placeHoderHeaderLayout里面fragment的填充布局
            manager.addPlaceHoderHeaderLayout(position, viewList.get(position).placeHoderHeaderLayout);
            return viewList.get(position).root;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);//(FrameLayout)
        }
    };


    private void initTabBar() {
        final List<LinearLayout> llList = new ArrayList<>();

        llDetailTab = (LinearLayout) findViewById(R.id.ll_detail_tab);
        for (int i = 0; i < llDetailTab.getChildCount(); i++) {
            LinearLayout tab = (LinearLayout) llDetailTab.getChildAt(i);
            final int finalI = i;
            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mViewPager != null && mViewPager.getAdapter().getCount() > finalI) {
                        if (mViewPager.getCurrentItem() == finalI) {
                            return;
                        }
                        mViewPager.setCurrentItem(finalI);
                    }
                }
            });
            llList.add(tab);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (LinearLayout linearLayout : llList) {
                    //文字
                    TextView textView = (TextView) linearLayout.getChildAt(0);
                    textView.setTextColor(Color.parseColor("#333333"));
                    //线条
                    View view = (View) linearLayout.getChildAt(1);
                    view.setBackgroundColor(Color.parseColor("#ffffffff"));
                }
                //文字
                TextView textView = (TextView) llList.get(position).getChildAt(0);
                textView.setTextColor(Color.parseColor("#f23030"));
                //线条
                View view = (View) llList.get(position).getChildAt(1);
                view.setBackgroundColor(Color.parseColor("#f23030"));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 初始化刷新控件
     */
    private void initStickHeaderInfo(StickHeaderLayout shl_root) {
        final PtrClassicFrameLayout rotate_header_list_view_frame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
        rotate_header_list_view_frame.setEnabledNextPtrAtOnce(true);
        rotate_header_list_view_frame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /** 刷新处理*/
//                        loadData();
                        tv_refresh_data.setText("刷新了数据");

                        rotate_header_list_view_frame.refreshComplete();
                    }
                }, 100);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (manager.isCanPullToRefresh()) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }
                return false;
            }
        });

        /** 显示top的情况*/
        shl_root.addOnPlaceHoderListener(new StickHeaderLayout.OnPlaceHoderListener() {
            @Override
            public void onSizeChanged(int headerHeight, int stickHeight) {
//                iv_detail_top.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollChanged(int stickHeight) {
            }

            @Override
            public void onHeaderTranslationY(float translationY) {
                /** 显示top的情况*/
                int width, height;
                Point p = new Point();
                getWindowManager().getDefaultDisplay().getSize(p);
                width = p.x;
                height = p.y;
                Rect rect = new Rect(0, 0, width, height);
//                if (llDetailTab.getLocalVisibleRect(rect)) {
//                    iv_detail_top.setVisibility(View.VISIBLE);
//                } else {
//                    iv_detail_top.setVisibility(View.INVISIBLE);
//                }
            }
        });
    }

}
