package com.example.springsora.balltogether.fragment.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.MyOrder;
import com.example.springsora.balltogether.custom_adapter.OrderAdapter;
import com.example.springsora.balltogether.custom_layout.FirstRefreshLoadLayout;

import java.util.List;

/**
 * Created by JJBOOM on 2016/5/7.
 */
public class OrderFragment extends BaseFragment {
    private View view;

    private ImageView order_back;
    private TextView order_title;
    private FirstRefreshLoadLayout order_refresh;
    private ListView order_listview;
    private TextView order_edit;

    private OrderAdapter orderAdapter;
    private List<MyOrder> orders;
    private Integer id;


    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        id = getArguments().getInt("id");
        orders = (List<MyOrder>) getArguments().getSerializable("orders");
        view = inflater.inflate(R.layout.order_fragment,null);
        initControl();
        return view;
    }

    private void  initControl(){
        order_back = (ImageView) view.findViewById(R.id.order_back);
        order_title = (TextView) view.findViewById(R.id.order_title);
        order_refresh = (FirstRefreshLoadLayout) view.findViewById(R.id.order_refresh);
        order_listview = (ListView) view.findViewById(R.id.order_listview);
        order_edit = (TextView) view.findViewById(R.id.order_edit);
        orderAdapter = new OrderAdapter(getActivity(),id,orders,order_listview);
        order_listview.setAdapter(orderAdapter);
        switch (id){
            case R.id.all_order_layout:
                order_title.setText("全部订单");
                break;
            case R.id.person_pay:
                order_title.setText("待使用");
                order_edit.setVisibility(view.GONE);
                break;
            case R.id.person_evaluate:
                order_title.setText("待评价");
                order_edit.setVisibility(view.VISIBLE);
                break;
            case R.id.person_refund:
                order_title.setText("退款");
                order_edit.setVisibility(view.VISIBLE);
                break;
        }
    }

    private void buttonListener(){
        order_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        order_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                order_refresh.setRefreshing(false);
            }
        });
        order_refresh.setOnLoadListener(new FirstRefreshLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                order_refresh.setLoading(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            orders.remove(data.getExtras().getInt("position"));
            orderAdapter.notifyDataSetChanged();
        }
    }
}
