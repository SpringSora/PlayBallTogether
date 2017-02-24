package com.example.springsora.balltogether.playballtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.city.CityListFragment;

/**
 * Created by Springsora on 2016/5/11.
 */
public class CityListActivity extends AppCompatActivity {
    private CityListFragment cityListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityListFragment = new CityListFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,cityListFragment,"cityListFragment")
                .commit();
    }
}
