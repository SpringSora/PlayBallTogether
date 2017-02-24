package com.example.springsora.balltogether.pager.modify;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BasePager;
import com.example.springsora.balltogether.custom_viewpager.MyViewPager;
import com.example.springsora.balltogether.playballtogether.ModifyActivity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by JJBOOM on 2016/4/23.
 */
public class NewPhonePager extends BasePager {

    private View view;

    private MyViewPager myViewPager;

    private EditText modify_phone_edit;

    private RelativeLayout modify_phone_sendcode_button;

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    public NewPhonePager(Context context,MyViewPager myViewPager)
    {
        super(context);
        this.myViewPager = myViewPager;
    }

    @Override
    public View initView() {
        view = LayoutInflater.from(context).inflate(R.layout.modify_phone_pager2,null);
        initControl();
        initData();
        return view;
    }

    @Override
    public void initData() {
        buttonListener();
    }

    private void initControl(){
        modify_phone_edit = (EditText) view.findViewById(R.id.modify_phone_edit);
        modify_phone_sendcode_button = (RelativeLayout) view.findViewById(R.id.modify_phone_sendcode_button);
        modify_phone_sendcode_button.setEnabled(false);
    }
    private void buttonListener(){
        modify_phone_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = modify_phone_edit.getText().toString();
                if(Pattern.matches(REGEX_MOBILE, phone)){
                    modify_phone_sendcode_button.setBackgroundResource(R.color.colorTitle);
                    modify_phone_sendcode_button.setEnabled(true);
                }else{
                    modify_phone_sendcode_button.setBackgroundResource(R.color.colorLine);
                    modify_phone_sendcode_button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        modify_phone_sendcode_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_phone_sendcode_button.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        ((ModifyActivity) context).getProgressDialog().show();
                        modify_phone_sendcode_button.setBackgroundResource(R.color.colorTitle);
                        isPhoneExist();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_phone_sendcode_button.setBackgroundResource(R.color.colorTitle);
                        break;
                }
                return true;
            }
        });
    }

    private void isPhoneExist(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/checkUserServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (Boolean.valueOf(s)){
                    BallApplication.setPhonenum(modify_phone_edit.getText().toString());
                    //SMSSDK.getVerificationCode(BallApplication.ChinaCode, modify_phone_edit.getText().toString());
                    ((ModifyActivity) context).getProgressDialog().dismiss();
                    myViewPager.setCurrentItem(2);

                }else{
                    Toast.makeText(context, "此电话号码已经被注册", Toast.LENGTH_SHORT).show();
                    ((ModifyActivity) context).getProgressDialog().dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
                ((ModifyActivity) context).getProgressDialog().dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("phone",modify_phone_edit.getText().toString());
                return  map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse arg0) {
                // TODO Auto-generated method stub
                String str = null;
                try {
                    str = new String(arg0.data,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str, HttpHeaderParser.parseCacheHeaders(arg0));
            }
        };
        request.setTag("isPhoneExist()");
        BallApplication.getQueues().add(request);
    }
}
