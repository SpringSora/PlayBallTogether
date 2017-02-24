package com.example.springsora.balltogether.fragment.modify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
public class AgeFragment extends BaseFragment {


    private View view;

    private ImageView modify_age_back;

    private Spinner modify_age_spinner;

    private RelativeLayout modify_age_button;

    private int age;
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
        view = inflater.inflate(R.layout.modify_age,null);
        initControl();
        return view;
    }

    private void initControl(){
        modify_age_back = (ImageView) view.findViewById(R.id.modify_age_back);
        modify_age_spinner = (Spinner) view.findViewById(R.id.modify_age_spinner);
        modify_age_button = (RelativeLayout) view.findViewById(R.id.modify_age_button);

        if(BallApplication.getUser().getU_age()!=null&&BallApplication.getUser().getU_age()!=0){
            modify_age_spinner.setSelection(BallApplication.getUser().getU_age()-1);
        }
    }

    private void buttonListener (){
        modify_age_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        modify_age_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age = position + 1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modify_age_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        modify_age_button.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_age_button.setBackgroundResource(R.color.colorTitle);
                        modify_age_button.setEnabled(false);
                        ModifyAgeToServer();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_age_button.setBackgroundResource(R.color.colorTitle);
                        break;
                }
                return true;
            }
        });
    }
    private void ModifyAgeToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/ModifyServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(Boolean.valueOf(s)){
                    BallApplication.getUser().setU_age(age);
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
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                modify_age_button.setEnabled(true);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Modify_type",BallApplication.Modify_Age_Tag+"");
                map.put(BallApplication.Modify_Age_Tag+"",age+"");
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
