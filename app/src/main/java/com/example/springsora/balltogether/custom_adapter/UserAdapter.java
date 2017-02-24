package com.example.springsora.balltogether.custom_adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.bean.User;
import com.example.springsora.balltogether.custom_view.CircleImageView;
import com.example.springsora.balltogether.playballtogether.ViewUserInfoActivity;
import com.example.springsora.balltogether.utils.AsyncImageLoader;

import java.util.List;

/**
 * Created by Springsora on 2016/5/10.
 */
public class UserAdapter extends BaseAdapter {

    private List<User> users;
    private Context context;
    private ListView listView;
    private AsyncImageLoader asyncImageLoader;

    public UserAdapter(Context context, ListView listView, List<User> users) {
        this.context = context;
        this.listView = listView;
        this.users = users;
        asyncImageLoader = new AsyncImageLoader();

    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder;
        final User user;
        user = users.get(position);
        if(convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.result_user_listview_item,null);
            holder = new ViewHolder();
            holder.result_user = (RelativeLayout) view.findViewById(R.id.result_user);
            holder.result_user_pic = (CircleImageView) view.findViewById(R.id.result_user_pic);
            holder.result_user_nickname = (TextView) view.findViewById(R.id.result_user_nickname);
            holder.result_user_sign = (TextView) view.findViewById(R.id.result_user_sign);
            holder.result_user_sex = (ImageView) view.findViewById(R.id.result_user_sex);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.result_user_pic.setTag(BallApplication.ServerUrl + user.getU_pic() + position);
        Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + user.getU_pic(), new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewTag = (ImageView) listView.findViewWithTag(imageUrl+position);
                if(imageViewTag!=null){
                    imageViewTag.setImageDrawable(imageDrawable);
                }
            }
        });
        if(drawable!=null){
            holder.result_user_pic.setImageDrawable(drawable);
        }else{
            holder.result_user_pic.setImageResource(R.mipmap.default_logo);
        }

        holder.result_user_nickname.setText(user.getU_nickname());
        holder.result_user_sign.setText(user.getSignature());
        if(user.getU_sex()!=null){
            if(user.getU_sex()=="1"){
                holder.result_user_sex.setImageResource(R.mipmap.boy);
            }else{
                holder.result_user_sex.setImageResource(R.mipmap.girl);
            }
        }else{
            holder.result_user_sex.setImageResource(R.mipmap.sex);
        }

        holder.result_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewUserInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("User",user);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return view;
    }
    class ViewHolder{
        RelativeLayout result_user;
        CircleImageView result_user_pic;
        TextView result_user_nickname;
        TextView result_user_sign;
        ImageView result_user_sex;
    }
}
