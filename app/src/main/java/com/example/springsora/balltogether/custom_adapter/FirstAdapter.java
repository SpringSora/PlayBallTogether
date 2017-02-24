package com.example.springsora.balltogether.custom_adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.bean.DateBall;
import com.example.springsora.balltogether.custom_listview.FirstListView;
import com.example.springsora.balltogether.custom_view.CircleImageView;
import com.example.springsora.balltogether.playballtogether.DateBallActivity;
import com.example.springsora.balltogether.playballtogether.DateBallDetailActivity;
import com.example.springsora.balltogether.playballtogether.MainActivity;
import com.example.springsora.balltogether.utils.AsyncImageLoader;
import com.example.springsora.balltogether.utils.BitmapUtils;
import com.example.springsora.balltogether.utils.HttpUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JJBOOM on 2016/4/21.
 */
public class FirstAdapter extends BaseAdapter {

    private List<DateBall> dateBalls;
    private Context context;
    private boolean flag;
    private DateBall dateBall1;
    private DateBall dateBall2;
    private AsyncImageLoader asyncImageLoader;
    private static final int LineItem = 2;
    private FirstListView firstListView;

    public FirstAdapter(List<DateBall> dateBalls,Context context,FirstListView firstListView) {
        this.dateBalls = dateBalls;
        this.context = context;
        this.firstListView = firstListView;
        asyncImageLoader = new AsyncImageLoader();
    }

    @Override
    public int getCount() {
        if(dateBalls.size()%LineItem==0){
            flag = false;
            return dateBalls.size()/LineItem;
        }else{
            flag = true;
            return dateBalls.size()/LineItem+1;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder viewHolder;
        if(convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.dateball_show,null);
            viewHolder = new ViewHolder();
            viewHolder.dateball_info1 = (LinearLayout) view.findViewById(R.id.dateball_info1);
            viewHolder.dateball_info2 = (LinearLayout) view.findViewById(R.id.dateball_info2);
            viewHolder.ViewNone = view.findViewById(R.id.ViewNone);
            viewHolder.dateball_user_pic = (CircleImageView) view.findViewById(R.id.dateball_user_pic);
            viewHolder.dateball_user_pic2 = (CircleImageView) view.findViewById(R.id.dateball_user_pic2);
            viewHolder.dateball_username = (TextView) view.findViewById(R.id.dateball_username);
            viewHolder.dateball_username2 = (TextView) view.findViewById(R.id.dateball_username2);
            viewHolder.locationText = (TextView) view.findViewById(R.id.locationText);
            viewHolder.locationText2 = (TextView) view.findViewById(R.id.locationText2);
            viewHolder.dateball_pic = (ImageView) view.findViewById(R.id.dateball_pic);
            viewHolder.dateball_pic2 = (ImageView) view.findViewById(R.id.dateball_pic2);
            viewHolder.dateball_title = (TextView) view.findViewById(R.id.dateball_title);
            viewHolder.dateball_title2 = (TextView) view.findViewById(R.id.dateball_title2);
            viewHolder.dateball_content = (TextView) view.findViewById(R.id.dateball_content);
            viewHolder.dateball_content2 = (TextView) view.findViewById(R.id.dateball_content2);
            viewHolder.dateball_type_img = (ImageView) view.findViewById(R.id.dateball_type_img);
            viewHolder.dateball_type_img2 = (ImageView) view.findViewById(R.id.dateball_type_img2);
            viewHolder.dateball_num = (TextView) view.findViewById(R.id.dateball_num);
            viewHolder.dateball_num2 = (TextView) view.findViewById(R.id.dateball_num2);
            viewHolder.dateball_date = (TextView) view.findViewById(R.id.dateball_date);
            viewHolder.dateball_date2 = (TextView) view.findViewById(R.id.dateball_date2);

            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
       // viewHolder.view.set


        if(flag){
            //如果数据为奇数
            if(position+1==getCount()){
                dateBall1 = dateBalls.get(position*LineItem);
                dateBall2 = null;
            }else{
                dateBall1 = dateBalls.get(position*LineItem);
                dateBall2 = dateBalls.get(position*LineItem+1);
            }
            UpdateView(viewHolder,position);
        }else{
            //如果数据为偶数
            dateBall1 = dateBalls.get(position*LineItem);
            dateBall2 = dateBalls.get(position*LineItem+1);
            UpdateView(viewHolder,position);
        }

        if(flag&&position+1==getCount()){
            viewHolder.dateball_info2.setVisibility(View.GONE);
            viewHolder.ViewNone.setVisibility(View.VISIBLE);
        }else{
            viewHolder.dateball_info2.setVisibility(View.VISIBLE);
            viewHolder.ViewNone.setVisibility(View.GONE);
        }
        buttonListener(viewHolder);
        return view;
    }

    class ViewHolder{
        LinearLayout dateball_info1;
        LinearLayout dateball_info2;
        View ViewNone;
        CircleImageView dateball_user_pic;
        TextView dateball_username;
        TextView locationText;
        ImageView dateball_pic;
        TextView dateball_title;
        TextView dateball_content;
        ImageView dateball_type_img;
        TextView dateball_num;
        TextView dateball_date;
        CircleImageView dateball_user_pic2;
        TextView dateball_username2;
        TextView locationText2;
        ImageView dateball_pic2;
        TextView dateball_title2;
        TextView dateball_content2;
        ImageView dateball_type_img2;
        TextView dateball_num2;
        TextView dateball_date2;
    }

    private void buttonListener(final ViewHolder viewHolder){
        viewHolder.dateball_info1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(context, DateBallDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DateBall", (Serializable) viewHolder.dateball_info1.getTag());
                        intent.putExtras(bundle);
                        ((MainActivity)context).startActivityForResult(intent, 1);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        });

        viewHolder.dateball_info2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(context, DateBallDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DateBall", (Serializable) viewHolder.dateball_info2.getTag());
                        intent.putExtras(bundle);
                        ((MainActivity)context).startActivityForResult(intent, 1);

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private void UpdateView(ViewHolder viewHolder,int position){
        //listview item第一列
        if(dateBall1!=null){
            viewHolder.dateball_info1.setTag(dateBall1);
            if(dateBall1.getUser().getU_nickname()!=null&&!dateBall1.getUser().getU_nickname().trim().equals("")){
                viewHolder.dateball_username.setText(dateBall1.getUser().getU_nickname());
            }else{
                viewHolder.dateball_username.setText(dateBall1.getUser().getU_phone());
            }

            viewHolder.locationText.setText(dateBall1.getD_location());
            if(dateBall1.getD_pic()!=null){
                viewHolder.dateball_pic.setVisibility(View.VISIBLE);
                viewHolder.dateball_pic.setTag(BallApplication.ServerUrl + dateBall1.getD_pic());
                Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + dateBall1.getD_pic(), new AsyncImageLoader.ImageCallback() {
                    @Override
                    public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                        ImageView imageViewTag = (ImageView) firstListView.findViewWithTag(imageUrl);
                        if(imageViewTag!=null){
                            imageViewTag.setImageDrawable(imageDrawable);
                        }
                    }
                });
                if(drawable==null){
                    viewHolder.dateball_pic.setImageResource(R.mipmap.ic_launcher);
                }else{
                    viewHolder.dateball_pic.setImageDrawable(drawable);
                }
            }else{
                viewHolder.dateball_pic.setVisibility(View.GONE);
            }
            viewHolder.dateball_title.setText(dateBall1.getD_title());
            viewHolder.dateball_content.setText(dateBall1.getD_text());
            viewHolder.dateball_type_img.setImageResource(BallApplication.getMipmapHobby().get(dateBall1.getD_type()));
            viewHolder.dateball_type_img.setTag(position * 2);
            viewHolder.dateball_num.setText(dateBall1.getD_num() + "人");

            viewHolder.dateball_date.setText(calculateTime(dateBall1.getD_time()));
            if(dateBall1.getUser().getU_pic()!=null){
                viewHolder.dateball_user_pic.setTag(BallApplication.ServerUrl + dateBall1.getUser().getU_pic());
                Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + dateBall1.getUser().getU_pic(), new AsyncImageLoader.ImageCallback() {
                    @Override
                    public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                        ImageView imageViewTag = (ImageView) firstListView.findViewWithTag(imageUrl);
                        if(imageViewTag!=null){
                            imageViewTag.setImageDrawable(imageDrawable);
                        }
                    }
                });
                if(drawable == null){
                    viewHolder.dateball_user_pic.setImageResource(R.mipmap.default_logo);
                }else{
                    viewHolder.dateball_user_pic.setImageDrawable(drawable);
                }
            }
            dateBall1 = null;
        }

        //listview item第二列
        if(dateBall2!=null){
            viewHolder.dateball_info2.setTag(dateBall2);
            if(dateBall2.getUser().getU_nickname()!=null&&!dateBall2.getUser().getU_nickname().trim().equals("")){
                viewHolder.dateball_username2.setText(dateBall2.getUser().getU_nickname());
            }else{
                viewHolder.dateball_username2.setText(dateBall2.getUser().getU_phone());
            }
            viewHolder.locationText2.setText(dateBall2.getD_location());
            if(dateBall2.getD_pic()!=null){
                viewHolder.dateball_pic2.setTag(BallApplication.ServerUrl + dateBall2.getD_pic());
                viewHolder.dateball_pic2.setVisibility(View.VISIBLE);
                Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + dateBall2.getD_pic(), new AsyncImageLoader.ImageCallback() {
                    @Override
                    public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                        if(dateBall2!=null){
                            ImageView imageViewTag = (ImageView) firstListView.findViewWithTag(BallApplication.ServerUrl+dateBall2.getD_pic());
                            if(imageViewTag!=null){
                                imageViewTag.setImageDrawable(imageDrawable);
                            }
                        }
                    }
                });
                if(drawable==null){
                    viewHolder.dateball_pic2.setImageResource(R.mipmap.ic_launcher);
                }else{
                    viewHolder.dateball_pic2.setImageDrawable(drawable);
                }
            }else{
                viewHolder.dateball_pic2.setVisibility(View.GONE);
            }
            viewHolder.dateball_title2.setText(dateBall2.getD_title());
            viewHolder.dateball_content2.setText(dateBall2.getD_text());
            viewHolder.dateball_type_img2.setTag(position * 2 + 1);
            viewHolder.dateball_type_img2.setImageResource(BallApplication.getMipmapHobby().get(dateBall2.getD_type()));
            viewHolder.dateball_num2.setText(dateBall2.getD_num() + "人");
            viewHolder.dateball_date2.setText(calculateTime(dateBall2.getD_time()));
            if(dateBall2.getUser().getU_pic() != null) {
                viewHolder.dateball_user_pic2.setTag(dateBall2.getUser().getU_pic());
                Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + dateBall2.getUser().getU_pic(), new AsyncImageLoader.ImageCallback() {
                    @Override
                    public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                        ImageView imageViewTag = (ImageView) firstListView.findViewWithTag(imageUrl);
                        if(imageViewTag!=null){
                            imageViewTag.setImageDrawable(imageDrawable);
                        }
                    }
                });
                if(drawable == null){
                    viewHolder.dateball_user_pic2.setImageResource(R.mipmap.default_logo);
                } else {
                    viewHolder.dateball_user_pic2.setImageDrawable(drawable);
                }
            }
            dateBall2 = null;
        }
    }

    private String calculateTime(Timestamp timestamp){

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String time = simpleDateFormat.format(timestamp);
            long millionSeconds = simpleDateFormat.parse(time).getTime();
            long currentTime = System.currentTimeMillis();
            Log.i("currenttime:",currentTime+"");
            Log.i("newtime:",millionSeconds+"");
            long calcuate = currentTime - millionSeconds;
            long day = calcuate/(1000*60*60*24);
            long hours = (calcuate/(60 * 60 * 1000) - day * 24);
            long minutes = (calcuate/(60 * 1000)) - day * 24 * 60 - hours * 60;
            long seconds = (calcuate/1000 - day * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60);
            if(day == 0){
                if(hours-12 == 0){
                    if(minutes == 0){
                        if(seconds == 0){

                        }else{
                            return "1分钟内";
                        }
                    }else{
                        return minutes + "分钟前";
                    }
                }else{
                    return  hours+"小时前";
                }

            }else{
                return day+"天前";
            }
            Log.i("calcuate",calcuate+"");
            Log.i("day:",day+"");
            Log.i("hours:",hours+"");
            Log.i("minutes:",minutes+"");
            Log.i("seconds:",seconds+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
