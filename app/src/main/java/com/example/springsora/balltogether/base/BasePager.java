package com.example.springsora.balltogether.base;

/**
 * Created by JJBOOM on 2016/4/20.
 */
import android.content.Context;
import android.view.View;


public abstract class BasePager{
    public Context context;
    public View view;

    public BasePager(Context context) {
        this.context = context;
        view = initView();
    }
    public abstract View initView();
    public abstract void initData();
    public View getRootView(){
        return view;
    }
}

