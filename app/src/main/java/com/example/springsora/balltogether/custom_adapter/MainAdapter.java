package com.example.springsora.balltogether.custom_adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import com.example.springsora.balltogether.base.BasePager;

import java.util.List;

/**
 * Created by JJBOOM on 2016/4/21.
 */
public class MainAdapter extends PagerAdapter {

    private List<BasePager> pagers;


    public MainAdapter(List<BasePager> pagers) {
        super();
        this.pagers = pagers;
    }

    @Override
    public int getCount() {
        return pagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(pagers.get(position).getRootView());
        return pagers.get(position).getRootView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pagers.get(position).getRootView());
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.i("MainAdapter","notifyDataSetChanged");
    }
}
