package com.example.springsora.balltogether.pager.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.base.BasePager;


/**
 * Created by JJBOOM on 2016/4/21.
 */
public class ThirdPager extends BasePager {
    private View view;
    @Override
    public View initView() {
        view = LayoutInflater.from(context).inflate(R.layout.pager_third,null);
        return view;
    }

    @Override
    public void initData() {

    }

    public ThirdPager(Context context) {
        super(context);
    }
}
