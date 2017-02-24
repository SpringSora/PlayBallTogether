package com.example.springsora.balltogether.playballtogether;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.SDKInitializer;
import com.example.springsora.balltogether.MD5.MD5;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.bean.BallGround;
import com.example.springsora.balltogether.bean.User;
import com.example.springsora.balltogether.fragment.main.FirstFragment;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private FirstFragment firstFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         firstFragment = new FirstFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,firstFragment,"First")
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity", "onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        BallApplication.getQueues().cancelAll(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MainActivity", "onActivityResult" + requestCode);
        switch (requestCode){
            case 1:
                firstFragment.UpdatePager(0);
                if(resultCode == RESULT_OK){
                        firstFragment.getFirstPager()
                                .getFirstRefreshLoadLayout()
                                .setRefreshing(true);
                        firstFragment.getFirstPager()
                                .PublishAfterRefresh();
                        firstFragment.UpdatePager(0);
                        BallApplication.setIsPublish(false);
                }
                break;
            case 2:
                        firstFragment.UpdatePager(1);
                break;
            case 4:
                        firstFragment.UpdatePager(3);
                break;
            case 5:

                break;
        }
    }
}
