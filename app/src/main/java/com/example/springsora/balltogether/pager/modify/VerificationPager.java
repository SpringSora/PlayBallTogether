package com.example.springsora.balltogether.pager.modify;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BasePager;
import com.example.springsora.balltogether.custom_viewpager.MyViewPager;

import java.util.regex.Pattern;

/**
 * Created by JJBOOM on 2016/4/23.
 */
public class VerificationPager extends BasePager {

    private View view;

    private TextView phone_hint_text;

    private EditText phone_verification_edit;

    private RelativeLayout modify_phone_verification_button;

    private ProgressDialog progressDialog;

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    private MyViewPager myViewPager;

    public VerificationPager(Context context,MyViewPager myViewPager) {
        super(context);
        this.myViewPager = myViewPager;
    }

    @Override
    public View initView() {
        view = LayoutInflater.from(context).inflate(R.layout.modify_phone_pager1,null);
        initControl();
        initData();
        return view;
    }

    @Override
    public void initData() {
        buttonListener();
    }

    private void initControl(){
        phone_hint_text = (TextView) view.findViewById(R.id.phone_hint_text);
        char[] hintPhone = BallApplication.getUser().getU_phone().toCharArray();
        hintPhone[3] = '*';hintPhone[4] = '*';hintPhone[5] = '*';hintPhone[6] = '*';
        phone_hint_text.setText(new String(hintPhone));
        phone_verification_edit = (EditText) view.findViewById(R.id.phone_verification_edit);
        modify_phone_verification_button = (RelativeLayout) view.findViewById(R.id.modify_phone_verification_button);
        modify_phone_verification_button.setEnabled(false);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    private void buttonListener(){

        phone_verification_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = phone_verification_edit.getText().toString();
                if (Pattern.matches(REGEX_MOBILE, phone)) {
                    modify_phone_verification_button.setBackgroundResource(R.color.colorTitle);
                    modify_phone_verification_button.setEnabled(true);
                } else {
                    modify_phone_verification_button.setBackgroundResource(R.color.colorLine);
                    modify_phone_verification_button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        modify_phone_verification_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_phone_verification_button.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_phone_verification_button.setBackgroundResource(R.color.colorTitle);
                        break;
                    case MotionEvent.ACTION_UP:
                        progressDialog.show();
                        TestPhone();
                        modify_phone_verification_button.setBackgroundResource(R.color.colorTitle);
                        modify_phone_verification_button.setEnabled(false);
                }
                return true;
            }
        });
    }

    private void TestPhone(){
        if(phone_verification_edit.getText().toString().equals(BallApplication.getUser().getU_phone())){
            progressDialog.dismiss();
            myViewPager.setCurrentItem(1);
            modify_phone_verification_button.setEnabled(true);
        }else{
            progressDialog.dismiss();
            Toast.makeText(context,"号码错误",Toast.LENGTH_SHORT).show();
            modify_phone_verification_button.setEnabled(true);
        }
    }
}
