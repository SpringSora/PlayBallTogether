package com.example.springsora.balltogether.custom_adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by JJBOOM on 2016/4/29.
 */
public class AdPagerAdapter extends PagerAdapter {

    private List<ImageView> imageViewList;

    public AdPagerAdapter(List<ImageView> imageViewList) {
        this.imageViewList = imageViewList;
    }

    @Override
    public int getCount() {
        return imageViewList.size()*1000;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(container.getChildCount()==4){
            container.removeView(imageViewList.get(position%4));
        }
        container.addView(imageViewList.get(position%4));
        return imageViewList.get(position%4);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position%4));
    }
}
