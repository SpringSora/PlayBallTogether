package com.example.springsora.balltogether.fragment.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.Comment;
import com.example.springsora.balltogether.custom_adapter.CommentAdapter;
import com.example.springsora.balltogether.custom_layout.FirstRefreshLoadLayout;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Springsora on 2016/5/9.
 */
public class ViewCommentFragment extends BaseFragment {
    private View view;
    private ImageView view_comment_back;
    private FirstRefreshLoadLayout comment_refresh;
    private ListView comment_listview;
    private List<Comment> comments;
    private Integer p_id;
    private CommentAdapter commentAdapter;
    private Integer page = 1;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        comments = (List<Comment>) getArguments().getSerializable("comments");
        p_id = getArguments().getInt("p_id");
        view  = inflater.inflate(R.layout.view_comment_fragment,null);
        initControl();
        return view;
    }

    private void initControl(){
        view_comment_back = (ImageView) view.findViewById(R.id.view_comment_back);
        comment_refresh = (FirstRefreshLoadLayout) view.findViewById(R.id.comment_refresh);
        comment_listview = (ListView) view.findViewById(R.id.comment_listview);
        commentAdapter = new CommentAdapter(getActivity(),comments,comment_listview);
        comment_listview.setAdapter(commentAdapter);
    }

    private void buttonListener(){
        view_comment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        comment_refresh.setOnLoadListener(new FirstRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                GetComments();
            }
        });
        comment_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                comments.clear();
                commentAdapter.notifyDataSetChanged();
                GetComments();
            }
        });
    }

    private void GetComments(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/GetCommentServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(comment_refresh.isLoading()){
                    comment_refresh.setLoading(false);
                }
                if(comment_refresh.isRefreshing()){
                    comment_refresh.setRefreshing(false);
                }
                if(!s.equals("false")){
                    Type type = new TypeToken<List<Comment>>(){}.getType();
                    List<Comment> temp = GsonUtil.jsonToBeanDateTime(s,type);
                    comments.addAll(temp);
                    commentAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
                if(comment_refresh.isLoading()){
                    comment_refresh.setLoading(false);
                }
                if(comment_refresh.isRefreshing()){
                    comment_refresh.setRefreshing(false);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> map = new HashMap<>();
                map.put("p_id",p_id+"");
                map.put("page",page+"");
                map.put("item",BallApplication.CommentItem);
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }
}
