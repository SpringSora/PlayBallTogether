package com.example.springsora.balltogether.fragment.modify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.springsora.balltogether.MD5.MD5;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JJBOOM on 2016/4/23.
 */
public class PasswordFragment extends BaseFragment {

    private View view;

    private ImageView modify_password_back;

    private EditText current_password;

    private EditText new_password;

    private EditText confirm_password;

    private RelativeLayout modify_password_button;

    private ProgressDialog dialog;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.modify_password,null);
        initControl();
        return view;
    }

    private void initControl(){
        modify_password_back = (ImageView) view.findViewById(R.id.modify_password_back);
        current_password = (EditText) view.findViewById(R.id.current_password);
        new_password = (EditText) view.findViewById(R.id.new_password);
        confirm_password = (EditText) view.findViewById(R.id.confirm_password);
        modify_password_button = (RelativeLayout) view.findViewById(R.id.modify_password_button);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
    }

    private void buttonListener(){
        modify_password_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        modify_password_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_password_button.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_password_button.setBackgroundResource(R.color.colorTitle);
                        CheckPassword();
                        modify_password_button.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_password_button.setBackgroundResource(R.color.colorTitle);
                }
                return true;
            }
        });
    }

    private void CheckPassword(){
        if(current_password.getText().toString()!=null&&!current_password.getText().toString().trim().equals("")){
            if(new_password.getText().toString()!=null&&!new_password.getText().toString().trim().equals("")){
                    if(new_password.getText().toString().length()>=6&&new_password.getText().toString().length()<=32){
                        if(confirm_password.getText().toString()!=null&&!confirm_password.getText().toString().trim().equals("")){
                            if(confirm_password.getText().toString().equals(new_password.getText().toString())){
                                dialog.show();
                                ModifyPasswordToServer();
                            }else{
                                Toast.makeText(getActivity(),"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                                modify_password_button.setEnabled(true);
                            }
                        }else{
                            Toast.makeText(getActivity(),"确认密码不能为空，请输入确认密码",Toast.LENGTH_SHORT).show();
                            modify_password_button.setEnabled(true);
                        }
                    }else{
                        Toast.makeText(getActivity(),"输入的新密码不规范",Toast.LENGTH_SHORT).show();
                        modify_password_button.setEnabled(true);
                    }
            }else{
                Toast.makeText(getActivity(),"新密码不能为空，请输入新密码",Toast.LENGTH_SHORT).show();
                modify_password_button.setEnabled(true);
            }
        }else{
            Toast.makeText(getActivity(),"当前密码不能为空，请输入当前密码",Toast.LENGTH_SHORT).show();
            modify_password_button.setEnabled(true);
        }
    }
    private void ModifyPasswordToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl+"/ModifyServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(Boolean.valueOf(s)){
                    dialog.dismiss();
                    modify_password_button.setEnabled(true);
                    Intent intent = new Intent();
                    getActivity().setResult(getActivity().RESULT_OK,intent);
                    getActivity().finish();
                }else {
                    modify_password_button.setEnabled(true);
                    dialog.dismiss();
                    Toast.makeText(getActivity(),"当前密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                modify_password_button.setEnabled(true);
                dialog.dismiss();
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Modify_type",BallApplication.Modify_Password_Tag+"");
                map.put(BallApplication.Modify_Password_Tag+"", MD5.GetMD5Code(confirm_password.getText().toString()));
                map.put("u_id",BallApplication.getUser().getU_id()+"");
                map.put("OldPassword",MD5.GetMD5Code(current_password.getText().toString()));
                return  map;
            }
        };
        BallApplication.getQueues().add(request);
    }
}
