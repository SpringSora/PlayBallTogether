package com.example.springsora.balltogether.pager.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BasePager;
import com.example.springsora.balltogether.bean.BallGround;
import com.example.springsora.balltogether.custom_adapter.SecondAdapter;
import com.example.springsora.balltogether.custom_layout.FirstRefreshLoadLayout;
import com.example.springsora.balltogether.custom_listview.SecondListView;
import com.example.springsora.balltogether.fragment.main.FirstFragment;
import com.example.springsora.balltogether.playballtogether.BallGroundDetailActivity;
import com.example.springsora.balltogether.playballtogether.MainActivity;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by JJBOOM on 2016/4/21.
 */
public class SecondPager extends BasePager {
    public SecondPager(Context context) {
        super(context);
    }
    private View view;
    private FirstRefreshLoadLayout second_refresh;
    private SecondListView secondListView;
    private List<BallGround> ballGrounds;
    private SecondAdapter secondAdapter;
    private Spinner spinner;
    private int page = 1;
    private int type = 0;

    @Override
    public View initView() {
        view = LayoutInflater.from(context).inflate(R.layout.pager_second,null);
        initControl();
        initData();
        return view;
    }

    @Override
    public void initData() {
        buttonListener();
    }

    private void initControl(){
        second_refresh = (FirstRefreshLoadLayout) view.findViewById(R.id.second_refresh);
        secondListView = (SecondListView) view.findViewById(R.id.second_listview);
        ballGrounds = new ArrayList<>();
        secondAdapter = new SecondAdapter(ballGrounds,context,secondListView);
        secondListView.setAdapter(secondAdapter);
        spinner =  ((FirstFragment)(((MainActivity)context).getFragmentManager().findFragmentByTag("First"))).getScreen_ball();
    }
    private void buttonListener(){
        second_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                ballGrounds.clear();
                secondAdapter.notifyDataSetChanged();
                GetBallGroundData();
            }
        });
        second_refresh.setOnLoadListener(new FirstRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                GetBallGroundData();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (type != position) {
                    type = position;
                    page = 1;
                    second_refresh.setRefreshing(true);
                    ballGrounds.clear();
                    secondAdapter.notifyDataSetChanged();
                    GetBallGroundData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        secondListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, BallGroundDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BallGround", ballGrounds.get(position));
                intent.putExtras(bundle);
                intent.putExtra("fragment",1);
                ((MainActivity)context).startActivityForResult(intent,2);
            }
        });
    }

    private void GetBallGroundData(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/GetBallGroundServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(second_refresh.isLoading()){
                    second_refresh.setLoading(false);
                }
                if(second_refresh.isRefreshing()){
                    second_refresh.setRefreshing(false);
                }
                if(!"false".equals(s)){
                    Type type = new TypeToken<List<BallGround>>(){}.getType();
                    List<BallGround> temp = GsonUtil.jsonToBeanDateTime(s,type);
                    ballGrounds.addAll(temp);
                    secondAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(context,"获取数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(second_refresh.isLoading()){
                    second_refresh.setLoading(false);
                }
                if(second_refresh.isRefreshing()){
                    second_refresh.setRefreshing(false);
                }
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
                BallApplication.getQueues().cancelAll("GetBallGroundData");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("page",page+"");
                map.put("item",BallApplication.BallGroundItem);
                if(BallApplication.getCity()!=null){
                    map.put("city",BallApplication.getCity());
                }
                map.put("type",type+"");
                return map;
            }
        };
        request.setTag("GetBallGroundData");
        BallApplication.getQueues().add(request);
    }

    public FirstRefreshLoadLayout getSecond_refresh() {
        return second_refresh;
    }

    public void  AfterRefresh(){
        page = 1;
        ballGrounds.clear();
        secondAdapter.notifyDataSetChanged();
        GetBallGroundData();
    }
}
