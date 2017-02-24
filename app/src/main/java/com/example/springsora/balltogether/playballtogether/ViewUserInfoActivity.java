package com.example.springsora.balltogether.playballtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.userinfo.ViewUserInfoFragment;

/**
 * Created by JJBOOM on 2016/5/5.
 */
public class ViewUserInfoActivity extends AppCompatActivity {
    private ViewUserInfoFragment viewUserInfoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewUserInfoFragment = new ViewUserInfoFragment();
        viewUserInfoFragment.setArguments(getIntent().getExtras());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,viewUserInfoFragment,"viewUserInfoFragment")
                .commit();
    }
}
