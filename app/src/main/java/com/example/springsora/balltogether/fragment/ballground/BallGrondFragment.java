package com.example.springsora.balltogether.fragment.ballground;

import android.content.Intent;
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
import com.example.springsora.balltogether.bean.BallGround;
import com.example.springsora.balltogether.custom_adapter.SecondAdapter;
import com.example.springsora.balltogether.custom_layout.FirstRefreshLoadLayout;
import com.example.springsora.balltogether.custom_listview.SecondListView;
import com.example.springsora.balltogether.playballtogether.BallGroundDetailActivity;
import com.example.springsora.balltogether.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Springsora on 2016/5/9.
 */
public class BallGrondFragment extends BaseFragment {
    private View view;

    private SecondListView ballground_listview;

    private ImageView ballground_back;

    private List<BallGround> ballGrounds;

    private SecondAdapter secondAdapter;

    private FirstRefreshLoadLayout ballground_refresh;

    private int type;

    private int page = 1;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
        GetBallGroundData();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.ballground_fragment,null);
        type = getArguments().getInt("type");
        initControl();
        return view;
    }

    private void initControl(){
        ballground_refresh = (FirstRefreshLoadLayout) view.findViewById(R.id.ballground_refresh);
        ballground_listview = (SecondListView) view.findViewById(R.id.ballground_listview);
        ballground_back = (ImageView) view.findViewById(R.id.ballground_back);
        ballGrounds = new ArrayList<>();
        secondAdapter = new SecondAdapter(ballGrounds,getActivity(),ballground_listview);
        ballground_listview.setAdapter(secondAdapter);
    }

    private void buttonListener(){
        ballground_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ballground_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                ballGrounds.clear();
                secondAdapter.notifyDataSetChanged();
                GetBallGroundData();
            }
        });

        ballground_refresh.setOnLoadListener(new FirstRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {

                page++;
                GetBallGroundData();
            }
        });

        ballground_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BallGroundDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BallGround", ballGrounds.get(position));
                intent.putExtras(bundle);
                intent.putExtra("fragment",2);
                getActivity().startActivityForResult(intent,0);
            }
        });
    }

    private void GetBallGroundData(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/GetBallGroundServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(ballground_refresh.isLoading()){
                    ballground_refresh.setLoading(false);
                }
                if(ballground_refresh.isRefreshing()){
                    ballground_refresh.setRefreshing(false);
                }
                if(!"false".equals(s)){
                    Type type = new TypeToken<List<BallGround>>(){}.getType();
                    List<BallGround> temp = GsonUtil.jsonToBeanDateTime(s, type);
                    ballGrounds.addAll(temp);
                    secondAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(ballground_refresh.isLoading()){
                    ballground_refresh.setLoading(false);
                }
                if(ballground_refresh.isRefreshing()){
                    ballground_refresh.setRefreshing(false);
                }
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
