package com.example.springsora.balltogether.playballtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.fragment.comment.CommentFragment;

/**
 * Created by Springsora on 2016/5/9.
 */
public class CommentActivity extends AppCompatActivity {
    private CommentFragment commentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commentFragment = new CommentFragment();
        commentFragment.setArguments(getIntent().getExtras());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frameLayout,commentFragment,"commentFragment")
                .commit();
    }
}
