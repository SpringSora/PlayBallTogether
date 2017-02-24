package com.example.springsora.balltogether.pager.modify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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
import com.example.springsora.balltogether.bean.User;
import com.example.springsora.balltogether.custom_viewpager.MyViewPager;
import com.example.springsora.balltogether.playballtogether.ModifyActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by JJBOOM on 2016/4/23.
 */
public class ChangePhonePager extends BasePager {

    private View view;

    private int recLen = 60;

    private TextView modify_countdown;

    private RelativeLayout modify_countdown_button;

    private EditText modify_edit_code;

    private RelativeLayout modify_phone_submit_button;


    private Handler CountDownHandler;

    private MyViewPager myViewPager;


    /**
     * 数据存储
     */
    private SharedPreferences pref;
    /**
     * 数据存储编辑
     */
    private SharedPreferences.Editor editor;
    public ChangePhonePager(Context context,MyViewPager myViewPager) {
        super(context);
        this.myViewPager = myViewPager;
    }

    @Override
    public View initView() {
        view = LayoutInflater.from(context).inflate(R.layout.modify_phone_pager3,null);
        initControl();
        initData();
        return view;
    }

    @Override
    public void initData() {
        buttonListener();
        new Thread(){
            @Override
            public void run() {
                while (true){
                    if(myViewPager.getCurrentItem()==2){
                        CountDownHandler.sendEmptyMessage(1);
                        break;
                    }
                }
            }
        }.start();
    }

    private void initControl(){
        modify_countdown = (TextView) view.findViewById(R.id.modify_countdown);
        modify_countdown_button = (RelativeLayout) view.findViewById(R.id.modify_countdown_button);
        modify_edit_code = (EditText) view.findViewById(R.id.modify_edit_code);
        modify_phone_submit_button = (RelativeLayout) view.findViewById(R.id.modify_phone_submit_button);
        modify_phone_submit_button.setEnabled(false);
        modify_countdown_button.setEnabled(false);
        CountDownHandler  = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        recLen--;
                        modify_countdown.setText(""+recLen+"s");
                        if(recLen > 0){
                            Message message = new Message();
                            message.what = 1;
                            CountDownHandler.sendMessageDelayed(message,1000);
                        }else{
                            modify_countdown_button.setBackgroundResource(R.color.colorTitle);
                            modify_countdown_button.setEnabled(true);
                            modify_countdown.setText("重新发送验证码");
                        }
                        break;
                }
            }
        };

    }

    private void buttonListener(){

        modify_edit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (modify_edit_code.getText().toString() != null && !modify_edit_code.getText().toString().trim().equals("")) {
                    modify_phone_submit_button.setBackgroundResource(R.color.colorTitle);
                    modify_phone_submit_button.setEnabled(true);
                } else {
                    modify_phone_submit_button.setBackgroundResource(R.color.colorLine);
                    modify_phone_submit_button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        modify_phone_submit_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_phone_submit_button.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_phone_submit_button.setBackgroundResource(R.color.colorTitle);
                        //SMSSDK.submitVerificationCode(BallApplication.ChinaCode, BallApplication.getPhonenum(), modify_edit_code.getText().toString());
                        ModifyPhoneToServer();
                        modify_phone_submit_button.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_phone_submit_button.setBackgroundResource(R.color.colorTitle);
                        break;
                }
                return true;
            }
        });

        modify_countdown_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        modify_countdown_button.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        //SMSSDK.getVerificationCode(BallApplication.ChinaCode,BallApplication.getPhonenum());
                        CountDownHandler.sendEmptyMessage(1);
                        modify_countdown_button.setBackgroundResource(R.color.colorLine);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_countdown_button.setBackgroundResource(R.color.colorTitle);
                        break;
                }
                return true;
            }
        });
    }

    private void ModifyPhoneToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl+"/ModifyServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(Boolean.valueOf(s)){
                    BallApplication.getUser().setU_phone(BallApplication.getPhonenum());
                    BallApplication.setPhonenum(null);
                    saveUserToSharePref(BallApplication.getUser());
                    Intent intent = new Intent();
                    ((ModifyActivity)context).setResult(((ModifyActivity) context).RESULT_OK, intent);
                    ((ModifyActivity)context).finish();
                }else{
                    modify_phone_submit_button.setEnabled(true);
                    Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                modify_phone_submit_button.setEnabled(true);
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String ,String> map = new HashMap<>();
                map.put("Modify_type",BallApplication.Modify_Phone_Tag+"");
                map.put(BallApplication.Modify_Phone_Tag + "", BallApplication.getPhonenum());
                map.put("u_id",BallApplication.getUser().getU_id()+"");
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }

    private void saveUserToSharePref(User user){
        pref = context.getSharedPreferences("base64",context.MODE_PRIVATE);
        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            //创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            //将对象写入字节流
            oos.writeObject(user);
            //将字节流编码成base64字符串
            String UserBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor = pref.edit();
            editor.putString("User",UserBase64);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
