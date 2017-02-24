package com.example.springsora.balltogether.custom_adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.bean.BallGround;
import com.example.springsora.balltogether.custom_listview.SecondListView;
import com.example.springsora.balltogether.utils.AsyncImageLoader;
import com.example.springsora.balltogether.utils.Distance;

import java.util.List;

/**
 * Created by JJBOOM on 2016/5/5.
 */
public class SecondAdapter extends BaseAdapter {

    private Context context;
    private List<BallGround> ballGrounds;
    private SecondListView secondListView;
    private AsyncImageLoader asyncImageLoader;

    public SecondAdapter(List<BallGround> ballGrounds, Context context, SecondListView secondListView) {
        this.ballGrounds = ballGrounds;
        this.context = context;
        this.secondListView = secondListView;
        asyncImageLoader = new AsyncImageLoader();

    }

    @Override
    public int getCount() {
        return ballGrounds.size();
    }

    @Override
    public Object getItem(int position) {
        return ballGrounds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        View view;
        final BallGround ballGround = ballGrounds.get(position);
        if(convertView==null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.ballground_listview_item,null);
            viewHolder.ballground_pic = (ImageView) view.findViewById(R.id.ballground_pic);
            viewHolder.ballground_name = (TextView) view.findViewById(R.id.ballground_name);
            viewHolder.ballground_addr = (TextView) view.findViewById(R.id.ballground_addr);
            viewHolder.ballground_distance = (TextView) view.findViewById(R.id.ballground_distance);
            viewHolder.ballground_type = (TextView) view.findViewById(R.id.ballground_type);
            viewHolder.ballground_price = (TextView) view.findViewById(R.id.ballground_price);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if(ballGround.getBallGroundPic1Path()!=null){
            viewHolder.ballground_pic.setTag(BallApplication.ServerUrl +"/"+ ballGround.getBallGroundPic1Path());
            Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + "/"+ballGround.getBallGroundPic1Path(), new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    ImageView imageViewTag = (ImageView) secondListView.findViewWithTag(BallApplication.ServerUrl + "/"+ballGround.getBallGroundPic1Path());
                    imageViewTag.setImageDrawable(imageDrawable);
                }
            });
            if(drawable!=null){
                viewHolder.ballground_pic.setImageDrawable(drawable);
            }else{
                viewHolder.ballground_pic.setImageResource(R.mipmap.preview);
            }
        }else if(ballGround.getBallGroundPic2Path()!=null){
            viewHolder.ballground_pic.setTag(BallApplication.ServerUrl + "/"+ballGround.getBallGroundPic2Path());
            Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl +"/"+ ballGround.getBallGroundPic2Path(), new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    ImageView imageViewTag = (ImageView) secondListView.findViewWithTag(BallApplication.ServerUrl +"/"+ ballGround.getBallGroundPic2Path());
                    imageViewTag.setImageDrawable(imageDrawable);
                }
            });
            if(drawable!=null){
                viewHolder.ballground_pic.setImageDrawable(drawable);
            }else{
                viewHolder.ballground_pic.setImageResource(R.mipmap.preview);
            }
        }else if(ballGround.getBallGroundPic3Path()!=null){
            viewHolder.ballground_pic.setTag(BallApplication.ServerUrl +"/"+ ballGround.getBallGroundPic3Path());
            Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl +"/"+ ballGround.getBallGroundPic3Path(), new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    ImageView imageViewTag = (ImageView) secondListView.findViewWithTag(BallApplication.ServerUrl +"/"+ ballGround.getBallGroundPic3Path());
                    imageViewTag.setImageDrawable(imageDrawable);
                }
            });
            if(drawable!=null){
                viewHolder.ballground_pic.setImageDrawable(drawable);
            }else{
                viewHolder.ballground_pic.setImageResource(R.mipmap.preview);
            }
        }else{
            viewHolder.ballground_pic.setImageResource(R.mipmap.preview);
        }

        if(ballGround.getGroundName()!=null){
            viewHolder.ballground_name.setText(ballGround.getGroundName());
        }
        if(ballGround.getBallType()!=0){
            viewHolder.ballground_type.setText("球场类型："+BallApplication.getHobbyMap().get(String.valueOf(ballGround.getBallType())));
        }

        if(ballGround.getGroundPrice()!=0){
            viewHolder.ballground_price.setText(String.valueOf(ballGround.getGroundPrice())+"元/时");
        }
        int distance = (int) Distance.distance(BallApplication.getLng(), BallApplication.getLat(), ballGround.getLng(), ballGround.getLat());
        if(distance>1000){
            viewHolder.ballground_distance.setText(Float.valueOf(distance / 1000)+"km");
        }else if(distance<500){
            viewHolder.ballground_distance.setText("<500m");
        }
        if(ballGround.getAddress()!=null){
            viewHolder.ballground_addr.setText(ballGround.getAddress());
        }

        return view;
    }

    class ViewHolder{
        ImageView ballground_pic;
        TextView ballground_name;
        TextView ballground_addr;
        TextView ballground_type;
        TextView ballground_distance;
        TextView ballground_price;
    }

}
