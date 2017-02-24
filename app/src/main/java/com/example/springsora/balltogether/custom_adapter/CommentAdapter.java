package com.example.springsora.balltogether.custom_adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.bean.Comment;
import com.example.springsora.balltogether.custom_view.CircleImageView;
import com.example.springsora.balltogether.utils.AsyncImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Springsora on 2016/5/9.
 */
public class CommentAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> comments;
    private ListView listView;
    private AsyncImageLoader asyncImageLoader;

    public CommentAdapter(Context context, List<Comment> comments, ListView listView) {
        this.context = context;
        this.comments = comments;
        this.listView = listView;
        asyncImageLoader = new AsyncImageLoader();
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.comment_listview_item,null);
            viewHolder = new ViewHolder();
            viewHolder.view_comment_userpic = (CircleImageView) view.findViewById(R.id.view_comment_userpic);
            viewHolder.view_comment_nicname = (TextView) view.findViewById(R.id.view_comment_nicname);
            viewHolder.view_comment_datetime = (TextView) view.findViewById(R.id.view_comment_datetime);
            viewHolder.view_comment_content = (TextView) view.findViewById(R.id.view_comment_content);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.view_comment_userpic.setTag(BallApplication.ServerUrl+comments.get(position).getUser().getU_pic()+position);
        Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + comments.get(position).getUser().getU_pic(), new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewTag = (ImageView) listView.findViewWithTag(BallApplication.ServerUrl+comments.get(position).getUser().getU_pic()+position);
                imageViewTag.setImageDrawable(imageDrawable);
            }
        });
        if(drawable!=null){
           viewHolder.view_comment_userpic.setImageDrawable(drawable);
        }else{
            viewHolder.view_comment_userpic.setImageResource(R.mipmap.default_logo);
        }
        if(viewHolder.view_comment_nicname!=null){
            viewHolder.view_comment_nicname.setText(comments.get(position).getUser().getU_nickname());
        }else{
            viewHolder.view_comment_nicname.setText(comments.get(position).getUser().getU_phone());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.view_comment_datetime.setText(simpleDateFormat.format(comments.get(position).getDatetime()));
        viewHolder.view_comment_content.setText(comments.get(position).getC_content());
        return view;
    }

    class ViewHolder{
        private CircleImageView view_comment_userpic;
        private TextView view_comment_nicname;
        private TextView view_comment_datetime;
        private TextView view_comment_content;
    }
}
