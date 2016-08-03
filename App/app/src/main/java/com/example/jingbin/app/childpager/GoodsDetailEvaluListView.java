package com.example.jingbin.app.childpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jingbin.app.R;
import com.example.jingbin.app.stickview.PlaceHoderHeaderLayout;

import java.util.ArrayList;


/**
 * Created by jingbin on 16/4/27.
 * 商品详情评价栏
 */
public class GoodsDetailEvaluListView extends FrameLayout {
    public ListView listView;
    private PlaceHoderHeaderLayout placeHoderHeaderLayout;
    private Context mContext;
    private View mFooterView;

    public GoodsDetailEvaluListView(Context context) {
        super(context);
        mContext = context;
        initView();
//        initData();
        loadData();
    }

    private void initData() {
        listView.setAdapter(new EvaluAdapter());
    }

    public PlaceHoderHeaderLayout getPlaceHoderHeaderLayout() {
        return placeHoderHeaderLayout;
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_goods_detail_listview, this);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.placehoder_goods_detail);
        listView = (ListView) view.findViewById(R.id.lv_goods_detail_scroll);
    }

    private void loadData() {
        /**
         * viewpager ：
         *      setOffscreenPageLimit，预加载机制偶尔会失效，导致评价没有数据时会回到顶部并不能滑动
         *      如果注释 mViewPager.setOffscreenPageLimit(3);，则点击评论时会回到顶部，但可以下滑查看其数据
         *      加上此行代码，初始化listview的数据，预加载机制失效也会有adapter填充，不会出现下滑不动的情况。
         *      实际上加上以后，一切正常，也不会回到顶部了。
         * */
        listView.setAdapter(new EvaluAdapter());

    }

    class EvaluAdapter extends BaseAdapter {

        ArrayList<String> listDatas ;
        public EvaluAdapter() {
            listDatas = new ArrayList<>();
            for (int i=0;i<100;i++) {
                listDatas.add("第"+i+"条评价");
            }
        }

        @Override
        public int getCount() {
            return listDatas.size();
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_evaluation_empty, null);
            TextView tv_evalu = (TextView) view.findViewById(R.id.tv_evalu);
            tv_evalu.setText(listDatas.get(i));
            return view;
        }
    }


}
