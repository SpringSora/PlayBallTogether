package com.example.springsora.balltogether.playballtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.dateball.AcceptFragment;

/**
 * Created by Springsora on 2016/5/11.
 */
public class AcceptActivity extends AppCompatActivity {
    private AcceptFragment acceptFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        acceptFragment = new AcceptFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,acceptFragment,"acceptFragment")
                .commit();
    }
}
