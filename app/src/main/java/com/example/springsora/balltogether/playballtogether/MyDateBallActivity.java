package com.example.springsora.balltogether.playballtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.dateball.MyDateBallFragment;

/**
 * Created by Springsora on 2016/5/10.
 */
public class MyDateBallActivity extends AppCompatActivity {
    private MyDateBallFragment myDateBallFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDateBallFragment = new MyDateBallFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,myDateBallFragment,"myDateBallFragment")
                .commit();

    }
}
