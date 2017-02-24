package com.example.springsora.balltogether.fragment.dateball;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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
import com.example.springsora.balltogether.bean.Promise;
import com.example.springsora.balltogether.custom_adapter.AcceptAdapter;
import com.example.springsora.balltogether.custom_layout.FirstRefreshLoadLayout;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Springsora on 2016/5/11.
 */
public class AcceptFragment extends BaseFragment {
    private View view;
    private ImageView accept_back;
    private FirstRefreshLoadLayout accept_refresh;
    private ListView accept_listview;
    private Integer page = 1;
    private List<Promise> promises;
    private AcceptAdapter acceptAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
        GetPromise();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.accept_fragment,null);
        promises = new ArrayList<>();
        initControl();
        return view;
    }
    private void initControl(){
        accept_back = (ImageView) view.findViewById(R.id.accept_back);
        accept_refresh = (FirstRefreshLoadLayout) view.findViewById(R.id.accept_refresh);
        accept_listview = (ListView) view.findViewById(R.id.accept_listview);
        acceptAdapter = new AcceptAdapter(getActivity(),accept_listview,promises);
        accept_listview.setAdapter(acceptAdapter);
    }

    private void buttonListener(){
        accept_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        accept_refresh.setOnLoadListener(new FirstRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                GetPromise();
            }
        });
        accept_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                promises.clear();
                acceptAdapter.notifyDataSetChanged();
                GetPromise();
            }
        });
    }

    private void GetPromise(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/GetPromiseServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(accept_refresh.isLoading()){
                    accept_refresh.setLoading(false);
                }
                if(accept_refresh.isRefreshing()){
                    accept_refresh.setRefreshing(false);
                }
                if(!"false".equals(s)){
                    Type type = new TypeToken<List<Promise>>(){}.getType();
                    List<Promise> temp = GsonUtil.jsonToBeanDateTime(s,type);
                    promises.addAll(temp);
                    acceptAdapter.notifyDataSetChanged();

                }else{

                    Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(accept_refresh.isLoading()){
                    accept_refresh.setLoading(false);
                }
                if(accept_refresh.isRefreshing()){
                    accept_refresh.setRefreshing(false);
                }
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("page",page.toString());
                map.put("item",BallApplication.PromiseItem);
                map.put("u_id",BallApplication.getUser().getU_id().toString());
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }
}
