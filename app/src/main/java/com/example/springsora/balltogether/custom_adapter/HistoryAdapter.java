package com.example.springsora.balltogether.custom_adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;

import java.util.List;

/**
 * Created by Springsora on 2016/5/10.
 */
public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<String> contents;
    private List<Integer> ids;
    private LinearLayout clearall;

    public HistoryAdapter(List<String> contents,List<Integer> ids, Context context,LinearLayout clearall) {
        this.contents = contents;
        this.ids = ids;
        this.context = context;
        this.clearall = clearall;
    }

    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public Object getItem(int position) {
        return contents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.history_list_view_item,null);
            viewHolder = new ViewHolder();
            viewHolder.history_clear = (ImageView) view.findViewById(R.id.history_clear);
            viewHolder.history_key = (TextView) view.findViewById(R.id.history_key);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.history_key.setText(contents.get(position));
        viewHolder.history_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = BallApplication.getHistoryHelper().getWritableDatabase();
                db.delete("history", "id=?", new String[]{ids.get(position) + ""});
                db.close();
                contents.remove(position);

                notifyDataSetChanged();
                if(contents.size()!=0){
                    clearall.setVisibility(View.VISIBLE);
                }else{
                    clearall.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }
    class ViewHolder{
        TextView history_key;
        ImageView history_clear;
    }
}
