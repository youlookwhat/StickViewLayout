package com.example.jingbin.app.childpager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.jingbin.app.R;
import com.example.jingbin.app.stickview.PlaceHoderHeaderLayout;

/**
 * Created by jingbin on 16/4/27.
 * 包含商品详情页里：
 * 图文详情、产品参数
 */
public class GoodsDetailWebListView extends FrameLayout {

    private PlaceHoderHeaderLayout placeHoderHeaderLayout;
    public ListView listView;
    private Context mContext;
    private WebView webview_list;
    // web显示部分（借用ListView）
    private View headerView;
    /**
     * 是哪个页面:
     * 图文详情：description
     * 产品参数：specs
     */
    private String whichOne;

    public GoodsDetailWebListView(Context context) {
        super(context);
    }

    public GoodsDetailWebListView(Context context, String whichOne) {
        super(context);
        mContext = context;
        this.whichOne = whichOne;
        initView();
        loadData();
    }

    public PlaceHoderHeaderLayout getPlaceHoderHeaderLayout() {
        return placeHoderHeaderLayout;
    }

    public void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_goods_detail_listview, this);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.placehoder_goods_detail);
        listView = (ListView) view.findViewById(R.id.lv_goods_detail_scroll);
    }

    private void loadData() {
        /** 网页显示部分*/
        if (headerView == null) {
            headerView = LayoutInflater.from(getContext()).inflate(R.layout.item_goods_detail_web, null);
            webview_list = (WebView) headerView.findViewById(R.id.webview_list);
            initWebView();

            String url;
            if ("description".equals(whichOne)) {
                url = "https://github.com/youlookwhat";
            } else {
                url = "https://github.com/youlookwhat/StickViewLayout";
            }
            webview_list.loadUrl(url);
            Log.e("------url:", url);

        }
        listView.addHeaderView(headerView);
        listView.setAdapter(new EmptyAdapter());
    }

    class EmptyAdapter extends BaseAdapter {

        public EmptyAdapter() {
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_detail_empty, null);
            return view;
        }
    }


    private void initWebView() {
        WebSettings ws = webview_list.getSettings();
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        //   ws.setUseWideViewPort(true); /**合作商页面适应屏幕*/
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
//        ws.setBuiltInZoomControls(false);// 隐藏缩放按钮
        //----------缩放---------
//        webview_list.setOnTouchListener(this);//监听触摸事件
        ws.setSupportZoom(true);
        // 双击缩放
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
//        webview_list.setFocusable(true);
//        webview_list.requestFocus();
//        webview_list.setClickable(true);
//        webview_list.setLongClickable(true);
        //设置加载进来的页面自适应手机屏幕
//        ws.setLoadWithOverviewMode(true);

        //设置缓存模式
        ws.setAppCacheEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        //缩放比例 1
        webview_list.setInitialScale(1);
//        ws.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        //---------------------
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        // 缩放排版:
//        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 适应屏幕
//        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        ws.setSupportMultipleWindows(true);// 新加
//        ws.setUseWideViewPort(true); /**合作商页面适应屏幕*/
        webview_list.setWebViewClient(new MyWebViewClient());
    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://v.youku.com/")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addCategory("android.intent.category.BROWSABLE");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                mContext.startActivity(intent);
                return true;
            } else {
                view.loadUrl(url);
            }
            return false;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            // html加载完成之后，添加监听图片的点击js函数
            //  stopProgressDialog();
            // mProgressBar.setVisibility(View.GONE);
//            addImageClickListener();
//            view.loadUrl("javascript:window.android.test();");
            super.onPageFinished(view, url);
        }

        //        webView默认是不处理https请求的，页面显示空白，需要进行如下设置：
//         onReceivedSslError为webView处理ssl证书设置
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }
    }


}
