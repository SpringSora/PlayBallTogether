package com.example.springsora.balltogether.playballtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.ballground.BallGroundDetailFragment1;
import com.example.springsora.balltogether.fragment.ballground.BallGroundDetailFragment2;

/**
 * Created by JJBOOM on 2016/5/6.
 */
public class BallGroundDetailActivity extends AppCompatActivity {
    private BallGroundDetailFragment1 ballGroundDetailFragment1;
    private BallGroundDetailFragment2 ballGroundDetailFragment2;
    private Integer fragment_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment_type = getIntent().getIntExtra("fragment",0);
        switch (fragment_type){
            case 0:
                break;
            case 1:
                ballGroundDetailFragment1 = new BallGroundDetailFragment1();
                ballGroundDetailFragment1.setArguments(getIntent().getExtras());
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout,ballGroundDetailFragment1,"ballGroundFragment1")
                        .commit();
                break;
            case 2:
                ballGroundDetailFragment2 = new BallGroundDetailFragment2();
                ballGroundDetailFragment2.setArguments(getIntent().getExtras());
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frameLayout,ballGroundDetailFragment2,"ballGroundFragment2")
                        .commit();
                break;
        }

    }
}
