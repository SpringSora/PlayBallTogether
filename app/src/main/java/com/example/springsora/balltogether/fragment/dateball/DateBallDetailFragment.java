package com.example.springsora.balltogether.fragment.dateball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.DateBall;
import com.example.springsora.balltogether.bean.User;
import com.example.springsora.balltogether.custom_adapter.DateBallDetailAdapter;
import com.example.springsora.balltogether.playballtogether.LoginActivity;
import com.example.springsora.balltogether.playballtogether.ViewUserInfoActivity;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.example.springsora.balltogether.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JJBOOM on 2016/5/5.
 */
public class DateBallDetailFragment extends BaseFragment {

    private View view;
    private DateBall dateBall;
    private ListView dateball_detail_listview;
    private ImageView dateball_detail_back;
    private DateBallDetailAdapter adapter;
    private ImageView dateball_detail_userpic;
    private TextView dateball_detail_nickname;
    private TextView dateball_detail_time;
    private TextView dateball_detail_title;
    private TextView dateball_detail_content;
    private ImageView dateball_detail_pic;
    private ImageView dateball_detail_type;
    private TextView dateball_detail_num;
    private TextView dateball_detail_ballground;
    private TextView dateball_detail_datetime;
    private TextView dateball_detail_location;
    private ImageView dateball_detail_usersex;
    private TextView dateball_detail_phone;
    private RelativeLayout send_date;
    private LinearLayout dateball_detail_userinfo;
    private Bitmap userPic;
    private Bitmap Pic;
    private AlertDialog.Builder alertDialog;
    private LinearLayout add_user_layout1;
    private LinearLayout add_user_layout2;
    private List<User> users;

    private float o_price;

    private Handler handler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case R.id.dateball_detail_userpic:
                    dateball_detail_userpic.setImageBitmap(userPic);
                    break;
                case R.id.dateball_detail_pic:
                    dateball_detail_pic.setImageBitmap(Pic);
                    break;
            }
        }
    };;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.dateball_detail_fragment,null);
        alertDialog = new AlertDialog.Builder(getActivity());
        users = new ArrayList<>();
        initControl();
        addHeaderItem1();
        addHeaderItem2();
        addHeaderItem3();
        addHeaderItem4();
        if(BallApplication.getUser().getU_id().intValue()!=dateBall.getUser().getU_id().intValue()){
            addHeaderItem5();
        }
        if(getArguments().getInt("type") == 2){
            addHeaderItem6();
        }
        initDateBall();
        return view;
    }

    private void initControl(){
        dateBall = (DateBall) getArguments().getSerializable("DateBall");
        dateball_detail_listview = (ListView) view.findViewById(R.id.dateball_detail_listview);
        dateball_detail_back = (ImageView) view.findViewById(R.id.dateball_detail_back);
        adapter = new DateBallDetailAdapter();
        dateball_detail_listview.setAdapter(adapter);
    }

    private void buttonListener(){
        dateball_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        dateball_detail_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewUserInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("User", dateBall.getUser());
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

    }

    private void addHeaderItem1(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_detail_item1,null);
        dateball_detail_userpic = (ImageView) view.findViewById(R.id.dateball_detail_userpic);
        dateball_detail_nickname = (TextView) view.findViewById(R.id.dateball_detail_nickname);
        dateball_detail_time = (TextView) view.findViewById(R.id.dateball_detail_time);
        dateball_detail_location = (TextView) view.findViewById(R.id.dateball_detail_location);
        dateball_detail_usersex = (ImageView) view.findViewById(R.id.dateball_detail_usersex);
        dateball_detail_userinfo = (LinearLayout) view.findViewById(R.id.dateball_detail_userinfo);
        dateball_detail_listview.addHeaderView(view);
    }
    private void addHeaderItem2(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_detail_item2,null);
        dateball_detail_title = (TextView) view.findViewById(R.id.dateball_detail_title);
        dateball_detail_content = (TextView) view.findViewById(R.id.dateball_detail_content);
        dateball_detail_pic = (ImageView) view.findViewById(R.id.dateball_detail_pic);
        dateball_detail_listview.addHeaderView(view);
    }

    private void  addHeaderItem3(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_detail_item3,null);
        dateball_detail_type = (ImageView) view.findViewById(R.id.dateball_detail_type);
        dateball_detail_num = (TextView) view.findViewById(R.id.dateball_detail_num);
        dateball_detail_ballground = (TextView) view.findViewById(R.id.dateball_detail_ballground);
        dateball_detail_listview.addHeaderView(view);
    }
    private void  addHeaderItem4(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_detail_item4,null);
        dateball_detail_datetime = (TextView) view.findViewById(R.id.dateball_detail_datetime);
        dateball_detail_phone = (TextView) view.findViewById(R.id.dateball_detail_phone);
        dateball_detail_listview.addHeaderView(view);
    }

    private void addHeaderItem5(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_detail_item5,null);
        send_date = (RelativeLayout) view.findViewById(R.id.send_date);
        send_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send_date.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        send_date.setBackgroundResource(R.color.colorTitle);
                        break;
                    case MotionEvent.ACTION_UP:
                        send_date.setBackgroundResource(R.color.colorTitle);
                        if (BallApplication.getUser() == null) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            getActivity().startActivityForResult(intent, 1);
                        } else {
                            if(dateBall.getBallGround()!=null){
                                String start[] = dateBall.getD_starttime().split(":");
                                String end[] = dateBall.getD_endtime().split(":");
                                Integer time = ((Integer.valueOf(end[0])-Integer.valueOf(start[0]))*60+Integer.valueOf(end[1])-Integer.valueOf(start[1]))/60;
                                o_price = time*dateBall.getBallGround().getGroundPrice()/(dateBall.getD_num()+1);
                            alertDialog.setTitle("是否接受")
                                    .setMessage("开始日期："+dateBall.getD_date()+"\n"+"开始时间："+dateBall.getD_starttime()+"\n"+"结束时间："+dateBall.getD_endtime()+"\n"+"应付金额："+o_price)
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).setPositiveButton("接受", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    WritePromise ();
                                }
                            }).show();
                            }else{
                                alertDialog.setTitle("是否接受")
                                        .setMessage("开始日期："+dateBall.getD_date()+"\n"+"开始时间："+dateBall.getD_starttime()+"\n"+"结束时间："+dateBall.getD_endtime())
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).setPositiveButton("接受", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        WritePromise ();
                                    }
                                }).show();
                            }
                        }
                        break;
                }
                return true;
            }
        });
        dateball_detail_listview.addHeaderView(view);
    }

    private void addHeaderItem6(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_detail_item6,null);
        add_user_layout1 = (LinearLayout) view.findViewById(R.id.add_user_layout1);
        add_user_layout2 = (LinearLayout) view.findViewById(R.id.add_user_layout2);
        getAddUsers();
        dateball_detail_listview.addHeaderView(view);
    }

    private void initDateBall(){
        if(dateBall.getUser().getU_pic()!=null){
            new Thread(){
                @Override
                public void run() {
                    userPic = HttpUtils.getNetWorkBitmap(BallApplication.ServerUrl+dateBall.getUser().getU_pic());
                    handler.sendEmptyMessage(R.id.dateball_detail_userpic);
                }
            }.start();
        }else{
            dateball_detail_userpic.setImageResource(R.mipmap.default_logo);
        }
        if(dateBall.getUser().getU_nickname()!=null&&!dateBall.getUser().getU_nickname().trim().equals("")){
            dateball_detail_nickname.setText(dateBall.getUser().getU_nickname());
        }else{
            dateball_detail_nickname.setText(dateBall.getUser().getU_phone());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateball_detail_time.setText(simpleDateFormat.format(dateBall.getD_time()));
        dateball_detail_location.setText(dateBall.getD_location());
        if(dateBall.getUser().getU_sex()!=null){
            if("1".equals(dateBall.getUser().getU_sex())){
                dateball_detail_usersex.setImageResource(R.mipmap.boy);
            }else{
                dateball_detail_usersex.setImageResource(R.mipmap.girl);
            }
        }else{
            dateball_detail_usersex.setImageResource(R.mipmap.sex);
        }
        dateball_detail_title.setText(dateBall.getD_title());
        dateball_detail_content.setText(dateBall.getD_text());
        if(dateBall.getD_pic()!=null){
            new Thread(){
                @Override
                public void run() {
                    Pic = HttpUtils.getNetWorkBitmap(BallApplication.ServerUrl+dateBall.getD_pic());
                    handler.sendEmptyMessage(R.id.dateball_detail_pic);
                }
            }.start();
        }else{
            dateball_detail_pic.setVisibility(View.GONE);
        }
        dateball_detail_type.setImageResource(BallApplication.getMipmapHobby().get(dateBall.getD_type()));
        dateball_detail_num.setText(dateBall.getD_num()+"");
        if(dateBall.getBallGround()==null){
            dateball_detail_ballground.setText("未选择球场");
        }else{
            dateball_detail_ballground.setText(dateBall.getBallGround().getGroundName());
        }
        dateball_detail_datetime.setText(dateBall.getD_date() + " " + dateBall.getD_starttime() + "～" + dateBall.getD_endtime());
        dateball_detail_phone.setText(dateBall.getUser().getU_phone());
    }


    private void WritePromise (){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/WritePromiseServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(dateBall.getBallGround()!=null){
                    WriteMyOrder();
                }else{
                    Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("u_id",BallApplication.getUser().getU_id().toString());
                map.put("d_id",dateBall.getD_id().toString());
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }

    private void WriteMyOrder(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/GenerateOrderServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(Boolean.valueOf(s)){
                    Toast.makeText(getActivity(),"发送成功",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(),"网路错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("u_id",BallApplication.getUser().getU_id()+"");
                map.put("p_id",dateBall.getBallGround().getP_id()+"");
                map.put("o_price",o_price+"");
                map.put("o_date",dateBall.getD_date());
                map.put("o_starttime",dateBall.getD_starttime());
                map.put("o_endtime",dateBall.getD_endtime());
                map.put("type","promise");
                map.put("d_id",dateBall.getD_id().toString());
                return map;
            }
        };
        request.setTag("WriteMyOrder");
        BallApplication.getQueues().add(request);
    }

    private void getAddUsers(){
        final StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/GetUsersServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(!"false".equals(s)){
                    Type type = new TypeToken<List<User>>(){}.getType();
                    List<User> temp = GsonUtil.jsonToBean(s, type);
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    if(temp!=null&&temp.size()!=0){
                        for(int i = 0;i < temp.size();i++){
                            TextView textView = new TextView(getActivity());
                            textView.setLayoutParams(params);
                            Resources resource = getActivity().getBaseContext().getResources();
                            ColorStateList csl =  resource.getColorStateList(R.color.colorTitle);
                            textView.setTextColor(csl);
                            if(temp.get(i).getU_nickname()!=null){
                                textView.setText(temp.get(i).getU_nickname());
                            }else{
                                textView.setText(temp.get(i).getU_phone());
                            }
                            if(i%2==0){
                                add_user_layout1.addView(textView);
                            } else{
                                add_user_layout2.addView(textView);
                            }
                        }
                    }else{

                    }

                }else {
                    Toast.makeText(getActivity(),"获取用户失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > map = new HashMap<>();
                map.put("d_id",dateBall.getD_id()+"");
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }

}
