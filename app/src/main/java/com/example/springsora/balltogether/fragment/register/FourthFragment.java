package com.example.springsora.balltogether.fragment.register;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.playballtogether.RegisterActivity;

import java.util.concurrent.CountDownLatch;


/**
 * Created by JJBOOM on 2016/4/21.
 */
public class FourthFragment extends BaseFragment {

    private View view;
    /**
     * 验证码输入框
     */
    private EditText register_edit_code;
    /**
     * 重发按钮
     */
    private RelativeLayout register_countdown;
    /**
     * 倒计时
     */
    private TextView countdown;
    /**
     * 提交验证码
     */
    private RelativeLayout register_submit_code;
    private int recLen = 60;

    private Handler CountDownHandler;

    private FifthFragment fifthFragment;


    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_fourth,null);
        initControl();
        return view;
    }

    private void initControl(){
        register_edit_code = (EditText) view.findViewById(R.id.register_edit_code);
        register_countdown = (RelativeLayout) view.findViewById(R.id.register_countdown);
        countdown = (TextView) view.findViewById(R.id.countdown);
        register_submit_code = (RelativeLayout) view.findViewById(R.id.register_submit_code);
        register_submit_code.setEnabled(false);
        register_countdown.setEnabled(false);
        fifthFragment = new FifthFragment();
        CountDownHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        recLen--;
                        countdown.setText(""+recLen+"s");
                        if(recLen > 0){
                            Message message = new Message();
                            message.what = 1;
                            CountDownHandler.sendMessageDelayed(message,1000);
                        }else{
                            register_countdown.setBackgroundResource(R.color.colorTitle);
                            register_countdown.setEnabled(true);
                            countdown.setText("重新发送验证码");
                        }
                        break;
                }
            }
        };
    }

    private void buttonListener(){



        register_edit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!register_edit_code.getText().toString().trim().equals("")) {
                    register_submit_code.setEnabled(true);
                    register_submit_code.setBackgroundResource(R.color.colorTitle);
                } else {
                    register_submit_code.setEnabled(false);
                    register_submit_code.setBackgroundResource(R.color.colorLine);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        register_countdown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        register_countdown.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        ((RegisterActivity)getActivity()).getProgressDialog().show();
                        register_countdown.setBackgroundResource(R.color.colorTitle);
                        Log.i("fourth", getArguments().getString("phone"));
                        //SMSSDK.getVerificationCode(BallApplication.ChinaCode, getArguments().getString("phone"));
                        break;
                }
                return true;
            }
        });

        register_submit_code.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        register_submit_code.setBackgroundResource(R.color.colorTitleDeep);

                        break;
                    case MotionEvent.ACTION_UP:
                        register_submit_code.setBackgroundResource(R.color.colorTitle);
                        ((RegisterActivity)getActivity()).getProgressDialog().show();
                        BallApplication.phonenum = getArguments().getString("phone");
                        //SMSSDK.submitVerificationCode(BallApplication.ChinaCode,getArguments().getString("phone"),register_edit_code.getText().toString());

                        Bundle bundle = new Bundle();
                        bundle.putString("phone", BallApplication.phonenum);
                        fifthFragment.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.register_frameLayout, fifthFragment, "Fifth")
                                .commit();
                        ((RegisterActivity)getActivity()).getProgressDialog().dismiss();

                        break;
                }
                return true;
            }
        });
        CountDownHandler.sendEmptyMessage(1);
    }



}
