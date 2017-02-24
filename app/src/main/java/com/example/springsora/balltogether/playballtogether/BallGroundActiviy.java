package com.example.springsora.balltogether.playballtogether;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.ballground.BallGrondFragment;

/**
 * Created by Springsora on 2016/5/9.
 */
public class BallGroundActiviy extends AppCompatActivity {
    private BallGrondFragment ballGrondFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ballGrondFragment = new BallGrondFragment();
        ballGrondFragment.setArguments(getIntent().getExtras());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,ballGrondFragment,"ballGrondFragment")
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    Intent intent = new Intent();
                    intent.putExtras(data.getExtras());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }
}
