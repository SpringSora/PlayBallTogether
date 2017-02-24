package com.example.springsora.balltogether.playballtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.comment.ViewCommentFragment;

/**
 * Created by Springsora on 2016/5/9.
 */
public class ViewCommentActivity extends AppCompatActivity {
    private ViewCommentFragment viewCommentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewCommentFragment = new ViewCommentFragment();
        viewCommentFragment.setArguments(getIntent().getExtras());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,viewCommentFragment,"viewCommentFragment")
                .commit();
    }
}
