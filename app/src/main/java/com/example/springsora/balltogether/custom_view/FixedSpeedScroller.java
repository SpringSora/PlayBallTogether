package com.example.springsora.balltogether.custom_view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by JJBOOM on 2016/4/29.
 */
public class FixedSpeedScroller extends Scroller {
    private int mDuration = 1000;
    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,mDuration);
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }
}
