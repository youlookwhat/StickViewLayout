package com.example.jingbin.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class StickActivity extends AppCompatActivity {

    private ArrayList<MainActivity.ViewPagerBean> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
        initTabData();
//        ListView viewById = findViewById(R.id.vp_scroll);
//        viewById.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_goods_evaluation_empty, viewById, false));
    }


    /**
     * 图文详情 产品参数 用户评价
     */
    private void initTabData() {
        viewList = new ArrayList<MainActivity.ViewPagerBean>();
    }

}
