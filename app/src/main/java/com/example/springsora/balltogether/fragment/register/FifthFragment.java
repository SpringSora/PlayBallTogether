package com.example.springsora.balltogether.fragment.register;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.MD5.MD5;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.User;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JJBOOM on 2016/4/22.
 */
public class FifthFragment extends BaseFragment {

    private View view;

    private EditText register_edit_password;

    private RelativeLayout register_done;

    private User user;

    private SharedPreferences pref;
    /**
     * 数据存储编辑
     */
    private SharedPreferences.Editor editor;

    private Handler RegisterHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    RegisterToServer();
                    break;
            }

        }
    };

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_fifth,null);
        initControl();
        return view;
    }

    private void initControl(){
        register_edit_password = (EditText) view.findViewById(R.id.register_edit_password);
        register_done = (RelativeLayout) view.findViewById(R.id.register_done);
        register_done.setEnabled(false);
    }

    private void buttonListener(){
        register_edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!register_edit_password.getText().toString().trim().equals("")&&register_edit_password.getText().toString().length()>=6){
                    register_done.setBackgroundResource(R.color.colorTitle);
                    register_done.setEnabled(true);
                }else{
                    register_done.setBackgroundResource(R.color.colorLine);
                    register_done.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        register_done.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        register_done.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        register_done.setBackgroundResource(R.color.colorTitle);
                        RegisterHandler.sendEmptyMessage(1);
                        break;
                }
                return true;
            }
        });
    }

    private void RegisterToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl+"/RegisterServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(!s.equals("false")){
                    Type type = new TypeToken<User>(){}.getType();
                    user = GsonUtil.jsonToBean(s, type);
                    BallApplication.setUser(user);
                    saveUserToSharePref(user);

                    Intent intent = new Intent();
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(),"注册失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String ,String> map = new HashMap<>();
                map.put("phone",getArguments().getString("phone"));
                map.put("password", MD5.GetMD5Code(register_edit_password.getText().toString()));
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }
    private void saveUserToSharePref(User user){
        pref = getActivity().getSharedPreferences("base64", getActivity().MODE_PRIVATE);
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
