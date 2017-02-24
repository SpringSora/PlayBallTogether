package com.example.springsora.balltogether.playballtogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.search.SearchResultFragment;

/**
 * Created by Springsora on 2016/5/10.
 */
public class SearchResultActivity extends AppCompatActivity {
    private SearchResultFragment searchResultFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchResultFragment = new SearchResultFragment();
        searchResultFragment.setArguments(getIntent().getExtras());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,searchResultFragment,"searchResultFragment")
                .commit();
    }
}
