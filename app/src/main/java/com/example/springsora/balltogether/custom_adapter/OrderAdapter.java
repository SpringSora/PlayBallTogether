package com.example.springsora.balltogether.custom_adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.bean.MyOrder;
import com.example.springsora.balltogether.playballtogether.CommentActivity;
import com.example.springsora.balltogether.playballtogether.MyOrderActivity;
import com.example.springsora.balltogether.utils.AsyncImageLoader;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JJBOOM on 2016/5/7.
 */
public class OrderAdapter extends BaseAdapter {

    private List<MyOrder> orders;
    private Context context;
    private Integer id;
    private AsyncImageLoader asyncImageLoader;
    private ListView listView;
    private AlertDialog.Builder builder;

    public OrderAdapter(Context context, int id, List<MyOrder> orders,ListView listView) {
        this.context = context;
        this.id = id;
        this.orders = orders;
        this.listView = listView;
        asyncImageLoader = new AsyncImageLoader();
        builder = new AlertDialog.Builder(context);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder viewHolder;
        final MyOrder order = orders.get(position);
        if(convertView==null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.order_listview_item,null);
            viewHolder.order_ballground_type = (ImageView) view.findViewById(R.id.order_ballground_type);
            viewHolder.order_ballground_name = (TextView) view.findViewById(R.id.order_ballground_name);
            viewHolder.order_status = (TextView) view.findViewById(R.id.order_status);
            viewHolder.order_ballground = (LinearLayout) view.findViewById(R.id.order_ballground);
            viewHolder.order_ballground_pic = (ImageView) view.findViewById(R.id.order_ballground_pic);
            viewHolder.order_datetime = (TextView) view.findViewById(R.id.order_datetime);
            viewHolder.order_start2end = (TextView) view.findViewById(R.id.order_start2end);
            viewHolder.order_price = (TextView) view.findViewById(R.id.order_price);
            viewHolder.order_button1 = (RelativeLayout) view.findViewById(R.id.order_button1);
            viewHolder.order_button1_text = (TextView) view.findViewById(R.id.order_button1_text);
            viewHolder.order_button2 = (RelativeLayout) view.findViewById(R.id.order_button2);
            viewHolder.order_button2_text = (TextView) view.findViewById(R.id.order_button2_text);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.order_ballground_type.setImageResource(BallApplication.getMipmapHobby().get(order.getBallGround().getBallType()));
        viewHolder.order_ballground_name.setText(order.getBallGround().getGroundName());
        if(order.getBallGround().getBallGroundPic1Path()!=null){
            viewHolder.order_ballground_pic.setTag(BallApplication.ServerUrl +"/"+ order.getBallGround().getBallGroundPic1Path()+position);
            Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl +"/"+ order.getBallGround().getBallGroundPic1Path(), new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    ImageView imageViewTag = (ImageView) listView.findViewWithTag(imageUrl+position);
                    if(imageViewTag!=null){
                        imageViewTag.setImageDrawable(imageDrawable);
                    }
                }
            });
            if(drawable!=null){
                viewHolder.order_ballground_pic.setImageDrawable(drawable);
            }else{
                viewHolder.order_ballground_pic.setImageResource(R.mipmap.preview);
            }
        }else if(order.getBallGround().getBallGroundPic2Path()!=null){
            viewHolder.order_ballground_pic.setTag(BallApplication.ServerUrl +"/"+ order.getBallGround().getBallGroundPic2Path()+position);
            Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl +"/"+ order.getBallGround().getBallGroundPic2Path(), new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    ImageView imageViewTag = (ImageView) listView.findViewWithTag(imageUrl+position);
                    if(imageViewTag!=null){
                        imageViewTag.setImageDrawable(imageDrawable);
                    }
                }
            });
            if(drawable!=null){
                viewHolder.order_ballground_pic.setImageDrawable(drawable);
            }else{
                viewHolder.order_ballground_pic.setImageResource(R.mipmap.preview);
            }

        }else if(order.getBallGround().getBallGroundPic3Path()!=null){
            viewHolder.order_ballground_pic.setTag(BallApplication.ServerUrl + "/"+order.getBallGround().getBallGroundPic3Path()+position);
            Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + "/"+order.getBallGround().getBallGroundPic3Path(), new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    ImageView imageViewTag = (ImageView) listView.findViewWithTag(imageUrl+position);
                    if(imageViewTag!=null){
                        imageViewTag.setImageDrawable(imageDrawable);
                    }
                }
            });
            if(drawable!=null){
                viewHolder.order_ballground_pic.setImageDrawable(drawable);
            }else{
                viewHolder.order_ballground_pic.setImageResource(R.mipmap.preview);
            }
        }else{
            viewHolder.order_ballground_pic.setImageResource(R.mipmap.preview);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        viewHolder.order_datetime.setText("下单时间： "+format.format(order.getO_datetime()));
        viewHolder.order_start2end.setText("运动时间： "+order.getO_date()+" "+order.getO_starttime()+"～"+order.getO_endtime());
        viewHolder.order_price.setText("总价： "+order.getO_price()+"");

        switch (id){
            case R.id.all_order_layout:
                if(order.getO_ispay()==1){
                    viewHolder.order_status.setText("待确认");
                    viewHolder.order_button1_text.setText("申请退款");
                    viewHolder.order_button2_text.setText("确认支付");
                    viewHolder.order_button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder
                                    .setMessage("是否申请退款")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).setPositiveButton("退款", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ModifyOrderStatus(viewHolder.order_button1.getId(),order);
                                }
                            }).show();
                        }
                    });
                    viewHolder.order_button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder
                                    .setMessage("是否确认支付")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ModifyOrderStatus(id, order);
                                }
                            }).show();
                        }
                    });
                }else if(order.getO_isevaluate()==1){
                    viewHolder.order_status.setText("待评价");
                    viewHolder.order_button1.setVisibility(View.GONE);
                    viewHolder.order_button2_text.setText("评价");
                    viewHolder.order_button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CommentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("order", order);
                            bundle.putInt("position", position);
                            intent.putExtras(bundle);
                            ((MyOrderActivity) context).getFragmentManager().findFragmentByTag("OrderFragment").startActivityForResult(intent, 4);
                        }
                    });
                }else if(order.getO_isrefund()==1){
                    viewHolder.order_status.setText("已退款");
                    viewHolder.order_button1.setVisibility(View.INVISIBLE);
                    viewHolder.order_button2.setVisibility(View.INVISIBLE);
                }else {
                    viewHolder.order_status.setText("已完成");
                    viewHolder.order_button1.setVisibility(View.INVISIBLE);
                    viewHolder.order_button2.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.person_pay:
                viewHolder.order_status.setText("待确认");
                viewHolder.order_button1_text.setText("申请退款");
                viewHolder.order_button2_text.setText("确认支付");
                viewHolder.order_button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder
                                .setMessage("是否申请退款")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setPositiveButton("退款", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ModifyOrderStatus(viewHolder.order_button1.getId(),order);
                            }
                        }).show();
                    }
                });
                viewHolder.order_button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder
                                .setMessage("是否确认支付")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ModifyOrderStatus(id,order);
                            }
                        }).show();
                    }
                });
                break;
            case R.id.person_evaluate:
                viewHolder.order_status.setText("待评价");
                viewHolder.order_button1.setVisibility(View.GONE);
                viewHolder.order_button2_text.setText("评价");
                viewHolder.order_button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CommentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order",order);
                        bundle.putInt("position",position);
                        intent.putExtras(bundle);
                        ((MyOrderActivity) context).getFragmentManager().findFragmentByTag("OrderFragment").startActivityForResult(intent, 4);
                    }
                });
                break;
            case R.id.person_refund:
                viewHolder.order_status.setText("已退款");
                viewHolder.order_button1.setVisibility(View.INVISIBLE);
                viewHolder.order_button2.setVisibility(View.INVISIBLE);
                break;
        }
        return view;
    }

    class ViewHolder{
        ImageView order_ballground_type;
        TextView order_ballground_name;
        TextView order_status;
        LinearLayout order_ballground;
        ImageView order_ballground_pic;
        TextView order_datetime;
        TextView order_start2end;
        TextView order_price;
        RelativeLayout order_button1;
        TextView order_button1_text;
        RelativeLayout order_button2;
        TextView order_button2_text;
    }

    private void ModifyOrderStatus(final Integer Rid, final MyOrder order){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/ModifyOrderStatusServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(Boolean.valueOf(s)){
                    ((MyOrderActivity)context).finish();
                }else{
                    Toast.makeText(context,"操作失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String > map = new HashMap<>();
                map.put("o_id",order.getO_id()+"");
                switch (Rid){
                    case R.id.order_button1:
                        map.put("isrefund","1");
                        break;
                    case R.id.person_pay:
                        map.put("isevaluate","1");
                        break;
                }
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }
}
