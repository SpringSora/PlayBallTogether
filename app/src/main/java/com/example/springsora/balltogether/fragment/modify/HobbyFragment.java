package com.example.springsora.balltogether.fragment.modify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
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
public class HobbyFragment extends BaseFragment {

    private View view;

    private CheckBox modify_hobby_football;
    private CheckBox modify_hobby_basketball;
    private CheckBox modify_pingpong;
    private CheckBox modify_hobby_tennis;
    private CheckBox modify_hobby_badminton;
    private CheckBox modify_hobby_billiards;
    private CheckBox modify_hobby_volleyball;
    private RelativeLayout modify_hobby_button;
    private ImageView modify_hobby_back;

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
        view = inflater.inflate(R.layout.modify_hobby,null);
        modify_hobby_football = (CheckBox) view.findViewById(R.id.modify_hobby_football);
        modify_hobby_basketball = (CheckBox) view.findViewById(R.id.modify_hobby_basketball);
        modify_pingpong = (CheckBox) view.findViewById(R.id.modify_pingpong);
        modify_hobby_tennis = (CheckBox) view.findViewById(R.id.modify_hobby_tennis);
        modify_hobby_badminton = (CheckBox) view.findViewById(R.id.modify_hobby_badminton);
        modify_hobby_billiards = (CheckBox) view.findViewById(R.id.modify_hobby_billiards);
        modify_hobby_volleyball = (CheckBox) view.findViewById(R.id.modify_hobby_volleyball);
        modify_hobby_button = (RelativeLayout) view.findViewById(R.id.modify_hobby_button);
        modify_hobby_back = (ImageView) view.findViewById(R.id.modify_hobby_back);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        if(BallApplication.getUser().getU_playtype()!=null&&!BallApplication.getUser().getU_playtype().trim().equals("")){
            char[] ball = BallApplication.getUser().getU_playtype().toCharArray();
            for(int i = 0;i<ball.length;i++){
                if("1".equals(ball[i])){
                    switch (i){
                        case 0:
                            modify_hobby_football.setChecked(true);
                            break;
                        case 1:
                            modify_hobby_basketball.setChecked(true);
                            break;
                        case 2:
                            modify_pingpong.setChecked(true);
                            break;
                        case 3:
                            modify_hobby_billiards.setChecked(true);
                            break;
                        case 4:
                            modify_hobby_badminton.setChecked(true);
                            break;
                        case 5:
                            modify_hobby_tennis.setChecked(true);
                            break;
                        case 6:
                            modify_hobby_volleyball.setChecked(true);
                            break;
                    }
                }
            }
        }
        return view;
    }

    private void buttonListener(){
        modify_hobby_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        modify_hobby_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        modify_hobby_button.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_hobby_button.setBackgroundResource(R.color.colorTitle);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_hobby_button.setBackgroundResource(R.color.colorTitle);
                        progressDialog.show();
                        ModifyHobbyToServer();
                        break;
                }
                return true;
            }
        });
    }

    private String CheckedNum(){
        char[] ball = new char[7];

        if(modify_hobby_football.isChecked()){
            ball[0]='1';
        }else{
            ball[0]='0';
        }

        if(modify_hobby_basketball.isChecked()){
            ball[1]='1';
        }else{
            ball[1]='0';
        }
        if(modify_pingpong.isChecked()){
            ball[2]='1';
        }else{
            ball[2]='0';
        }
        if(modify_hobby_billiards.isChecked()){
            ball[3]='1';
        }else{
            ball[3]='0';
        }
        if(modify_hobby_badminton.isChecked()){
            ball[4]='1';
        }else{
            ball[4]='0';
        }
        if(modify_hobby_tennis.isChecked()){
            ball[5]='1';
        }else{
            ball[5]='0';
        }
        if(modify_hobby_volleyball.isChecked()){
            ball[6]='1';
        }else{
            ball[6]='0';
        }
        return new String(ball);
    }

    private void ModifyHobbyToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/ModifyServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                if(Boolean.valueOf(s)){
                    BallApplication.getUser().setU_playtype(CheckedNum());
                    saveUserToSharePref(BallApplication.getUser());
                    Intent intent = new Intent();
                    getActivity().setResult(getActivity().RESULT_OK,intent);
                    getActivity().finish();
                }else{
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
                map.put("Modify_type",BallApplication.Modify_Hobby_Tag+"");
                map.put(""+BallApplication.Modify_Hobby_Tag,CheckedNum());
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
