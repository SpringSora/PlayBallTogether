package com.example.springsora.balltogether.fragment.search;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.CityModel;
import com.example.springsora.balltogether.custom_adapter.CityAdapter;
import com.example.springsora.balltogether.sql.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Springsora on 2016/5/12.
 */
public class SearchCityFragment extends BaseFragment {

    private View view;
    private ImageView search_city_back;
    private EditText search_city_edit;
    private ListView search_city_listview;
    private List<CityModel> cityModels;
    private SQLiteDatabase database;
    private CityAdapter cityAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.search_city_fragment,null);
        search_city_back = (ImageView) view.findViewById(R.id.search_city_back);
        search_city_edit = (EditText) view.findViewById(R.id.search_city_edit);
        search_city_listview = (ListView) view.findViewById(R.id.search_city_listview);
        database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
        cityModels = new ArrayList<>();
        cityAdapter = new CityAdapter(cityModels,getActivity());
        search_city_listview.setAdapter(cityAdapter);
        return view;
    }

    private void  buttonListener(){

        search_city_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        search_city_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String curCharacter = s.toString().trim();
                if (!TextUtils.isEmpty(curCharacter)) {
                    getCityNames(curCharacter);
                }
            }
        });

        search_city_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BallApplication.setCity(cityModels.get(position).getCityName());
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
    }

    private void getCityNames(String string)
    {
        Cursor cursor = database.rawQuery("SELECT CityName FROM T_City where CityName like '%"+string+"%' ORDER BY NameSort", null);
        cityModels.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            CityModel cityModel = new CityModel();
            cityModel.setCityName(cursor.getString(cursor.getColumnIndex("CityName")));
            cityModels.add(cityModel);
        }
        cityAdapter.notifyDataSetChanged();
    }

}
