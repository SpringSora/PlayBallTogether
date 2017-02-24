package com.example.springsora.balltogether.pager.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BasePager;
import com.example.springsora.balltogether.bean.MyOrder;
import com.example.springsora.balltogether.custom_adapter.FourthAdapter;
import com.example.springsora.balltogether.custom_listview.FourthListView;
import com.example.springsora.balltogether.custom_view.CircleImageView;
import com.example.springsora.balltogether.playballtogether.AcceptActivity;
import com.example.springsora.balltogether.playballtogether.LoginActivity;
import com.example.springsora.balltogether.playballtogether.MainActivity;
import com.example.springsora.balltogether.playballtogether.MyDateBallActivity;
import com.example.springsora.balltogether.playballtogether.MyOrderActivity;
import com.example.springsora.balltogether.playballtogether.UserInfoActivity;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.example.springsora.balltogether.utils.HttpUtils;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JJBOOM on 2016/4/21.
 */
public class FourthPager extends BasePager {
    /**
     * 加载页面
     */
    private View view;
    /**
     * 自定义listview对象
     */
    private FourthListView fourthListView;
    /**
     * 加载头页面
     */
    private View headerView;
    /**
     * 适配器
     */
    private FourthAdapter fourthAdapter;
    /**
     * 登陆布局
     */
    private RelativeLayout user_login;

    /**
     *用户名
     */
    private TextView user_NiceName;
    /**
     * 头像
     */
    private CircleImageView user_pic;

    private  Handler handler;

    private Bitmap bitmap;

    private RelativeLayout person_pay;

    private RelativeLayout person_evaluate;

    private RelativeLayout  person_refund;

    private TextView pay_hint;

    private TextView eveluate_hint;

    private TextView refund_hint;

    private List<MyOrder> orders;

    private List<MyOrder> ispayOrders;

    private List<MyOrder> isevaluateOrders;

    private List<MyOrder> isrefundOrders;

    private RelativeLayout all_order_layout;

    private RelativeLayout invite_layout;

    private RelativeLayout invited_layout;

    public FourthPager(Context context) {
        super(context);
    }


    @Override
    public View initView() {
        orders = new ArrayList<>();
        ispayOrders = new ArrayList<>();
        isevaluateOrders = new ArrayList<>();
        isrefundOrders = new ArrayList<>();
        initControl();
        setHeader();
        initData();
        return view;
    }

    @Override
    public void initData() {
        fourthAdapter = new FourthAdapter();
        fourthListView.setAdapter(fourthAdapter);
        if(BallApplication.getUser()!=null){
            GetMyOrder();
        }
        ButtonListener();
    }

    /**
     * 初始化各个控件
     */
    private void initControl(){
        view = LayoutInflater.from(context).inflate(R.layout.pager_fourth,null);
        fourthListView = (FourthListView) view.findViewById(R.id.fourth_list_view);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                                if(bitmap!=null){
                                    user_pic.setImageBitmap(bitmap);
                                    BallApplication.setUserPic(bitmap);
                                }else{
                                    Toast.makeText(context,"头像获取失败",Toast.LENGTH_SHORT).show();
                                    BallApplication.setUserPic(null);
                                }
                        break;
                }
            }
        };
    }

    /**
     * 为listview添加一个头
     */
    private void setHeader(){
        headerView = LayoutInflater.from(context).inflate(R.layout.fourth_listview_header,null);
        user_login = (RelativeLayout) headerView.findViewById(R.id.user_login);
        user_NiceName = (TextView) headerView.findViewById(R.id.user_nicname);
        user_pic = (CircleImageView) headerView.findViewById(R.id.user_pic);
        if(BallApplication.getUser()!=null){
            if(BallApplication.getUser().getU_nickname()!=null){
                user_NiceName.setText(BallApplication.getUser().getU_nickname());
            }else{
                user_NiceName.setText(BallApplication.getUser().getU_phone());
            }
            if(BallApplication.getUser().getU_pic()!=null){
               // http://localhost:8080/PlayBallTogether/UserUpLoad/u+u_id/pic.png
                if(BallApplication.getUserPic()!=null){
                    user_pic.setImageBitmap(BallApplication.getUserPic());
                }else{
                    new Thread(){
                        @Override
                        public void run() {
                            bitmap =   HttpUtils.getNetWorkBitmap(BallApplication.ServerUrl + BallApplication.getUser().getU_pic());
                            handler.sendEmptyMessage(0);
                        }
                    }.start();

                }
            }
        }
        //item2
        View secondView = LayoutInflater.from(context).inflate(R.layout.fourth_listview_header2,null);
        all_order_layout = (RelativeLayout) secondView.findViewById(R.id.all_order_layout);
        person_pay = (RelativeLayout) secondView.findViewById(R.id.person_pay);
        person_evaluate = (RelativeLayout) secondView.findViewById(R.id.person_evaluate);
        person_refund = (RelativeLayout) secondView.findViewById(R.id.person_refund);
        pay_hint = (TextView) secondView.findViewById(R.id.pay_hint);
        eveluate_hint = (TextView) secondView.findViewById(R.id.eveluate_hint);
        refund_hint = (TextView) secondView.findViewById(R.id.refund_hint);
        pay_hint.setVisibility(View.GONE);
        eveluate_hint.setVisibility(View.GONE);
        refund_hint.setVisibility(View.GONE);
        //item3
        View ThirdView = LayoutInflater.from(context).inflate(R.layout.fourth_listview_header3,null);
        invite_layout = (RelativeLayout) ThirdView.findViewById(R.id.invite_layout);

        //item4
        View FourthView = LayoutInflater.from(context).inflate(R.layout.fourth_listview_header4,null);
        invited_layout = (RelativeLayout) FourthView.findViewById(R.id.invited_layout);

        //add
        fourthListView.addHeaderView(headerView);
        fourthListView.addHeaderView(secondView);
        fourthListView.addHeaderView(ThirdView);
        fourthListView.addHeaderView(FourthView);
    }

    /**
     * 各类监听事件
     */
    private void ButtonListener(){

            user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BallApplication.getUser()==null){
                    Intent intent = new Intent(context, LoginActivity.class);
                    ((MainActivity)context).startActivityForResult(intent,4);
                }else{
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    ((MainActivity)context).startActivityForResult(intent,4);
                }
                }
            });
        person_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BallApplication.getUser()==null){
                    Intent intent = new Intent(context, LoginActivity.class);
                    ((MainActivity)context).startActivityForResult(intent,4);
                }else{
                    Intent intent = new Intent(context, MyOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",R.id.person_pay);
                    bundle.putSerializable("orders", (Serializable) ispayOrders);
                    intent.putExtras(bundle);
                    ((MainActivity)context).startActivityForResult(intent, 4);
                }
            }
        });

        person_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BallApplication.getUser()==null){
                    Intent intent = new Intent(context, LoginActivity.class);
                    ((MainActivity)context).startActivityForResult(intent,4);
                }else{
                    Intent intent = new Intent(context, MyOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", R.id.person_evaluate);
                    bundle.putSerializable("orders", (Serializable) isevaluateOrders);
                    intent.putExtras(bundle);
                    ((MainActivity)context).startActivityForResult(intent, 4);
                }


            }
        });

        person_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BallApplication.getUser()==null){
                    Intent intent = new Intent(context, LoginActivity.class);
                    ((MainActivity)context).startActivityForResult(intent,4);
                }else{
                    Intent intent = new Intent(context, MyOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orders", (Serializable) isrefundOrders);
                    bundle.putInt("id", R.id.person_refund);
                    intent.putExtras(bundle);
                    ((MainActivity)context).startActivityForResult(intent, 4);
                }
            }
        });

        all_order_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BallApplication.getUser()==null){
                    Intent intent = new Intent(context, LoginActivity.class);
                    ((MainActivity)context).startActivityForResult(intent,4);
                }else{
                    Intent intent = new Intent(context, MyOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", R.id.all_order_layout);
                    bundle.putSerializable("orders", (Serializable) orders);
                    intent.putExtras(bundle);
                    ((MainActivity)context).startActivityForResult(intent, 4);
                }

            }
        });

        invite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BallApplication.getUser()==null){
                    Intent intent = new Intent(context, LoginActivity.class);
                    ((MainActivity)context).startActivityForResult(intent,4);
                }else{
                    Intent intent = new Intent(context, MyDateBallActivity.class);
                    ((MainActivity)context).startActivityForResult(intent,4);
                }
            }
        });

        invited_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BallApplication.getUser()==null){
                    Intent intent = new Intent(context, LoginActivity.class);
                    ((MainActivity)context).startActivityForResult(intent,4);
                }else{
                    Intent intent = new Intent(context, AcceptActivity.class);
                    ((MainActivity)context).startActivityForResult(intent, 4);
                }
            }
        });
        }

    private void GetMyOrder(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/GetOrderServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(!"false".equals(s)){
                    Type type = new TypeToken<List<MyOrder>>(){}.getType();
                    List<MyOrder> temp = GsonUtil.jsonToBeanDateTime(s,type);
                    orders.addAll(temp);
                        for(int i = 0;i < orders.size(); i++){
                            if(orders.get(i).getO_ispay()==1){
                                pay_hint.setVisibility(View.VISIBLE);
                                ispayOrders.add(orders.get(i));
                            }
                            if(orders.get(i).getO_isevaluate()==1){
                                eveluate_hint.setVisibility(View.VISIBLE);
                                isevaluateOrders.add(orders.get(i));
                            }
                            if(orders.get(i).getO_isrefund()==1){
                                refund_hint.setVisibility(View.VISIBLE);
                                isrefundOrders.add(orders.get(i));
                            }
                        }
                    pay_hint.setText(ispayOrders.size()+"");
                    eveluate_hint.setText(isevaluateOrders.size()+"");
                    refund_hint.setText(isrefundOrders.size()+"");
                }else{
                    Toast.makeText(context,"数据获取失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"网路错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("u_id",BallApplication.getUser().getU_id().toString());
                return map;
            }
        };
        request.setTag("GetMyOrder");
        BallApplication.getQueues().add(request);
    }

}

