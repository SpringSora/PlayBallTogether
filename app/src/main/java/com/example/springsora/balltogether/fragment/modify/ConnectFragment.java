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
 * Created by JJBOOM on 2016/4/23.
 */
public class ConnectFragment extends BaseFragment {

    private View view;

    private ImageView modify_connect_back;

    private EditText modify_connect_edit;

    private RelativeLayout modify_connect_button;

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
        view = inflater.inflate(R.layout.modify_connect,null);
        initControl();
        return view;
    }

    private void initControl(){
        modify_connect_back = (ImageView) view.findViewById(R.id.modify_connect_back);
        modify_connect_edit = (EditText) view.findViewById(R.id.modify_connect_edit);
        modify_connect_button = (RelativeLayout) view.findViewById(R.id.modify_connect_button);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        if(BallApplication.getUser().getU_name()!=null){
            modify_connect_edit.setText(BallApplication.getUser().getU_name());
        }
        modify_connect_edit.setFocusable(true);
        modify_connect_edit.setFocusableInTouchMode(true);
        modify_connect_edit.requestFocus();
    }

    private void buttonListener(){
        modify_connect_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        modify_connect_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_connect_button.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_connect_button.setBackgroundResource(R.color.colorTitle);
                        progressDialog.show();
                        ModifyConnectToServer();
                        modify_connect_button.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_connect_button.setBackgroundResource(R.color.colorTitle);
                        break;
                }
                return true;
            }
        });
    }

    private void ModifyConnectToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/ModifyServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(Boolean.valueOf(s)){
                    BallApplication.getUser().setU_name(modify_connect_edit.getText().toString());
                    saveUserToSharePref(BallApplication.getUser());
                    progressDialog.dismiss();
                    Intent intent = new Intent();
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"修改失败",Toast.LENGTH_SHORT).show();
                }
                modify_connect_button.setEnabled(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                modify_connect_button.setEnabled(false);
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Modify_type",BallApplication.Modify_Connect_Tag+"");
                map.put(BallApplication.Modify_Connect_Tag+"",modify_connect_edit.getText().toString());
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
