package com.example.springsora.balltogether.playballtogether;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.custom_listview.FirstListView;
import com.example.springsora.balltogether.fragment.main.FirstFragment;
import com.example.springsora.balltogether.fragment.screen_dateball.ScreenBallFragment;

/**
 * Created by JJBOOM on 2016/5/5.
 */
public class ScreenBallActivity extends AppCompatActivity {
    private ScreenBallFragment screenBallFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenBallFragment = new ScreenBallFragment();
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putInt("Type", intent.getIntExtra("Type", 0));
        screenBallFragment.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,screenBallFragment,"ScreenBallFragment")
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ScreenBallActivity","onStart");
    }
}
