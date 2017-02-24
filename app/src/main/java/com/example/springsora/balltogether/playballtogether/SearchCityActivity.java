package com.example.springsora.balltogether.playballtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.search.SearchCityFragment;

/**
 * Created by Springsora on 2016/5/12.
 */
public class SearchCityActivity extends AppCompatActivity {
    private SearchCityFragment searchCityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchCityFragment = new SearchCityFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,searchCityFragment,"searchCityFragment")
                .commit();
    }
}
