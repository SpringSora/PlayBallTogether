package com.example.springsora.balltogether.fragment.modify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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
 * Created by JJBOOM on 2016/4/24.
 */
public class SignFragment extends BaseFragment {

    private View view;

    private ImageView modify_sign_back;

    private EditText modify_sign_edit;

    private RelativeLayout modify_sign_button;

    private ProgressDialog progressDialog;

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
        view = inflater.inflate(R.layout.modify_sign,null);
        initControl();
        return view;
    }

    private void initControl(){
        modify_sign_back = (ImageView) view.findViewById(R.id.modify_sign_back);
        modify_sign_edit = (EditText) view.findViewById(R.id.modify_sign_edit);
        modify_sign_button = (RelativeLayout) view.findViewById(R.id.modify_sign_button);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        if(BallApplication.getUser().getSignature()!=null){
            modify_sign_edit.setText(BallApplication.getUser().getSignature());
        }
    }

    private void buttonListener(){
        modify_sign_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        modify_sign_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_sign_button.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_sign_button.setBackgroundResource(R.color.colorTitle);
                        break;
                    case MotionEvent.ACTION_UP:
                        progressDialog.show();
                        ModifySignToServer();
                        modify_sign_button.setBackgroundResource(R.color.colorTitle);
                        break;
                }
                return true;
            }
        });
    }

    private void ModifySignToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/ModifyServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                if(Boolean.valueOf(s)){
                    BallApplication.getUser().setSignature(modify_sign_edit.getText().toString());
                    saveUserToSharePref(BallApplication.getUser());
                    Intent intent = new Intent();
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }else {
                    Toast.makeText(getActivity(),"修改失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Modify_type",BallApplication.Modify_Sign_Tag+"");
                map.put(BallApplication.Modify_Sign_Tag+"",modify_sign_edit.getText().toString());
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
