package com.example.springsora.balltogether.playballtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.dateball.DateBallDetailFragment;

/**
 * Created by JJBOOM on 2016/5/5.
 */
public class DateBallDetailActivity extends AppCompatActivity {
    private DateBallDetailFragment dateBallDetailFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateBallDetailFragment = new DateBallDetailFragment();
        Bundle bundle = getIntent().getExtras();
        dateBallDetailFragment.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,dateBallDetailFragment,"dateBallDetailFragment")
                .commit();
    }
}
