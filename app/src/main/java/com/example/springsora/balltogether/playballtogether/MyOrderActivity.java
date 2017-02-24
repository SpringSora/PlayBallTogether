package com.example.springsora.balltogether.playballtogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.order.OrderFragment;

/**
 * Created by JJBOOM on 2016/5/7.
 */
public class MyOrderActivity extends AppCompatActivity {

    private OrderFragment orderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orderFragment = new OrderFragment();
        orderFragment.setArguments(getIntent().getExtras());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,orderFragment,"OrderFragment")
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Actvity", "Ok");
    }
}
