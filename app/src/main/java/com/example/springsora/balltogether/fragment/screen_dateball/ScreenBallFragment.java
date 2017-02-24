package com.example.springsora.balltogether.fragment.screen_dateball;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.DateBall;
import com.example.springsora.balltogether.custom_adapter.FirstAdapter;
import com.example.springsora.balltogether.custom_layout.FirstRefreshLoadLayout;
import com.example.springsora.balltogether.custom_listview.FirstListView;
import com.example.springsora.balltogether.custom_listview.ScreenDateBallListview;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JJBOOM on 2016/5/5.
 */
public class ScreenBallFragment extends BaseFragment {
    private View view;
    private FirstListView screenDateBallListview;
    private FirstRefreshLoadLayout firstRefreshLoadLayout;
    private ImageView screen_dateball_back;
    private Spinner screen_dateball_spinner;
    private FirstAdapter adapter;
    private List<DateBall> dateBalls;
    private int page = 1;
    private int type;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
        firstRefreshLoadLayout.setRefreshing(true);
        getScreenDateBall();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.screen_dateball,null);
        initControl();
        return view;
    }

    private void initControl(){
        screenDateBallListview = (FirstListView) view.findViewById(R.id.screen_dateball_listview);
        firstRefreshLoadLayout = (FirstRefreshLoadLayout) view.findViewById(R.id.screen_dateball_refresh);
        screen_dateball_back = (ImageView) view.findViewById(R.id.screen_dateball_back);
        screen_dateball_spinner = (Spinner) view.findViewById(R.id.screen_dateball_spinner);
        Bundle bundle = getArguments();
        type = bundle.getInt("Type");
        screen_dateball_spinner.setSelection(type-1);
        dateBalls = new ArrayList<>();
        adapter = new FirstAdapter(dateBalls,getActivity(),screenDateBallListview);
        screenDateBallListview.setAdapter(adapter);
    }

    private void buttonListener(){
        screen_dateball_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if((position+1)!=type){
                    type = position+1;
                    page = 1;
                    firstRefreshLoadLayout.setRefreshing(true);
                    dateBalls.clear();
                    adapter.notifyDataSetChanged();
                    getScreenDateBall();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        screen_dateball_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        firstRefreshLoadLayout.setOnLoadListener(new FirstRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                getScreenDateBall();
            }
        });

        firstRefreshLoadLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                dateBalls.clear();
                adapter.notifyDataSetChanged();
                getScreenDateBall();
            }
        });
    }

    private void getScreenDateBall(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/GetDateBallServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(firstRefreshLoadLayout.isLoading()){
                    firstRefreshLoadLayout.setLoading(false);
                }
                if(firstRefreshLoadLayout.isRefreshing()){
                    firstRefreshLoadLayout.setRefreshing(false);
                }
                if(!s.equals("false")){
                    Type type = new TypeToken<List<DateBall>>(){}.getType();
                    List<DateBall> temps = GsonUtil.jsonToBeanDateTime(s, type);
                    dateBalls.addAll(temps);
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
                if(firstRefreshLoadLayout.isLoading()){
                    firstRefreshLoadLayout.setLoading(false);
                }
                if(firstRefreshLoadLayout.isRefreshing()){
                    firstRefreshLoadLayout.setRefreshing(false);
                }
                BallApplication.getQueues().cancelAll("requestScreenDateBall");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("item",BallApplication.DateBallItem);
                map.put("page",String.valueOf(page));
                if(BallApplication.getCity()!=null){
                    map.put("city",BallApplication.getCity());
                }
                if(type!=0){
                    map.put("type",type+"");
                }
                return map;
            }
        };
        request.setTag("requestScreenDateBall");
        BallApplication.getQueues().add(request);
    }
}
