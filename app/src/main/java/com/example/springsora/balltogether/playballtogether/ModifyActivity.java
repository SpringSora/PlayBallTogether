package com.example.springsora.balltogether.playballtogether;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.modify.AgeFragment;
import com.example.springsora.balltogether.fragment.modify.ConnectFragment;
import com.example.springsora.balltogether.fragment.modify.HobbyFragment;
import com.example.springsora.balltogether.fragment.modify.NicknameFragment;
import com.example.springsora.balltogether.fragment.modify.PasswordFragment;
import com.example.springsora.balltogether.fragment.modify.PhoneFragment;
import com.example.springsora.balltogether.fragment.modify.SexFragment;
import com.example.springsora.balltogether.fragment.modify.SignFragment;


import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by JJBOOM on 2016/4/23.
 */
public class ModifyActivity extends AppCompatActivity{

    private EventHandler eh;

    private ProgressDialog progressDialog;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        Intent intent = getIntent();


        switch (intent.getIntExtra("Control", 0)){
            case R.id.modify_nickname:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout,new NicknameFragment(),"NickName")
                        .commit();
                break;
            case R.id.modify_phone:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout,new PhoneFragment(),"Phone")
                        .commit();
                break;
            case R.id.modify_pw:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout,new PasswordFragment(),"Password")
                        .commit();
                break;
            case R.id.modify_sex:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout,new SexFragment(),"Sex")
                        .commit();
                break;
            case R.id.modify_age:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout,new AgeFragment(),"Age")
                        .commit();
                break;
            case R.id.modify_connect:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout,new ConnectFragment(),"Password")
                        .commit();
                break;
            case R.id.user_info_signature:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout,new SignFragment(),"Sign")
                        .commit();
                break;
            case R.id.modify_hobby:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout,new HobbyFragment(),"Hobby")
                        .commit();
                break;
            default:
                break;
        }
    }

    /**
     * SMSSDK短信验证初始化
     */
    private void initSMSSDK(){
        SMSSDK.initSDK(ModifyActivity.this, "11f3e117bdf9e", "eb3cb60cee3c3b95a9c80b8dc7030532");
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if(result==SMSSDK.RESULT_COMPLETE){
                    //回调完成
                    if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        //提交验证码成功
                        progressDialog.dismiss();
                        Log.i("Code", "submit success");
                    }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        progressDialog.dismiss();
                        num++;
                        if(num==1){

                        }
                        //获取验证码成功
                    }else if(event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证国家
                    }else{
                        ((Throwable)data).printStackTrace();
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    protected void onStart() {
        initSMSSDK();
        super.onStart();
    }

    @Override
    protected void onStop() {
        SMSSDK.unregisterEventHandler(eh);
        super.onStop();
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }
}
