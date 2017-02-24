package com.example.springsora.balltogether.fragment.ballground;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.BallGround;
import com.example.springsora.balltogether.custom_adapter.BallGroundAdapter;
import com.example.springsora.balltogether.custom_adapter.BallGroundPagerAdapter;
import com.example.springsora.balltogether.custom_view.CircleImageView;
import com.example.springsora.balltogether.custom_view.FixedSpeedScroller;
import com.example.springsora.balltogether.playballtogether.ViewCommentActivity;
import com.example.springsora.balltogether.utils.AsyncImageLoader;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by JJBOOM on 2016/5/6.
 */
public class BallGroundDetailFragment1 extends BaseFragment {

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

    private ProgressDialog progressDialog;

    private RelativeLayout ballground_detail_set_date;

    private RelativeLayout ballground_detail_set_starttime;

    private RelativeLayout ballground_detail_set_endtime;

    private TextView ballground_detail_date;

    private TextView ballground_detail_starttime;

    private TextView ballground_detail_endtime;

    private DatePickerDialog datePickerDialog;

    private TimePickerDialog timePickerDialog;

    private String NewDate;

    private String StartTime;

    private String EndTime;

    private Date CurrentDate;

    private Float o_price;

    /**
     * 定时任务
     */
    private ScheduledExecutorService scheduledExecutorService;

    private AlertDialog.Builder alertDialog;

    private AlertDialog.Builder builder;

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
        builder = new AlertDialog.Builder(getActivity());
        ballground_detail_back = (ImageView) view.findViewById(R.id.ballground_detail_back);
        ballground_detail_listview = (ListView) view.findViewById(R.id.ballground_detail_listview);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在生成订单...");
        progressDialog.setCancelable(false);
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
        ballground_detail_type.setText(BallApplication.getHobbyMap().get(ballGround.getBallType()+""));
        ballground_detail_num.setText(ballGround.getGroundNum() + " 个");
        ballground_detail_left.setText(ballGround.getGroundLeft() + " 个");
        ballground_detail_info.setText(ballGround.getGroundInfo());
        //item3
        View item3 = null;
        if(ballGround.getComments().size()!=0){
            item3 = LayoutInflater.from(getActivity()).inflate(R.layout.ballground_detail_item3, null);
            comment_userpic = (CircleImageView) item3.findViewById(R.id.comment_userpic);
            Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + ballGround.getComments().get(0).getUser().getU_pic(), new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    comment_userpic.setImageDrawable(imageDrawable);
                }
            });
            if(drawable!=null){
                comment_userpic.setImageDrawable(drawable);
            }else{
                comment_userpic.setImageResource(R.mipmap.default_logo);
            }
            comment_nickname = (TextView) item3.findViewById(R.id.comment_nickname);
            comment_nickname.setText(ballGround.getComments().get(0).getUser().getU_nickname());
            comment_datetime = (TextView) item3.findViewById(R.id.comment_datetime);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            comment_datetime.setText(format.format(ballGround.getComments().get(0).getDatetime()));
            comment_content = (TextView) item3.findViewById(R.id.comment_content);
            comment_content.setText(ballGround.getComments().get(0).getC_content());
            comment_num = (TextView) item3.findViewById(R.id.comment_num);
            comment_num.setText("查看全部"+ballGround.getComments().size()+"条评论");
            view_all_comment = (RelativeLayout) item3.findViewById(R.id.view_all_comment);
            view_all_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ViewCommentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("comments", (Serializable) ballGround.getComments());
                    bundle.putInt("p_id",ballGround.getP_id());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,0);
                }
            });
        }

        //item4
        View item4 = LayoutInflater.from(getActivity()).inflate(R.layout.ballground_detail_item4,null);
        ballground_detail_set_date = (RelativeLayout) item4.findViewById(R.id.ballground_detail_set_date);
        ballground_detail_set_starttime = (RelativeLayout) item4.findViewById(R.id.ballground_detail_set_starttime);
        ballground_detail_set_endtime = (RelativeLayout) item4.findViewById(R.id.ballground_detail_set_endtime);
        ballground_detail_date = (TextView) item4.findViewById(R.id.ballground_detail_date);
        ballground_detail_starttime = (TextView) item4.findViewById(R.id.ballground_detail_starttime);
        ballground_detail_endtime = (TextView) item4.findViewById(R.id.ballground_detail_endtime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        CurrentDate = new Date(System.currentTimeMillis());//获取当前时间
        NewDate = formatter.format(CurrentDate);
        ballground_detail_date.setText(NewDate);
        //item5
        View item5 = LayoutInflater.from(getActivity()).inflate(R.layout.ballground_detail_item5,null);
        ballground_detail_submit = (RelativeLayout) item5.findViewById(R.id.ballground_detail_submit);
        //add
        ballground_detail_listview.addHeaderView(item1);
        ballground_detail_listview.addHeaderView(item2);
        if(item3!=null){
            ballground_detail_listview.addHeaderView(item3);
        }
        ballground_detail_listview.addHeaderView(item4);
        ballground_detail_listview.addHeaderView(item5);

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

        ballground_detail_set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePickDialog();
            }
        });

        ballground_detail_set_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePickDialog(R.id.ballground_detail_set_starttime);
            }
        });

        ballground_detail_set_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePickDialog(R.id.ballground_detail_set_endtime);
            }
        });


        ballground_detail_submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ballground_detail_submit.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        ballground_detail_submit.setBackgroundResource(R.color.colorTitle);
                        ballground_detail_submit.setEnabled(false);

                        if (StartTime != null && !"".equals(StartTime)) {
                            if (EndTime != null && !"".equals(EndTime)) {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                String CurrentTimeString = format.format(CurrentDate);
                                String str1[] = NewDate.split("-");
                                String str2[] = CurrentTimeString.split("-");
                                boolean flag;
                                if (Integer.valueOf(str1[0]) - Integer.valueOf(str2[0]) > 0) {
                                    flag = true;
                                } else if (Integer.valueOf(str1[0]) - Integer.valueOf(str2[0]) == 0) {
                                    if (Integer.valueOf(str1[1]) - Integer.valueOf(str2[1]) > 0) {
                                        flag = true;
                                    } else if (Integer.valueOf(str1[1]) - Integer.valueOf(str2[1]) == 0) {
                                        if (Integer.valueOf(str1[2]) - Integer.valueOf(str2[2]) > 0) {
                                            flag = true;
                                        } else if (Integer.valueOf(str1[2]) - Integer.valueOf(str2[2]) == 0) {
                                            flag = true;
                                        } else {
                                            flag = false;
                                            Toast.makeText(getActivity(), "日期选择错误", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        flag = false;
                                        Toast.makeText(getActivity(), "日期选择错误", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    flag = false;
                                    Toast.makeText(getActivity(), "日期选择错误", Toast.LENGTH_SHORT).show();
                                }

                                if (flag) {
                                    int StartHour = Integer.valueOf(StartTime.split(":")[0]);
                                    int StartMin = Integer.valueOf(StartTime.split(":")[1]);
                                    int EndHour = Integer.valueOf(EndTime.split(":")[0]);
                                    int EndMin = Integer.valueOf(EndTime.split(":")[1]);
                                    if (EndHour - StartHour < 0) {
                                        Toast.makeText(getActivity(), "时间选择错误", Toast.LENGTH_SHORT).show();
                                        ballground_detail_submit.setEnabled(true);
                                    } else if (StartHour - EndHour == 0) {
                                        if (StartMin - EndMin < 0) {
                                            Toast.makeText(getActivity(), "运动时间不能小于一个小时", Toast.LENGTH_SHORT).show();
                                            ballground_detail_submit.setEnabled(true);
                                        } else {
                                            Toast.makeText(getActivity(), "时间选择错误", Toast.LENGTH_SHORT).show();
                                            ballground_detail_submit.setEnabled(true);
                                        }
                                    } else if (EndHour - StartHour == 1) {
                                        if (EndMin - StartMin < 0) {
                                            Toast.makeText(getActivity(), "运动时间不能小于一个小时", Toast.LENGTH_SHORT).show();
                                            ballground_detail_submit.setEnabled(true);
                                        } else {
                                            int c = (EndMin - StartMin + 60) / 60;
                                            o_price = ballGround.getGroundPrice() * c;
                                            builder.setTitle("确认信息")
                                                    .setMessage("开始日期： " + NewDate + "\n" + "开始时间： " + StartTime + "\n" + "结束时间： " + EndTime + "\n" + "应付金额： " + o_price)
                                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            ballground_detail_submit.setEnabled(true);
                                                        }
                                                    }).setPositiveButton("支付", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    progressDialog.show();
                                                    WriteMyOrder();
                                                }
                                            }).show();
                                        }
                                    } else {
                                        int c = ((EndHour - StartHour) * 60 + (EndMin - StartMin)) / 60;
                                        o_price = ballGround.getGroundPrice() * c;
                                        builder.setTitle("确认信息")
                                                .setMessage("开始日期： " + NewDate + "\n" + "开始时间： " + StartTime + "\n" + "结束时间： " + EndTime + "\n" + "应付金额： " + o_price)
                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ballground_detail_submit.setEnabled(true);
                                                    }
                                                }).setPositiveButton("支付", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                progressDialog.show();
                                                WriteMyOrder();
                                            }
                                        }).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "日期选择错误", Toast.LENGTH_SHORT).show();
                                    ballground_detail_submit.setEnabled(true);
                                }
                            } else {
                                Toast.makeText(getActivity(), "请选择结束时间", Toast.LENGTH_SHORT).show();
                                ballground_detail_submit.setEnabled(true);
                            }
                        } else {
                            Toast.makeText(getActivity(), "请选择开始时间", Toast.LENGTH_SHORT).show();
                            ballground_detail_submit.setEnabled(true);
                        }


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

    private void WriteMyOrder(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/GenerateOrderServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                ballground_detail_submit.setEnabled(true);
                if(Boolean.valueOf(s)){
                    Intent intent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(),"支付失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                ballground_detail_submit.setEnabled(true);
                Toast.makeText(getActivity(),"网路错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("u_id",BallApplication.getUser().getU_id()+"");
                map.put("p_id",ballGround.getP_id()+"");
                map.put("o_price",o_price.toString());
                map.put("o_date",NewDate);
                map.put("o_starttime",StartTime);
                map.put("o_endtime",EndTime);
                return map;
            }
        };
        request.setTag("WriteMyOrder");
        BallApplication.getQueues().add(request);
    }

    private void ShowDatePickDialog(){
        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                NewDate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                ballground_detail_date.setText(NewDate);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void ShowTimePickDialog(final int id){
        final Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                switch (id){
                    case R.id.ballground_detail_set_starttime:
                        if(minute<10){
                            StartTime = hourOfDay+":"+"0"+minute;
                        }else{
                            StartTime = hourOfDay+":"+minute;
                        }
                        ballground_detail_starttime.setText(StartTime);
                        break;
                    case R.id.ballground_detail_set_endtime:
                        if(minute<10){
                            EndTime = hourOfDay+":"+"0"+minute;
                        }else{
                            EndTime = hourOfDay+":"+minute;
                        }
                        ballground_detail_endtime.setText(EndTime );
                        break;
                }
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }
}
