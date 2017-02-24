package com.example.springsora.balltogether.playballtogether;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.fragment.register.FifthFragment;
import com.example.springsora.balltogether.fragment.register.FourthFragment;
import com.example.springsora.balltogether.fragment.register.ThirdFragment;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by JJBOOM on 2016/4/22.
 */
public class RegisterActivity extends AppCompatActivity {

    private EventHandler eh;
    private ThirdFragment thirdFragment;
    private FifthFragment fifthFragment;
    private FourthFragment fourthFragment;
    private ProgressDialog progressDialog;
    private int RequestNum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_main);
        thirdFragment = new ThirdFragment();
        fifthFragment = new FifthFragment();
        fourthFragment = new FourthFragment();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.register_frameLayout,thirdFragment,"Third")
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initSMSSDK();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SMSSDK.unregisterEventHandler(eh);
    }

    /**
     * SMSSDK短信验证初始化
     */
    private void initSMSSDK(){
        SMSSDK.initSDK(RegisterActivity.this, "11f3e117bdf9e", "eb3cb60cee3c3b95a9c80b8dc7030532");
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if(result==SMSSDK.RESULT_COMPLETE){
                    //回调完成
                    if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        //提交验证码成功
                        progressDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("phone", BallApplication.getPhonenum());
                        fifthFragment.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.register_frameLayout, fifthFragment, "Fifth")
                                .commit();
                        Log.i("Code","submit success");
                    }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        progressDialog.dismiss();
                        RequestNum++;
                        if(RequestNum>1){

                        }else{
                            Bundle bundle = new Bundle();
                            bundle.putString("phone",BallApplication.getPhonenum());
                            fourthFragment.setArguments(bundle);
                            getFragmentManager()
                                    .beginTransaction().replace(R.id.register_frameLayout, fourthFragment, "FourthFragment")
                                    .commit();
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

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }
}
