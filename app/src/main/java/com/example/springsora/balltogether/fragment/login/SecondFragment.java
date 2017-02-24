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
     * ���ص�ҳ��
     */
    private View view;

    /**
     * ��¼ҳ���ViewPager
     */
    private MyViewPager LoginViewPager;
    /**
     * ҳ�漯��
     */
    private List<BasePager> pagers;

    /**
     *  ��½����
     */
    private Login_FirstPager login_firstPager;

    /**
     * ע�ᰴť
     */
    private LinearLayout login_right_layout;

    /**
     * ���ݴ洢
     */
    private SharedPreferences pref;
    /**
     * ���ݴ洢�༭
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
        //�����ֽ������
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            //�������������������װ�ֽ���
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            //������д���ֽ���
            oos.writeObject(user);
            //���ֽ��������base64�ַ���
            String UserBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor = pref.edit();
            editor.putString("User",UserBase64);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
