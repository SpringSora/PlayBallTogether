package com.example.springsora.balltogether.fragment.ballground;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.BallGround;
import com.example.springsora.balltogether.custom_adapter.BallGroundAdapter;
import com.example.springsora.balltogether.custom_adapter.BallGroundPagerAdapter;
import com.example.springsora.balltogether.custom_view.CircleImageView;
import com.example.springsora.balltogether.custom_view.FixedSpeedScroller;
import com.example.springsora.balltogether.utils.AsyncImageLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Springsora on 2016/5/9.
 */
public class BallGroundDetailFragment2 extends BaseFragment {
    private View view;
    private BallGround ballGround;
    private ImageView ballground_detail_back;
    private ListView ballground_detail_listview;
    private ViewPager ballground_detail_viewpager;
    private LinearLayout ballground_point_layout;
    private List<ImageView> imageViews;
    private List<ImageView> pointImageViews;
    private AsyncImageLoader asyncImageLoader;
    private FixedSpeedScroller mScroller;
    private TextView ballground_detail_type;

    private TextView ballground_detail_num;

    private TextView ballground_detail_left;

    private int currentItem = 400;

    private Handler AutoFlowing;

    private TextView ballground_detail_name;

    private TextView ballground_detail_address;

    private ImageView ballground_detail_phone;

    private TextView ballground_detail_price;

    private TextView ballground_detail_info;

    private CircleImageView comment_userpic;

    private TextView comment_nickname;

    private TextView comment_datetime;

    private TextView comment_content;

    private TextView comment_num;

    private RelativeLayout view_all_comment;

    private RelativeLayout ballground_detail_submit;

    private AlertDialog.Builder alertDialog;

    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.ballground_detail,null);
        asyncImageLoader = new AsyncImageLoader();
        initControl();
        initHeader();
        return view;
    }

    private void initControl(){
        ballGround = (BallGround) getArguments().getSerializable("BallGround");
        imageViews = new ArrayList<>();
        pointImageViews = new ArrayList<>();
        alertDialog = new AlertDialog.Builder(getActivity());
        ballground_detail_back = (ImageView) view.findViewById(R.id.ballground_detail_back);
        ballground_detail_listview = (ListView) view.findViewById(R.id.ballground_detail_listview);
        ballground_detail_listview.setAdapter(new BallGroundAdapter());
    }

    private void initHeader(){
        View item1 = LayoutInflater.from(getActivity()).inflate(R.layout.ballground_detail_item1,null);
        ballground_detail_viewpager = (ViewPager) item1.findViewById(R.id.ballground_detail_viewpager);
        ballground_point_layout = (LinearLayout) item1.findViewById(R.id.ballground_point_layout);
        ballground_detail_name = (TextView) item1.findViewById(R.id.ballground_detail_name);
        ballground_detail_address = (TextView) item1.findViewById(R.id.ballground_detail_address);
        ballground_detail_phone = (ImageView) item1.findViewById(R.id.ballground_detail_phone);
        ballground_detail_price = (TextView) item1.findViewById(R.id.ballground_detail_price);
        ballground_detail_name.setText(ballGround.getGroundName());
        ballground_detail_address.setText(ballGround.getAddress());
        ballground_detail_phone.setTag(ballGround.getGroundPhone());
        ballground_detail_price.setText(ballGround.getGroundPrice() + " 元/时");
        AutoFlowing = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ballground_detail_viewpager.setCurrentItem(currentItem);
            }
        };
        for (int i=0;i<3;i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.mipmap.preview);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(imageView);
            ImageView dotView =  new ImageView(getActivity());
            dotView.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.width = 50;
            params.height = 50;
            params.leftMargin = 2;
            params.rightMargin = 2;
            ballground_point_layout.addView(dotView, params);
            pointImageViews.add(dotView);
        }
        ballground_detail_viewpager.setAdapter(new BallGroundPagerAdapter(imageViews));
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(ballground_detail_viewpager.getContext(),new AccelerateDecelerateInterpolator());
            mField.set(ballground_detail_viewpager,mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ballground_detail_viewpager.setCurrentItem(400);
        mScroller.setmDuration(500);
        GetImageView();
        //item2
        View item2 = LayoutInflater.from(getActivity()).inflate(R.layout.ballground_detail_item2, null);
        ballground_detail_type = (TextView) item2.findViewById(R.id.ballground_detail_type);
        ballground_detail_num = (TextView) item2.findViewById(R.id.ballground_detail_num);
        ballground_detail_left = (TextView) item2.findViewById(R.id.ballground_detail_left);
        ballground_detail_info = (TextView) item2.findViewById(R.id.ballground_detail_info);
        ballground_detail_type.setText(BallApplication.getHobbyMap().get(ballGround.getBallType() + ""));
        ballground_detail_num.setText(ballGround.getGroundNum() + " 个");
        ballground_detail_left.setText(ballGround.getGroundLeft() + " 个");
        ballground_detail_info.setText(ballGround.getGroundInfo());
        //item3
        View item3 = LayoutInflater.from(getActivity()).inflate(R.layout.ballground_detail_item3,null);
        comment_userpic = (CircleImageView) item3.findViewById(R.id.comment_userpic);
        comment_nickname = (TextView) item3.findViewById(R.id.comment_nickname);
        comment_datetime = (TextView) item3.findViewById(R.id.comment_datetime);
        comment_content = (TextView) item3.findViewById(R.id.comment_content);
        comment_num = (TextView) item3.findViewById(R.id.comment_num);
        view_all_comment = (RelativeLayout) item3.findViewById(R.id.view_all_comment);
        //item5
        View item5 = LayoutInflater.from(getActivity()).inflate(R.layout.ballground_detail_item5,null);
        ballground_detail_submit = (RelativeLayout) item5.findViewById(R.id.ballground_detail_submit);

        //add
        ballground_detail_listview.addHeaderView(item1);
        ballground_detail_listview.addHeaderView(item2);
        ballground_detail_listview.addHeaderView(item3);
        ballground_detail_listview.addHeaderView(item5);
    }

    private void buttonListener(){
        ballground_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        ballground_detail_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                for (int i = 0; i < pointImageViews.size(); i++) {
                    if (i == currentItem % 3) {
                        pointImageViews.get(i).setImageResource(R.mipmap.point_deep);
                    } else {
                        pointImageViews.get(i).setImageResource(R.mipmap.point);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ballground_detail_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setTitle("是否拨打电话")
                        .setMessage(ballGround.getGroundPhone())
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ballGround.getGroundPhone()));
                        startActivity(intent);
                    }
                }).show();

            }
        });

        ballground_detail_submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        ballground_detail_submit.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        ballground_detail_submit.setBackgroundResource(R.color.colorTitle);
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ballGround",ballGround);
                        intent.putExtras(bundle);
                        getActivity().setResult(Activity.RESULT_OK,intent);
                        getActivity().finish();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        ballground_detail_submit.setBackgroundResource(R.color.colorTitle);
                        break;
                }
                return true;
            }
        });

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
            synchronized (ballground_detail_viewpager) {
                currentItem = currentItem+1;
                AutoFlowing.obtainMessage().sendToTarget();
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        startPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopPlay();
    }


    private void GetImageView(){

        Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + "/" +ballGround.getBallGroundPic1Path(), new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                imageViews.get(0).setImageDrawable(imageDrawable);
                ballground_detail_viewpager.getAdapter().notifyDataSetChanged();
            }
        });
        if(drawable!=null){
            imageViews.get(0).setImageDrawable(drawable);
        }else{
            imageViews.get(0).setImageResource(R.mipmap.preview);
        }

        Drawable drawable2 = asyncImageLoader.loadDrawable(BallApplication.ServerUrl +"/" + ballGround.getBallGroundPic2Path(), new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                imageViews.get(1).setImageDrawable(imageDrawable);
                ballground_detail_viewpager.getAdapter().notifyDataSetChanged();
            }
        });
        if(drawable2!=null){
            imageViews.get(1).setImageDrawable(drawable2);
        }else{
            imageViews.get(1).setImageResource(R.mipmap.preview);
        }

        Drawable drawable3 = asyncImageLoader.loadDrawable(BallApplication.ServerUrl +"/" + ballGround.getBallGroundPic3Path(), new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                imageViews.get(2).setImageDrawable(imageDrawable);
                ballground_detail_viewpager.getAdapter().notifyDataSetChanged();
            }
        });
        if(drawable3!=null){
            imageViews.get(2).setImageDrawable(drawable3);
        }else{
            imageViews.get(2).setImageResource(R.mipmap.preview);
        }
    }

}
