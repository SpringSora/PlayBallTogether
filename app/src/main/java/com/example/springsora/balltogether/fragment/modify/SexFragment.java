package com.example.springsora.balltogether.fragment.modify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.User;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JJBOOM on 2016/4/23.
 */
public class SexFragment extends BaseFragment {

    View view;

    private ImageView modify_sex_back;

    private RelativeLayout modify_man;

    private RelativeLayout modify_woman;

    private ImageView modify_sex_man;

    private ImageView modify_sex_woman;

    private String sex;

    /**
     * 数据存储
     */
    private SharedPreferences pref;
    /**
     * 数据存储编辑
     */
    private SharedPreferences.Editor editor;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.modify_sex,null);
        initControl();
        return view;
    }

    private void initControl(){
        modify_sex_back = (ImageView) view.findViewById(R.id.modify_sex_back);
        modify_man = (RelativeLayout) view.findViewById(R.id.modify_man);
        modify_woman = (RelativeLayout) view.findViewById(R.id.modify_woman);
        modify_sex_man = (ImageView) view.findViewById(R.id.modify_sex_man);
        modify_sex_woman = (ImageView) view.findViewById(R.id.modify_sex_woman);
        if(BallApplication.getUser().getU_sex()!=null&&"1".equals(BallApplication.getUser().getU_sex())){
            modify_sex_man.setVisibility(View.VISIBLE);
        }else if(BallApplication.getUser().getU_sex()!=null&&"0".equals(BallApplication.getUser().getU_sex())){
            modify_sex_woman.setVisibility(View.VISIBLE);
        }
    }

    private void buttonListener(){

        modify_sex_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        modify_man.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_man.setBackgroundResource(R.color.colorBody);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_man.setBackgroundResource(R.color.colorWhite);
                        sex = "1";
                        ModifySexToServer();
                        modify_sex_man.setVisibility(View.VISIBLE);
                        modify_sex_woman.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_man.setBackgroundResource(R.color.colorWhite);
                        break;
                }
                return true;
            }
        });

        modify_woman.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_woman.setBackgroundResource(R.color.colorBody);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_woman.setBackgroundResource(R.color.colorWhite);
                        sex = "0";
                        ModifySexToServer();
                        modify_sex_man.setVisibility(View.INVISIBLE);
                        modify_sex_woman.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_woman.setBackgroundResource(R.color.colorWhite);
                        break;
                }
                return true;
            }
        });
    }
    private void  ModifySexToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/ModifyServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(Boolean.valueOf(s)){
                    BallApplication.getUser().setU_sex(sex);
                    saveUserToSharePref(BallApplication.getUser());
                    Intent intent = new Intent();
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(),"修改失败",Toast.LENGTH_SHORT).show();
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
                map.put("Modify_type",BallApplication.Modify_Sex_Tag+"");
                map.put(BallApplication.Modify_Sex_Tag+"",sex);
                map.put("u_id",BallApplication.getUser().getU_id()+"");
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }
    private void saveUserToSharePref(User user){
        pref = getActivity().getSharedPreferences("base64",getActivity().MODE_PRIVATE);
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
