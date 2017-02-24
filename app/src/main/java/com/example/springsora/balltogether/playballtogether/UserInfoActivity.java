package com.example.springsora.balltogether.playballtogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.userinfo.UserInfoFragment;


/**
 * Created by JJBOOM on 2016/4/22.
 */
public class UserInfoActivity extends AppCompatActivity {
    private UserInfoFragment userInfoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInfoFragment = new UserInfoFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,userInfoFragment,"user_info")
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("UserInfoActivity","onResult");
    }
}
