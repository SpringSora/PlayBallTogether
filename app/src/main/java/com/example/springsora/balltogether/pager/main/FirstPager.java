package com.example.springsora.balltogether.pager.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BasePager;
import com.example.springsora.balltogether.bean.Advertisement;
import com.example.springsora.balltogether.bean.DateBall;
import com.example.springsora.balltogether.custom_adapter.AdPagerAdapter;
import com.example.springsora.balltogether.custom_adapter.FirstAdapter;
import com.example.springsora.balltogether.custom_layout.FirstRefreshLoadLayout;
import com.example.springsora.balltogether.custom_listview.FirstListView;
import com.example.springsora.balltogether.custom_view.FixedSpeedScroller;
import com.example.springsora.balltogether.custom_viewpager.AdViewPager;
import com.example.springsora.balltogether.playballtogether.ScreenBallActivity;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.example.springsora.balltogether.utils.HttpUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by JJBOOM on 2016/4/21.
 */
public class FirstPager extends BasePager {

    public FirstPager(Context context) {
        super(context);
    }

    /**
     * 加载的页面
     */
    private View view;

    /**
     * 自定义ListView
     */
    private FirstListView firstListView;
    /**
     * 下拉刷新上拉加载对像
     */
    private FirstRefreshLoadLayout firstRefreshLoadLayout;
    /**
     * 广告
     */
    private AdViewPager first_pager_ad;


    private RelativeLayout dateball_football;
    private RelativeLayout dateball_basketball;
    private RelativeLayout dateball_pingpong;
    private RelativeLayout dateball_billiards;
    private RelativeLayout dateball_badminton;
    private RelativeLayout dateball_tennis;
    private RelativeLayout dateball_volleyball;

    private List<Advertisement> advertisements;

    private List<ImageView> imageViews;

    private Bitmap bitmap;

    private List<String> urls;

    private Handler AdHandler;

    private Handler AutoFlowing;

    private FixedSpeedScroller mScroller;

    private int currentItem = 400;

    private List<ImageView> pointImageViews;
    /**
     * 定时任务
     */
    private ScheduledExecutorService scheduledExecutorService;

    private LinearLayout point_layout;

    private FirstAdapter firstAdapter;
    private int page = 1;


    private List<DateBall> dateBalls;

    @Override
    public View initView()
    {
        initControl();
        FirstHeader();
        startPlay();
        initData();
        return view;
    }
    @Override
    public void initData() {
        buttonListener();
        requestAd();
        requestDateBall();
    }

    @Override
    public View getRootView() {
        return super.getRootView();
    }

    private void initControl(){
        view = LayoutInflater.from(context).inflate(R.layout.pager_first,null);
        advertisements = new ArrayList<>();
        imageViews = new ArrayList<>();
        urls = new ArrayList<>();
        pointImageViews = new ArrayList<>();
        dateBalls = new ArrayList<>();

        firstRefreshLoadLayout = (FirstRefreshLoadLayout) view.findViewById(R.id.first_refresh);
        firstListView = (FirstListView) view.findViewById(R.id.first_list_view);

        AdHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(bitmap!=null){
                    imageViews.get(msg.what).setTag(urls.get(msg.what));
                    imageViews.get(msg.what).setImageBitmap(bitmap);
                    first_pager_ad.getAdapter().notifyDataSetChanged();
                }
            }
        };
        AutoFlowing = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                first_pager_ad.setCurrentItem(currentItem);
            }
        };
        firstAdapter = new FirstAdapter(dateBalls,context,firstListView);
        firstListView.setAdapter(firstAdapter);

    }

    private void FirstHeader(){
        View FirstView = LayoutInflater.from(context).inflate(R.layout.pager_first_ad,null);
        first_pager_ad = (AdViewPager) FirstView.findViewById(R.id.first_pager_ad);
        dateball_football = (RelativeLayout) FirstView.findViewById(R.id.dateball_football);
        dateball_basketball = (RelativeLayout) FirstView.findViewById(R.id.dateball_basketball);
        dateball_pingpong = (RelativeLayout) FirstView.findViewById(R.id.dateball_pingpong);
        dateball_billiards = (RelativeLayout) FirstView.findViewById(R.id.dateball_billiards);
        dateball_badminton = (RelativeLayout) FirstView.findViewById(R.id.dateball_badminton);
        dateball_tennis = (RelativeLayout) FirstView.findViewById(R.id.dateball_tennis);
        dateball_volleyball = (RelativeLayout) FirstView.findViewById(R.id.dateball_volleyball);
        point_layout = (LinearLayout) FirstView.findViewById(R.id.point_layout);

        for(int i = 0;i < 4;i++){
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.mipmap.ic_launcher);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(imageView);
            ImageView dotView =  new ImageView(context);
            dotView.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.width = 50;
            params.height = 50;
            params.leftMargin = 2;
            params.rightMargin = 2;
            point_layout.addView(dotView, params);
            pointImageViews.add(dotView);
        }
        first_pager_ad.setAdapter(new AdPagerAdapter(imageViews));
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(first_pager_ad.getContext(),new AccelerateDecelerateInterpolator());
            mField.set(first_pager_ad,mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        first_pager_ad.setCurrentItem(400);
        mScroller.setmDuration(500);
        firstListView.addHeaderView(FirstView);
    }

    private void buttonListener(){
        dateball_football.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(context, ScreenBallActivity.class);
                        intent.putExtra("Type",1);
                        context.startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        });
        dateball_basketball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(context, ScreenBallActivity.class);
                        intent.putExtra("Type",2);
                        context.startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }

                return true;
            }
        });

        dateball_pingpong.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(context, ScreenBallActivity.class);
                        intent.putExtra("Type",3);
                        context.startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }

                return true;
            }
        });

        dateball_billiards.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(context, ScreenBallActivity.class);
                        intent.putExtra("Type",4);
                        context.startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }

                return true;
            }
        });
        dateball_badminton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(context, ScreenBallActivity.class);
                        intent.putExtra("Type",5);
                        context.startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }

                return true;
            }
        });
        dateball_tennis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(context, ScreenBallActivity.class);
                        intent.putExtra("Type",6);
                        context.startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }

                return true;
            }
        });
        dateball_volleyball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(context, ScreenBallActivity.class);
                        intent.putExtra("Type",7);
                        context.startActivity(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        });
        imageViews.get(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopPlay();
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(context, (String) imageViews.get(0).getTag(), Toast.LENGTH_SHORT).show();
                        startPlay();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        startPlay();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        stopPlay();
                        break;
                }
                return true;
            }
        });
        imageViews.get(1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopPlay();
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(context, (String) imageViews.get(1).getTag(), Toast.LENGTH_SHORT).show();
                        startPlay();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        startPlay();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        stopPlay();
                        break;
                }
                return true;
            }
        });
        imageViews.get(2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopPlay();
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(context, (String) imageViews.get(2).getTag(), Toast.LENGTH_SHORT).show();
                        startPlay();

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        startPlay();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        stopPlay();
                        break;
                }
                return true;
            }
        });
        imageViews.get(3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopPlay();
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(context, (String) imageViews.get(3).getTag(), Toast.LENGTH_SHORT).show();
                        startPlay();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        startPlay();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        stopPlay();
                        break;
                }
                return true;
            }
        });

        first_pager_ad.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                for (int i = 0; i < pointImageViews.size(); i++) {
                    if (i == currentItem % 4) {
                        pointImageViews.get(i).setImageResource(R.mipmap.point_deep);
                    } else {
                        pointImageViews.get(i).setImageResource(R.mipmap.point);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    //手势滑动
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;
                    //界面切换
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                    //滑动结束
                    case ViewPager.SCROLL_STATE_IDLE:
                        break;
                }
            }
        });

        firstRefreshLoadLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                dateBalls.clear();
                firstAdapter.notifyDataSetChanged();
                requestDateBall();
            }
        });

        firstRefreshLoadLayout.setOnLoadListener(new FirstRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                requestDateBall();
            }
        });

        firstListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void requestAd(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl+"/GetAdServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(null!=s&&!"".equals(s)){
                    Type type = new TypeToken<List<Advertisement>>(){}.getType();
                    advertisements = GsonUtil.jsonToBean(s,type);
                    new Thread(){
                        @Override
                        public void run() {
                            for(int i = 0;i < advertisements.size();i++){
                                if((bitmap = HttpUtils.getNetWorkBitmap(BallApplication.ServerUrl+advertisements.get(i).getPic_path()))!=null){
                                    urls.add(advertisements.get(i).getUrl());
                                    AdHandler.sendEmptyMessage(i);
                                }
                            }
                        }
                    }.start();
                }else{
                    Toast.makeText(context,"获取广告失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("flag","0");
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }

    private void requestDateBall(){
        final StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl+"/GetDateBallServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(!"false".equals(s)){
                    Type type = new TypeToken<List<DateBall>>(){}.getType();
                    List<DateBall> temps = GsonUtil.jsonToBeanDateTime(s, type);
                    dateBalls.addAll(temps);
                    firstAdapter.notifyDataSetChanged();
                    if(firstRefreshLoadLayout.isRefreshing()){
                        firstRefreshLoadLayout.setRefreshing(false);
                    }
                    if(firstRefreshLoadLayout.isLoading()){
                        firstRefreshLoadLayout.setLoading(false);
                    }
                }else{
                    if(firstRefreshLoadLayout.isRefreshing()){
                        firstRefreshLoadLayout.setRefreshing(false);
                    }
                    if(firstRefreshLoadLayout.isLoading()){
                        firstRefreshLoadLayout.setLoading(false);
                    }
                    Toast.makeText(context,"获取数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
                if(firstRefreshLoadLayout.isRefreshing()){
                    firstRefreshLoadLayout.setRefreshing(false);
                }
                if(firstRefreshLoadLayout.isLoading()){
                    firstRefreshLoadLayout.setLoading(false);
                }
                BallApplication.getQueues().cancelAll("requestDateBall");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String ,String> map = new HashMap<>();
                map.put("item",BallApplication.DateBallItem);
                map.put("page",String.valueOf(page));
                if(BallApplication.getCity()!=null){
                    map.put("city",BallApplication.getCity());
                }
                return map;
            }
        };
        request.setTag("requestDateBall");
        BallApplication.getQueues().add(request);
    }

    /**
     * 轮播
     */
    public void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
    }

    /**
     * 停止轮播
     */
    public void stopPlay(){
        scheduledExecutorService.shutdown();
    }

    /**
     *执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (first_pager_ad) {
                currentItem = currentItem+1;
                AutoFlowing.obtainMessage().sendToTarget();
            }
        }

    }

    public FirstRefreshLoadLayout getFirstRefreshLoadLayout() {
        return firstRefreshLoadLayout;
    }

    public void PublishAfterRefresh(){
        page = 1;
        dateBalls.clear();
        requestDateBall();
    }


}
