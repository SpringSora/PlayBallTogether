package com.example.springsora.balltogether.fragment.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
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
import com.example.springsora.balltogether.bean.User;
import com.example.springsora.balltogether.custom_adapter.ResultPagerAdapter;
import com.example.springsora.balltogether.custom_adapter.SecondAdapter;
import com.example.springsora.balltogether.custom_adapter.UserAdapter;
import com.example.springsora.balltogether.custom_layout.FirstRefreshLoadLayout;
import com.example.springsora.balltogether.custom_listview.SecondListView;
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
 * Created by Springsora on 2016/5/10.
 */
public class SearchResultFragment extends BaseFragment {
    private View view;
    private ImageView result_back;
    private ViewPager result_viewpager;
    private PagerTabStrip result_tab;
    private List<View> viewContainter;
    private List<String> titleContainter;
    private ResultPagerAdapter resultPagerAdapter;
    private View pager1;
    private View pager2;
    private List<BallGround> ballGrounds;

    private FirstRefreshLoadLayout result_ballground_refresh;

    private SecondListView result_ballground_listview;

    private SecondAdapter secondAdapter ;

    private Integer page1 = 1;

    private Integer page2 = 1;

    private String content;

    private FirstRefreshLoadLayout result_user_refresh;

    private ListView result_user_listview;

    private UserAdapter userAdapter;

    private List<User> users;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
        GetBallGround();
        GetUser();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.search_result_fragment,null);
        content = getArguments().getString("content");
        viewContainter = new ArrayList<>();
        titleContainter = new ArrayList<>();
        ballGrounds = new ArrayList<>();
        users = new ArrayList<>();
        titleContainter.add("球场");
        titleContainter.add("用户");
        pager1 = LayoutInflater.from(getActivity()).inflate(R.layout.result_ballground,null);
        pager2 = LayoutInflater.from(getActivity()).inflate(R.layout.result_user,null);
        viewContainter.add(pager1);
        viewContainter.add(pager2);
        resultPagerAdapter = new ResultPagerAdapter(titleContainter,viewContainter);

        initControl();
        return view;
    }

    private void initControl(){
        result_back = (ImageView) view.findViewById(R.id.result_back);
        result_viewpager = (ViewPager) view.findViewById(R.id.result_viewPager);
        result_tab = (PagerTabStrip) view.findViewById(R.id.result_tab);
        result_ballground_refresh = (FirstRefreshLoadLayout) pager1.findViewById(R.id.result_ballground_refresh);
        result_ballground_listview = (SecondListView) pager1.findViewById(R.id.result_ballground_listview);
        result_user_refresh = (FirstRefreshLoadLayout) pager2.findViewById(R.id.result_user_refresh);
        result_user_listview = (ListView) pager2.findViewById(R.id.result_user_listview);
        userAdapter = new UserAdapter(getActivity(),result_user_listview,users);
        secondAdapter = new SecondAdapter(ballGrounds,getActivity(),result_ballground_listview);
        result_ballground_listview.setAdapter(secondAdapter);
        result_user_listview.setAdapter(userAdapter);
        //取消tab下面的长横线
        result_tab.setDrawFullUnderline(false);
        //设置tab的背景色
        result_tab.setBackgroundColor(this.getResources().getColor(R.color.colorWhite));
        //设置当前tab页签的下划线颜色
        result_tab.setTabIndicatorColor(this.getResources().getColor(R.color.colorTitle));
        result_tab.setTextSpacing(0);
        result_viewpager.setAdapter(resultPagerAdapter);
    }

    private void buttonListener(){
        result_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        result_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        result_ballground_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page1 = 1;
                ballGrounds.clear();
                secondAdapter.notifyDataSetChanged();
                GetBallGround();
            }
        });
        result_ballground_refresh.setOnLoadListener(new FirstRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page1++;
                GetBallGround();
            }
        });
        result_ballground_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BallGroundDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BallGround", ballGrounds.get(position));
                intent.putExtras(bundle);
                intent.putExtra("fragment", 1);
                startActivityForResult(intent, 2);
            }
        });

        result_user_refresh.setOnLoadListener(new FirstRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                page2++;
                GetUser();
            }
        });

        result_user_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page2 = 1;
                users.clear();
                userAdapter.notifyDataSetChanged();
                GetUser();
            }
        });
    }

    private void GetBallGround(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/SearchBallGroundServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(result_ballground_refresh.isLoading()){
                    result_ballground_refresh.setLoading(false);
                }
                if(result_ballground_refresh.isRefreshing()){
                    result_ballground_refresh.setRefreshing(true);
                }
                if(!"false".equals(s)){
                    Type type = new TypeToken<List<BallGround>>(){}.getType();
                    List<BallGround> temp = GsonUtil.jsonToBeanDateTime(s,type);
                    ballGrounds.addAll(temp);
                    secondAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(result_ballground_refresh.isLoading()){
                    result_ballground_refresh.setLoading(false);
                }
                if(result_ballground_refresh.isRefreshing()){
                    result_ballground_refresh.setRefreshing(true);
                }
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("page",page1.toString());
                map.put("item",BallApplication.BallGroundItem);
                map.put("city",BallApplication.getCity());
                map.put("key",content);
               return map;
            }
        };
        BallApplication.getQueues().add(request);
    }


    private void GetUser(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/SearchUserServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(result_user_refresh.isRefreshing()){
                    result_user_refresh.setRefreshing(false);
                }
                if(result_user_refresh.isLoading()){
                    result_user_refresh.setLoading(false);
                }
                if(!"false".equals(s)){
                    Type type = new TypeToken<List<User>>(){}.getType();
                    List<User> temp = GsonUtil.jsonToBean(s,type);
                    users.addAll(temp);
                    userAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(),"数据获取失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
                if(result_user_refresh.isRefreshing()){
                    result_user_refresh.setRefreshing(false);
                }
                if(result_user_refresh.isLoading()){
                    result_user_refresh.setLoading(false);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("page",page2.toString());
                map.put("item",BallApplication.UserItem);
                map.put("key",content);
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
