package com.example.springsora.balltogether.custom_listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by JJBOOM on 2016/4/21.
 */
public class FirstListView extends ListView {
    private static final String Tag = "FirstListView";
    public FirstListView(Context context) {
        super(context);
    }

    public FirstListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FirstListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
