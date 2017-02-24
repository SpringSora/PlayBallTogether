package com.example.springsora.balltogether.pager.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
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
import com.example.springsora.balltogether.MD5.MD5;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BasePager;
import com.example.springsora.balltogether.bean.User;
import com.example.springsora.balltogether.playballtogether.LoginActivity;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by JJBOOM on 2016/4/21.
 */
public class Login_FirstPager extends BasePager {
    public Login_FirstPager(Context context) {
        super(context);
    }
    private View view;
    private EditText login_user;
    private EditText login_pw;
    private RelativeLayout login_button;
    public static  final  String TAG = "Login_FirstPager";
    private AlertDialog.Builder builder;
    /**
     * 数据存储
     */
    private SharedPreferences pref;
    /**
     * 数据存储编辑
     */
    private SharedPreferences.Editor editor;

    /**
     * 进度条
     */
    private ProgressDialog progressDialog;


    private Handler LoginHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case MotionEvent.ACTION_DOWN:
                        login_button.setBackgroundResource(R.color.colorTitleDeep);
                    break;
                case MotionEvent.ACTION_UP:
                    login_button.setBackgroundResource(R.color.colorTitle);
                    if(null==login_user.getText().toString()||"".equals(login_user.getText().toString())){
                        Toast.makeText(context,"请输入用户名",Toast.LENGTH_SHORT).show();
                        login_button.setEnabled(true);
                    }else{
                        if(null==login_pw.getText().toString()||"".equals(login_pw.getText().toString())){
                            Toast.makeText(context,"请输入密码",Toast.LENGTH_SHORT).show();
                            login_button.setEnabled(true);
                        }else{
                            progressDialog.show();
                            LoginToServer();
                        }
                    }
                    break;
            }
        };
    };

    @Override
    public View initView() {
        view = LayoutInflater.from(context).inflate(R.layout.login_pager_first,null);
        initControl();
        initData();
        return view;
    }

    @Override
    public void initData() {
        buttonListener();
    }

    private void initControl(){
        login_user = (EditText) view.findViewById(R.id.login_user);
        login_pw = (EditText) view.findViewById(R.id.login_pw);
        login_button = (RelativeLayout) view.findViewById(R.id.login_button);
        builder = new AlertDialog.Builder(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        builder.setTitle("提示");
        builder.setMessage("您输入的账号或密码错误");
        builder.setCancelable(false);
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                login_button.setEnabled(true);
            }
        });
    }

    private void buttonListener(){
        login_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LoginHandler.sendEmptyMessage(MotionEvent.ACTION_DOWN);
                        break;
                    case MotionEvent.ACTION_UP:
                        LoginHandler.sendEmptyMessage(MotionEvent.ACTION_UP);
                        login_button.setEnabled(false);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 验证用户
     */
    private void LoginToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl+"/isUserLoginServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                login_button.setEnabled(true);
                if(s.equals("false")){
                    builder.show();
                }else{
                    Type type = new TypeToken<User>(){}.getType();
                    User user = GsonUtil.jsonToBean(s, type);
                    saveUserToSharePref(user);
                    BallApplication.setUser(user);
                    Intent intent = new Intent();
                    intent.putExtra("User","Ok");
                    ((LoginActivity) context).setResult(((LoginActivity) context).RESULT_OK, intent);
                    ((LoginActivity) context).finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                login_button.setEnabled(true);
                Log.d(TAG, "Volley returned error________________:" + volleyError);
                Class klass = volleyError.getClass();
                if(klass == AuthFailureError.class) {
                    Log.d(TAG,"AuthFailureError");
                    Toast.makeText(context,"未授权，请重新登录",Toast.LENGTH_LONG).show();
                } else if(klass == com.android.volley.NetworkError.class) {
                    Log.d(TAG,"NetworkError");
                    Toast.makeText(context,"网络连接错误，请重新登录",Toast.LENGTH_LONG).show();
                } else if(klass == com.android.volley.NoConnectionError.class) {
                    Log.d(TAG,"NoConnectionError");
                } else if(klass == com.android.volley.ServerError.class) {
                    Log.d(TAG,"ServerError");
                    Toast.makeText(context,"服务器未知错误，请重新登录",Toast.LENGTH_LONG).show();
                } else if(klass == com.android.volley.TimeoutError.class) {
                    Log.d(TAG,"TimeoutError");
                    Toast.makeText(context,"连接超时，请重新登录",Toast.LENGTH_LONG).show();
                } else if(klass == com.android.volley.ParseError.class) {
                    Log.d(TAG,"ParseError");
                } else if(klass == VolleyError.class) {
                    Log.d(TAG,"General error");
                }
                Toast.makeText(context,"登录失败",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> map = new HashMap<>();
                map.put("username",login_user.getText().toString());
                map.put("password", MD5.GetMD5Code(login_pw.getText().toString()));
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
        request.setTag("LoginToServer");
        BallApplication.getQueues().add(request);
    }

    private void saveUserToSharePref(User user){
        pref = context.getSharedPreferences("base64", context.MODE_PRIVATE);
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
