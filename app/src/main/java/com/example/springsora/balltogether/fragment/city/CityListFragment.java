package com.example.springsora.balltogether.fragment.city;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.CityModel;
import com.example.springsora.balltogether.bean.LocationService;
import com.example.springsora.balltogether.custom_adapter.CityAdapter;
import com.example.springsora.balltogether.playballtogether.SearchCityActivity;
import com.example.springsora.balltogether.sql.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Springsora on 2016/5/11.
 */
public class CityListFragment extends BaseFragment {
    private SQLiteDatabase database;
    private List<CityModel> mCityNames;
    private View view;
    private ImageView city_back;
    private EditText city_edit;
    private ListView city_listview;
    private TextView city_location;
    private RelativeLayout city_location_layout;
    private CityAdapter cityAdapter;
    private LocationService locationService;

    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        DBManager dbManager = new DBManager(getActivity());
        dbManager.openDateBase();
        dbManager.closeDatabase();
        database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
                + DBManager.DB_NAME, null);
        mCityNames = getCityNames();
        database.close();
        view = inflater.inflate(R.layout.citylist_fragment, null);
        city_back = (ImageView) view.findViewById(R.id.city_back);
        city_edit = (EditText) view.findViewById(R.id.city_edit);
        city_edit.setInputType(InputType.TYPE_NULL);
        city_edit.requestFocus();
        city_listview = (ListView) view.findViewById(R.id.city_listview);
        cityAdapter = new CityAdapter(mCityNames,getActivity());
        initHeader();
        city_listview.setAdapter(cityAdapter);
        locationService = BallApplication.getLocationService();
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
        return view;
    }

    private void initHeader(){
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.citylist_header,null);
        city_location = (TextView) header.findViewById(R.id.city_location);
        city_location_layout = (RelativeLayout) header.findViewById(R.id.city_location_layout);
        city_listview.addHeaderView(header,null,false);
    }

    private void buttonListener(){
        city_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        city_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BallApplication.setCity(mCityNames.get(position - 1).getCityName());
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });



        city_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchCityActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    /**
     * @return
     */
    private ArrayList<CityModel> getCityNames() {
        ArrayList<CityModel> names = new ArrayList<CityModel>();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM T_City ORDER BY NameSort", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            CityModel cityModel = new CityModel();
            cityModel.setCityName(cursor.getString(cursor
                    .getColumnIndex("CityName")));
            cityModel.setNameSort(cursor.getString(cursor
                    .getColumnIndex("NameSort")));
            names.add(cityModel);
        }
        cursor.close();
        return names;
    }
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                city_location.setText(location.getCity());
                city_location_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BallApplication.setCity(location.getCity());
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                });
                locationService.unregisterListener(mListener);
                locationService.stop();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(resultCode == Activity.RESULT_OK){
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
                break;
        }
    }
}
