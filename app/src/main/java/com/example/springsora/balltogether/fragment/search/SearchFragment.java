package com.example.springsora.balltogether.fragment.search;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.custom_adapter.HistoryAdapter;
import com.example.springsora.balltogether.playballtogether.SearchResultActivity;
import com.example.springsora.balltogether.sql.HistoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Springsora on 2016/5/10.
 */
public class SearchFragment extends BaseFragment {
    private View view;
    private ImageView search_back;
    private EditText search_edit;
    private ListView search_listview;
    private List<String> contents;
    private List<Integer> ids;
    private HistoryAdapter historyAdapter;
    private LinearLayout clear_all_history;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.search_fragment,null);
        contents = new ArrayList<>();
        ids = new ArrayList<>();
        QueryHistory();
        Log.i("size",contents.size()+"");
        initControl();
        return view;
    }
    private void initControl(){
        search_back = (ImageView) view.findViewById(R.id.search_back);
        search_edit = (EditText) view.findViewById(R.id.search_edit);
        search_listview = (ListView) view.findViewById(R.id.search_listview);
        clear_all_history = (LinearLayout) view.findViewById(R.id.clear_all_history);
        if(contents.size()!=0){
            clear_all_history.setVisibility(View.VISIBLE);
        }else{
            clear_all_history.setVisibility(View.GONE);
        }
        historyAdapter = new HistoryAdapter(contents,ids,getActivity(),clear_all_history);
        search_listview.setAdapter(historyAdapter);
    }

    private void buttonListener(){

        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        search_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("intent", "OK");
                    SQLiteDatabase db = BallApplication.getHistoryHelper().getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("content", search_edit.getText().toString());
                    db.insert("history", null, values);
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("content", search_edit.getText().toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                }
                return false;
            }
        });

        clear_all_history.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_CANCEL:
                        clear_all_history.setBackgroundResource(R.color.colorWhite);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        clear_all_history.setBackgroundResource(R.color.colorHint);
                        break;
                    case MotionEvent.ACTION_UP:
                        clear_all_history.setBackgroundResource(R.color.colorWhite);
                        break;
                }
                return true;
            }
        });

        search_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("content", contents.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void QueryHistory(){
        SQLiteDatabase db = BallApplication.getHistoryHelper().getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from history order by id desc", null);
        while (cursor.moveToNext()){
            ids.add(cursor.getInt(0));
            contents.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        contents.clear();
        ids.clear();
        historyAdapter.notifyDataSetChanged();
        SQLiteDatabase db = BallApplication.getHistoryHelper().getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from history order by id desc", null);
        while (cursor.moveToNext()){
            contents.add(cursor.getString(1));
            ids.add(cursor.getInt(0));
        }
        historyAdapter.notifyDataSetChanged();
        cursor.close();
        db.close();
        search_edit.setText("");
        if(contents.size()!=0){
            clear_all_history.setVisibility(View.VISIBLE);
        }else{
            clear_all_history.setVisibility(View.GONE);
        }
    }
}
