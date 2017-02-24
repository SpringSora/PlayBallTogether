package com.example.springsora.balltogether.custom_layout;

import android.content.Context;
import android.nfc.Tag;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.springsora.balltogether.R;


/**
 * Created by JJBOOM on 2016/4/21.
 */
public class FirstRefreshLoadLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener{

    /**
     * ������������ʱ����������
     */

    private int mTouchSlop;


    /**
     * listviewʵ��
     */
    private ListView mListView;

    /**
     * ����������, ������ײ����������ز���
     */
    private OnLoadListener mOnLoadListener;

    /**
     * ListView�ļ�����footer
     */
    private View mListViewFooter;


    /**
     * �Ƿ��ڼ����� ( �������ظ��� )
     */
    private boolean isLoading = false;

    private Context context;




    /**
     * ����ʱ��y����
     */
    private int mYDown;
    /**
     * ̧��ʱ��y����, ��mYDownһ�����ڻ������ײ�ʱ�ж���������������
     */
    private int mLastY;
    /**
     * ����ʱx����
     */
    private int mXDown;

    private int mLastX;

    private static final String Tag = "FirstRefreshLoadLayout";



    public FirstRefreshLoadLayout(Context context) {
        this(context, null);
        this.context = context;
    }

    public FirstRefreshLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.first_footer, null,
                false);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(mListView == null){
            getListView();
        }
    }


    /**
     * ��ȡListView����
     */
    private void getListView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                // ���ù�����������ListView, ʹ�ù����������Ҳ�����Զ�����
                mListView.setOnScrollListener(this);
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) ev.getRawY();
                mXDown = (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                // �ƶ�
                mLastY = (int) ev.getRawY();
                mLastX = (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_UP:
                if(canLoad()){
                    loadData();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // ����
                mYDown = (int) event.getRawY();
                mXDown = (int) event.getRawX();
                break;

            case MotionEvent.ACTION_MOVE:
                // �ƶ�
                mLastY = (int) event.getRawY();
                mLastX = (int) event.getRawX();
                if(Math.abs(mLastY - mYDown) < Math.abs(mLastX - mXDown) ){
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                // ̧��
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }


    /**
     * �Ƿ���Լ��ظ���, �����ǵ�����ײ�, listview���ڼ�����, ��Ϊ��������.
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }


    /**
     * �ж��Ƿ�����ײ�
     */
    private boolean isBottom() {

        if (mListView != null && mListView.getAdapter() != null) {
            return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
        }
        return false;
    }


    /**
     * �Ƿ�����������
     *
     * @return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    /**
     * ���������ײ�,��������������.��ôִ��onLoad����
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            // ����״̬
            setLoading(true);
            //
            mOnLoadListener.onLoad();
        }
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            mListView.addFooterView(mListViewFooter);
        } else {
            mListView.removeFooterView(mListViewFooter);
            mYDown = 0;
            mLastY = 0;
        }
    }


    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }


    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // ����ʱ������ײ�Ҳ���Լ��ظ���
        if (canLoad()) {
            loadData();
        }
    }
    /**
     * ���ظ���ļ�����
     *
     * @author mrsimple
     */
    public static interface OnLoadListener {
        public void onLoad();
    }

    public boolean isLoading() {
        return isLoading;
    }

}
