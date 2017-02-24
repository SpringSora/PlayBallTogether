package com.example.springsora.balltogether.custom_viewpager;

/**
 * Created by JJBOOM on 2016/4/20.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
//lazyviewpager是修改了原来viewpager的运行方式，把提前加载的方式改为不提前加载
public class MyViewPager extends LazyViewPager{

    public MyViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return false;
    }

}

