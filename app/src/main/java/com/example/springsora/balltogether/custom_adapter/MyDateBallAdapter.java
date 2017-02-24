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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.bean.DateBall;
import com.example.springsora.balltogether.custom_view.CircleImageView;
import com.example.springsora.balltogether.playballtogether.DateBallDetailActivity;
import com.example.springsora.balltogether.utils.AsyncImageLoader;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Springsora on 2016/5/10.
 */
public class MyDateBallAdapter extends BaseAdapter {
    private ListView listView;
    private Context context;
    private List<DateBall> dateBallList;
    private AsyncImageLoader asyncImageLoader;
    private Date CurDate;
    private AlertDialog.Builder builder;

    public MyDateBallAdapter(Context context, List<DateBall> dateBallList, ListView listView) {
        this.context = context;
        this.dateBallList = dateBallList;
        this.listView = listView;
        asyncImageLoader = new AsyncImageLoader();
        CurDate = new Date(System.currentTimeMillis());
        builder = new AlertDialog.Builder(context);
    }

    @Override
    public int getCount() {
        return dateBallList.size();
    }

    @Override
    public Object getItem(int position) {
        return dateBallList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = null;
        ViewHolder holder;
        final DateBall dateBall = dateBallList.get(position);
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.mydateball_listview_item,null);
            holder = new ViewHolder();
            holder.mydateball_layout = (RelativeLayout) view.findViewById(R.id.mydateball_layout);
            holder.mydateball_user_pic = (CircleImageView) view.findViewById(R.id.mydateball_user_pic);
            holder.mydateball_user_nickname = (TextView) view.findViewById(R.id.mydateball_user_nickname);
            holder.mydateball_datetime = (TextView) view.findViewById(R.id.mydateball_datetime);
            holder.mydateball_title = (TextView) view.findViewById(R.id.mydateball_title);
            holder.mydateball_pic = (ImageView) view.findViewById(R.id.mydateball_pic);
            holder.mydateball_type = (TextView) view.findViewById(R.id.mydateball_type);
            holder.mydateball_status = (TextView) view.findViewById(R.id.mydateball_status);
            holder.mydateball_cancel = (LinearLayout) view.findViewById(R.id.mydateball_cancel);
            holder.mydateball_cancel_layout = (RelativeLayout) view.findViewById(R.id.mydateball_cancel_layout);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.mydateball_user_pic.setTag(BallApplication.ServerUrl + dateBall.getUser().getU_pic()+position);
        Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + dateBall.getUser().getU_pic(), new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewTag = (ImageView) listView.findViewWithTag(imageUrl+position);
                if(imageViewTag!=null){
                    imageViewTag.setImageDrawable(imageDrawable);
                }
            }
        });
        if(drawable!=null){
            holder.mydateball_user_pic.setImageDrawable(drawable);
        }else{
            holder.mydateball_user_pic.setImageResource(R.mipmap.default_logo);
        }
        if(dateBall.getUser().getU_nickname()!=null){
            holder.mydateball_user_nickname.setText(dateBall.getUser().getU_nickname());
        }else{
            holder.mydateball_user_nickname.setText(dateBall.getUser().getU_phone());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        holder.mydateball_datetime.setText(simpleDateFormat.format(dateBall.getD_time()));
        holder.mydateball_title.setText(dateBall.getD_title());
        if(Integer.valueOf(simpleDateFormat.format(CurDate).split("-")[0])-Integer.valueOf(dateBall.getD_date().split("-")[0]) == 0){
            if(Integer.valueOf(simpleDateFormat.format(CurDate).split("-")[1])-Integer.valueOf(dateBall.getD_date().split("-")[1]) == 0){
                if(Integer.valueOf(simpleDateFormat.format(CurDate).split("-")[2])-Integer.valueOf(dateBall.getD_date().split("-")[2]) == 0){
                    if(Integer.valueOf(simpleDateFormat1.format(CurDate).split(":")[0])-Integer.valueOf(dateBall.getD_starttime().split(":")[0]) == 0){
                        if(Integer.valueOf(simpleDateFormat1.format(CurDate).split(":")[1])-Integer.valueOf(dateBall.getD_starttime().split(":")[1]) == 0){
                            holder.mydateball_status.setText("正在进行");
                        }else if(Integer.valueOf(simpleDateFormat1.format(CurDate).split(":")[1])-Integer.valueOf(dateBall.getD_starttime().split(":")[1]) < 0){
                            holder.mydateball_status.setText("正在进行");
                        }else{
                            holder.mydateball_status.setText("已过期");
                        }
                    }else if(Integer.valueOf(simpleDateFormat1.format(CurDate).split(":")[0])-Integer.valueOf(dateBall.getD_starttime().split(":")[0]) < 0){
                        holder.mydateball_status.setText("正在进行");
                    }else{
                        holder.mydateball_status.setText("已过期");
                    }
                }else if(Integer.valueOf(simpleDateFormat.format(CurDate).split("-")[2])-Integer.valueOf(dateBall.getD_date().split("-")[2]) < 0){
                    holder.mydateball_status.setText("正在进行");
                }else{
                    holder.mydateball_status.setText("已过期");
                }
            }else if(Integer.valueOf(simpleDateFormat.format(CurDate).split("-")[1])-Integer.valueOf(dateBall.getD_date().split("-")[1]) < 0){
                holder.mydateball_status.setText("正在进行");
            }else{
                holder.mydateball_status.setText("已过期");
            }
        }else if(Integer.valueOf(simpleDateFormat.format(CurDate).split("-")[0])-Integer.valueOf(dateBall.getD_date().split("-")[0]) < 0){
            holder.mydateball_status.setText("正在进行");
        }else{
            holder.mydateball_status.setText("已过期");
        }
        holder.mydateball_pic.setTag(BallApplication.ServerUrl + dateBall.getD_pic() + position);
        if(dateBall.getD_pic()!=null&&!dateBall.getD_pic().trim().equals("")){
            holder.mydateball_pic.setVisibility(View.VISIBLE);
            Drawable drawable1 = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + dateBall.getD_pic(), new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    ImageView imageViewTag = (ImageView) listView.findViewWithTag(imageUrl+position);
                    if(imageViewTag!=null){
                        imageViewTag.setImageDrawable(imageDrawable);
                    }
                }
            });
            if(drawable1!=null){
                holder.mydateball_pic.setImageDrawable(drawable1);
            }else{
                holder.mydateball_pic.setImageResource(R.mipmap.preview);
            }
        }else{
            holder.mydateball_pic.setVisibility(View.GONE);
        }
        holder.mydateball_type.setText(BallApplication.getHobbyMap().get(dateBall.getD_type()+""));
        if(holder.mydateball_status.getText().toString().equals("已过期")){
            holder.mydateball_cancel_layout.setVisibility(View.GONE);
        }else{
            holder.mydateball_cancel_layout.setVisibility(View.VISIBLE);
            holder.mydateball_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setMessage("您是否取消")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteDateBall(dateBallList.get(position));
                                    dateBallList.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            });
        }
        return view;
    }

    class ViewHolder{
        RelativeLayout mydateball_layout;
        CircleImageView mydateball_user_pic;
        TextView mydateball_user_nickname;
        TextView mydateball_datetime;
        TextView mydateball_title;
        ImageView mydateball_pic;
        TextView mydateball_type;
        TextView mydateball_status;
        LinearLayout mydateball_cancel;
        RelativeLayout mydateball_cancel_layout;
    }

    private void deleteDateBall(final DateBall dateBall){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/DeleteDateBallServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("d_id",dateBall.getD_id()+"");
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }
}
