package com.example.springsora.balltogether.custom_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.bean.CityModel;

import java.util.List;

/**
 * Created by Springsora on 2016/5/11.
 */
public class CityAdapter extends BaseAdapter {

    private List<CityModel> cityModels;
    private Context context;

    public CityAdapter(List<CityModel> cityModels, Context context) {
        this.cityModels = cityModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cityModels.size();
    }

    @Override
    public Object getItem(int position) {
        return cityModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.citylist_item,null);
            holder.city_name = (TextView) view.findViewById(R.id.city_name);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.city_name.setText(cityModels.get(position).getCityName());
        return view;
    }

    class ViewHolder{
        TextView city_name;
    }
}
