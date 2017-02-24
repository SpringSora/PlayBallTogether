package com.example.springsora.balltogether.playballtogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.login.SecondFragment;


/**
 * Created by JJBOOM on 2016/4/21.
 */
public class LoginActivity extends AppCompatActivity {

    private SecondFragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);
        secondFragment = new SecondFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.login_frameLayout,secondFragment,"Second")
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
