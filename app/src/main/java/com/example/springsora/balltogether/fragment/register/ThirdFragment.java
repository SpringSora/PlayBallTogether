package com.example.springsora.balltogether.fragment.register;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.playballtogether.RegisterActivity;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by JJBOOM on 2016/4/21.
 */
public class ThirdFragment extends BaseFragment {

    private View view;

    /**
     * 电话号码编辑框
     */
    private EditText resgister_edit_phone;
    /**
     * 发送验证码按钮
     */
    private RelativeLayout resgiter_send_code;

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    private FourthFragment  fourthFragment;


    private Handler checkPhoneHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MotionEvent.ACTION_UP:
                    ((RegisterActivity)getActivity()).getProgressDialog().show();
                    isPhoneExist();
                    break;
            }
        }
    };

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_third,null);
        initControl();
        return view;
    }

    private void initControl(){
        resgister_edit_phone = (EditText) view.findViewById(R.id.register_edit_phone);
        resgiter_send_code = (RelativeLayout) view.findViewById(R.id.register_send_code);
        fourthFragment = new FourthFragment();
        resgiter_send_code.setEnabled(false);
    }

    private void buttonListener(){

        resgister_edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = resgister_edit_phone.getText().toString();
                if (Pattern.matches(REGEX_MOBILE, phone)) {
                    resgiter_send_code.setBackgroundResource(R.color.colorTitle);
                    resgiter_send_code.setEnabled(true);
                } else {
                    resgiter_send_code.setBackgroundResource(R.color.colorLine);
                    resgiter_send_code.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resgiter_send_code.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        resgiter_send_code.setBackgroundResource(R.color.colorTitleDeep);
                        break;
                    case MotionEvent.ACTION_UP:
                        resgiter_send_code.setBackgroundResource(R.color.colorTitle);
                        checkPhoneHandler.sendEmptyMessage(MotionEvent.ACTION_UP);
                        break;
                }
                return true;
            }
        });
    }

    private void isPhoneExist(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/checkUserServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (Boolean.valueOf(s)){
                    BallApplication.setPhonenum(resgister_edit_phone.getText().toString());
                    //SMSSDK.getVerificationCode(BallApplication.ChinaCode, resgister_edit_phone.getText().toString());
                    ((RegisterActivity)getActivity()).getProgressDialog().dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", resgister_edit_phone.getText().toString());
                    fourthFragment.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction().replace(R.id.register_frameLayout, fourthFragment, "FourthFragment")
                            .commit();

                }else{
                    Toast.makeText(getActivity(), "此电话号码已经被注册", Toast.LENGTH_SHORT).show();
                    ((RegisterActivity)getActivity()).getProgressDialog().dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_SHORT).show();
                ((RegisterActivity)getActivity()).getProgressDialog().dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("phone",resgister_edit_phone.getText().toString());
                return  map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse arg0) {
                // TODO Auto-generated method stub
                String str = null;
                try {
                    str = new String(arg0.data,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str, HttpHeaderParser.parseCacheHeaders(arg0));
            }
        };
        request.setTag("isPhoneExist()");
        BallApplication.getQueues().add(request);
    }
}
