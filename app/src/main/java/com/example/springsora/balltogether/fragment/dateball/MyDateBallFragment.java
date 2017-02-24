package com.example.springsora.balltogether.fragment.dateball;

import android.content.Intent;
import android.opengl.GLException;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.springsora.balltogether.bean.DateBall;
import com.example.springsora.balltogether.custom_adapter.MyDateBallAdapter;
import com.example.springsora.balltogether.custom_layout.FirstRefreshLoadLayout;
import com.example.springsora.balltogether.playballtogether.DateBallDetailActivity;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Springsora on 2016/5/10.
 */
public class MyDateBallFragment extends BaseFragment {
    private View view;
    private FirstRefreshLoadLayout mydateball_refresh;
    private ListView mydateball_listview;
    private MyDateBallAdapter myDateBallAdapter;
    private List<DateBall> dateBalls;
    private Integer page = 1;
    private ImageView mydateball_back;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
        GetMyDateBall();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.mydateball_fragment,null);
        dateBalls = new ArrayList<>();
        mydateball_listview = (ListView) view.findViewById(R.id.mydateball_listview);
        mydateball_refresh = (FirstRefreshLoadLayout) view.findViewById(R.id.mydateball_refresh);
        mydateball_back = (ImageView) view.findViewById(R.id.mydateball_back);
        myDateBallAdapter = new MyDateBallAdapter(getActivity(),dateBalls,mydateball_listview);
        mydateball_listview.setAdapter(myDateBallAdapter);
        return view;
    }

    private void buttonListener(){
        mydateball_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mydateball_refresh.setOnLoadListener(new FirstRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page++;
                GetMyDateBall();
            }
        });

        mydateball_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                dateBalls.clear();
                myDateBallAdapter.notifyDataSetChanged();
                GetMyDateBall();
            }
        });

        mydateball_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DateBallDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DateBall", dateBalls.get(position));
                bundle.putInt("type",2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void GetMyDateBall(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/MyDateBallServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(mydateball_refresh.isLoading()){
                    mydateball_refresh.setLoading(false);
                }
                if(mydateball_refresh.isRefreshing()){
                    mydateball_refresh.setRefreshing(false);
                }
                if(!"false".equals(s)){
                    Type type = new TypeToken<List<DateBall>>(){}.getType();
                    List<DateBall> temp = GsonUtil.jsonToBeanDateTime(s,type);
                    dateBalls.addAll(temp);
                    myDateBallAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(mydateball_refresh.isLoading()){
                    mydateball_refresh.setLoading(false);
                }
                if(mydateball_refresh.isRefreshing()){
                    mydateball_refresh.setRefreshing(false);
                }
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("u_id",BallApplication.getUser().getU_id()+"");
                map.put("page",page.toString());
                map.put("item",BallApplication.BallGroundItem);
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }
}
