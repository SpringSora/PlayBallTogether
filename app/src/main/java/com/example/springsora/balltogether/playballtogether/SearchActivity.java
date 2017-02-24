package com.example.springsora.balltogether.playballtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.search.SearchFragment;

/**
 * Created by JJBOOM on 2016/4/24.
 */
public class SearchActivity extends AppCompatActivity {
    private SearchFragment searchFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchFragment = new SearchFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,searchFragment,"searchFragment")
                .commit();

    }
}
