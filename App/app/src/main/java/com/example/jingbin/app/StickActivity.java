package com.example.jingbin.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.jingbin.app.stick.ScrollableLayout;

import java.util.ArrayList;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;

/**
 * @author jingbin
 * https://github.com/w446108264/ScrollableLayout 滑动时有卡顿现象
 */
public class StickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        arrayList.add("22222");
        ByRecyclerView viewById = findViewById(R.id.vp_scroll);
        ScrollableLayout scrollableLayout = findViewById(R.id.sl_root);
        viewById.setAdapter(new InAdapter(R.layout.item_goods_evaluation_empty, arrayList));

        scrollableLayout.getHelper().setCurrentScrollableContainer(viewById);

    }

    private class InAdapter extends BaseRecyclerAdapter<String> {
        public InAdapter(int layoutId, ArrayList<String> arrayList) {
            super(layoutId, arrayList);
        }

        @Override
        protected void bindView(BaseByViewHolder<String> holder, String bean, int position) {
            holder.setText(R.id.tv_evalu, bean);
        }
    }
}
