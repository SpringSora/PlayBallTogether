package com.example.springsora.balltogether.fragment.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.base.BasePager;
import com.example.springsora.balltogether.bean.User;
import com.example.springsora.balltogether.custom_adapter.LoginViewPagerAdapter;
import com.example.springsora.balltogether.custom_viewpager.MyViewPager;
import com.example.springsora.balltogether.pager.login.Login_FirstPager;
import com.example.springsora.balltogether.playballtogether.RegisterActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JJBOOM on 2016/4/21.
 */
public class SecondFragment extends BaseFragment {
    /**
     * 加载的页面
     */
    private View view;

    /**
     * 登录页面的ViewPager
     */
    private MyViewPager LoginViewPager;
    /**
     * 页面集合
     */
    private List<BasePager> pagers;

    /**
     *  登陆界面
     */
    private Login_FirstPager login_firstPager;

    /**
     * 注册按钮
     */
    private LinearLayout login_right_layout;

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
        LoginViewPager.setAdapter(new LoginViewPagerAdapter(pagers));
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_second,null);
        initControl();
        initList();
        return view;
    }

    private void initControl(){
        LoginViewPager = (MyViewPager) view.findViewById(R.id.login_list_view);
        login_right_layout = (LinearLayout) view.findViewById(R.id.login_right_layout);
    }
    private void initList(){
        pagers = new ArrayList<>();
        login_firstPager = new Login_FirstPager(getActivity());
        pagers.add(login_firstPager);
    }

    private void buttonListener(){
        login_right_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BallApplication.setIsRegister(true);
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == getActivity().RESULT_OK){
                    User user = (User) data.getSerializableExtra("UserData");
                    Log.i("SecondFragment",user.getU_id()+"");
                    saveUserToSharePref(user);
                    Intent intent = new Intent();
                    intent.putExtra("User","OK");
                    getActivity().setResult(getActivity().RESULT_OK,intent);
                    getActivity().finish();
                }
                break;
        }
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
