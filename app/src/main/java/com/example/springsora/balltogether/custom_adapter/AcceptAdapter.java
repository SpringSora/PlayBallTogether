package com.example.springsora.balltogether.custom_adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.example.springsora.balltogether.bean.DateBall;
import com.example.springsora.balltogether.bean.Promise;
import com.example.springsora.balltogether.custom_view.CircleImageView;
import com.example.springsora.balltogether.utils.AsyncImageLoader;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Springsora on 2016/5/11.
 */
public class AcceptAdapter extends BaseAdapter {

    private List<Promise> promises;
    private ListView listView;
    private Context context;
    private AsyncImageLoader asyncImageLoader;
    private AlertDialog.Builder builder;

    public AcceptAdapter(Context context, ListView listView, List<Promise> promises) {
        this.context = context;
        this.listView = listView;
        this.promises = promises;
        asyncImageLoader = new AsyncImageLoader();
        builder = new AlertDialog.Builder(context);
    }

    @Override
    public int getCount() {
        return promises.size();
    }

    @Override
    public Object getItem(int position) {
        return promises.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder;
        final Promise promise = promises.get(position);
        if(convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.accept_listview_item,null);
            holder = new ViewHolder();
            holder.accept_user_pic = (CircleImageView) view.findViewById(R.id.accept_user_pic);
            holder.accept_user_nickname = (TextView) view.findViewById(R.id.accept_user_nickname);
            holder.accept_datetime = (TextView) view.findViewById(R.id.accept_datetime);
            holder.accept_type = (TextView) view.findViewById(R.id.accept_type);
            holder.accept_date = (TextView) view.findViewById(R.id.accept_date);
            holder.accept_time = (TextView) view.findViewById(R.id.accept_time);
            holder.accept_ballground = (TextView) view.findViewById(R.id.accept_ballground);
            holder.accept_give_up = (RelativeLayout) view.findViewById(R.id.accept_give_up);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.accept_user_pic.setTag(BallApplication.ServerUrl + promise.getUser().getU_pic()+position);
        final Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + promise.getUser().getU_pic(), new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewTag = (ImageView) listView.findViewWithTag(imageUrl+position);
                if(imageViewTag!=null){
                    imageViewTag.setImageDrawable(imageDrawable);
                }
            }
        });
        if(drawable!=null){
            holder.accept_user_pic.setImageDrawable(drawable);
        }else{
            holder.accept_user_pic.setImageResource(R.mipmap.default_logo);
        }
        if(promise.getDateBall().getUser().getU_nickname()!=null){
            holder.accept_user_nickname.setText(promise.getDateBall().getUser().getU_nickname());
        }else{
            holder.accept_user_nickname.setText(promise.getDateBall().getUser().getU_phone());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        holder.accept_datetime.setText(simpleDateFormat.format(promise.getDateBall().getD_time()));
        holder.accept_type.setText(BallApplication.getHobbyMap().get(promise.getDateBall().getD_type() + ""));
        holder.accept_date.setText(promise.getDateBall().getD_date());
        holder.accept_time.setText(promise.getDateBall().getD_starttime()+"～"+promise.getDateBall().getD_endtime());
        if(promise.getDateBall().getBallGround()!=null){
            holder.accept_ballground.setText(promise.getDateBall().getBallGround().getGroundName());
        }else{
            holder.accept_ballground.setText("未选择球场");
        }
        holder.accept_give_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("您是否放弃此次运动")
                        .setMessage("如果已经预订相关球场可退款")
                        .setPositiveButton("放弃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePromiese(promise);
                                promises.remove(position);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("不放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
        return view;
    }

    class ViewHolder{
        CircleImageView accept_user_pic;
        TextView accept_user_nickname;
        TextView accept_datetime;
        TextView accept_type;
        TextView accept_date;
        TextView accept_time;
        TextView accept_ballground;
        RelativeLayout accept_give_up;
    }

    private void deletePromiese(final Promise promise){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/DeletePromiseServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(!"false".equals(s)){

                }else{
                    Toast.makeText(context,"放弃失败",Toast.LENGTH_SHORT).show();
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
                HashMap<String,String> map = new HashMap<>();
                map.put("u_id",BallApplication.getUser().getU_id().toString());
                map.put("d_id",promise.getDateBall().getD_id()+"");
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }
}
