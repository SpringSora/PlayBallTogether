package com.example.springsora.balltogether.fragment.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.base.BasePager;
import com.example.springsora.balltogether.bean.LocationService;
import com.example.springsora.balltogether.custom_adapter.MainAdapter;
import com.example.springsora.balltogether.custom_viewpager.LazyViewPager;
import com.example.springsora.balltogether.custom_viewpager.MyViewPager;
import com.example.springsora.balltogether.pager.main.FirstPager;
import com.example.springsora.balltogether.pager.main.FourthPager;
import com.example.springsora.balltogether.pager.main.SecondPager;
import com.example.springsora.balltogether.pager.main.ThirdPager;
import com.example.springsora.balltogether.playballtogether.CityListActivity;
import com.example.springsora.balltogether.playballtogether.DateBallActivity;
import com.example.springsora.balltogether.playballtogether.LoginActivity;
import com.example.springsora.balltogether.playballtogether.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JJBOOM on 2016/4/20.
 */
public class FirstFragment extends BaseFragment {

    private static final String TAG = "FirstFragment";
    /**
     *  4个页面的集合
     */
    private List<BasePager> pagers;
    /**
     * 第一个页面
     */
    private FirstPager firstPager;
    /**
     * 第二个页面
     */
    private SecondPager secondPager;
    /**
     * 第三个页面
     */
    private ThirdPager thirdPager;
    /**
     * 第四个页面
     */
    private FourthPager fourthPager;
    /**
     * 自定义ViewPager
     */
    private MyViewPager MainViewPager;
    /**
     * 菜单
     */
    private RadioGroup radioGroup;
    /**
     * 左上角按钮
     */
    private LinearLayout leftLayout;
    /**
     * 右上角按钮
     */
    private LinearLayout rightLayout;
    /**
     * 中间按钮
     */
    private RelativeLayout centerLayout;
    /**
     * 搜索输入框
     */
    private EditText searchEdit;
    /**
     * 标题
     */
    private RelativeLayout mToolbar;
    /**
     * 左上角文字
     */
    private TextView leftText;
    /**
     * 左上角图片
     */
    private ImageView leftImage;
    /**
     * 右上角图片
     */
    private ImageView rightImage;

    /**
     * 加载的页面
     */
    private View view;
    /**
     * 适配器；
     */
    private MainAdapter mainAdapter;

    private RadioButton fourth_menu;

    private LocationService locationService;

    private String city;

    private Spinner Screen_ball;



    @Override
    public void initData(Bundle savedInstanceState) {
        MainViewPager.setAdapter(mainAdapter);
        ButtonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view =  inflater.inflate(R.layout.fragment_first,null);
        initControl();
        initList();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView:");
        locationService = BallApplication.getLocationService();
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * 初始化页面
     */
    private void  initList(){

        //初始化页面集合
        pagers = new ArrayList<>();
        //初始化各个页面
        firstPager = new FirstPager(getActivity());
        secondPager = new SecondPager(getActivity());
        thirdPager = new ThirdPager(getActivity());
        fourthPager = new FourthPager(getActivity());
        //将四个页面添加到页面集合中
        pagers.add(firstPager);
        pagers.add(secondPager);
        pagers.add(thirdPager);
        pagers.add(fourthPager);
        mainAdapter = new MainAdapter(pagers);
    }

    /**
     * 初始化控件
     */
    private void initControl(){
        MainViewPager = (MyViewPager)view.findViewById(R.id.my_custom_viewpager);
        radioGroup = (RadioGroup) view.findViewById(R.id.menu_radio);
        leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
        rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
        centerLayout = (RelativeLayout) view.findViewById(R.id.center_layout);
        searchEdit = (EditText) view.findViewById(R.id.main_search_text);
        searchEdit.setInputType(InputType.TYPE_NULL);
        searchEdit.requestFocus();
        mToolbar = (RelativeLayout) view.findViewById(R.id.title_main_include);
        leftText = (TextView) view.findViewById(R.id.left_text);
        leftImage = (ImageView) view.findViewById(R.id.left_image);
        rightImage = (ImageView) view.findViewById(R.id.right_image);
        fourth_menu = (RadioButton) view.findViewById(R.id.menu_fourth);
        Screen_ball = (Spinner) view.findViewById(R.id.Screen_ball);

    }



    /**
     * 菜单监听事件
     */

    private void ButtonListener(){

        MainViewPager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mToolbar.setBackgroundResource(R.color.colorTitle);
                        leftText.setTextSize(15);
                        centerLayout.setVisibility(View.VISIBLE);
                        rightLayout.setVisibility(View.VISIBLE);
                        leftImage.setVisibility(View.VISIBLE);
                        leftText.setTextColor(Color.WHITE);
                        Screen_ball.setVisibility(View.GONE);
                        firstPager.startPlay();
                        if (BallApplication.getCity() == null) {
                            leftText.setText(R.string.search_city);
                        } else {
                            leftText.setText(BallApplication.getCity());
                        }
                        leftLayout.setClickable(true);
                        break;
                    case 1:
                        leftLayout.setClickable(false);
                        mToolbar.setBackgroundResource(R.color.colorSubtitle);
                        centerLayout.setVisibility(View.INVISIBLE);
                        rightLayout.setVisibility(View.INVISIBLE);
                        leftImage.setVisibility(View.GONE);
                        leftText.setTextSize(18);
                        leftText.setTextColor(Color.BLACK);
                        leftText.setText(R.string.menu_second_text);
                        Screen_ball.setVisibility(View.VISIBLE);
                        firstPager.stopPlay();
                        break;
                    case 2:
                        leftLayout.setClickable(false);
                        mToolbar.setBackgroundResource(R.color.colorSubtitle);
                        centerLayout.setVisibility(View.INVISIBLE);
                        rightLayout.setVisibility(View.INVISIBLE);
                        leftImage.setVisibility(View.GONE);
                        leftText.setTextSize(18);
                        leftText.setTextColor(Color.BLACK);
                        leftText.setText(R.string.menu_third_text);
                        Screen_ball.setVisibility(View.GONE);
                        firstPager.stopPlay();
                        break;
                    case 3:
                        leftLayout.setClickable(false);
                        mToolbar.setBackgroundResource(R.color.colorSubtitle);
                        centerLayout.setVisibility(View.INVISIBLE);
                        rightLayout.setVisibility(View.INVISIBLE);
                        leftImage.setVisibility(View.GONE);
                        leftText.setTextSize(18);
                        leftText.setTextColor(Color.BLACK);
                        leftText.setText(R.string.menu_fourth_text);
                        Screen_ball.setVisibility(View.GONE);
                        firstPager.stopPlay();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // titleChange(checkedId);
                switch (checkedId) {
                    case R.id.menu_first:
                        MainViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.menu_second:
                        MainViewPager.setCurrentItem(1, false);
                        break;
                    /*case R.id.menu_third:
                        MainViewPager.setCurrentItem(2, false);
                        break;*/
                    case R.id.menu_fourth:
                        MainViewPager.setCurrentItem(3, false);
                        break;
                }
            }
        });
            leftLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CityListActivity.class);
                    startActivityForResult(intent,5);
                }
            });

        rightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BallApplication.getUser() == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(getActivity(), DateBallActivity.class);
                    getActivity().startActivityForResult(intent, 1);
                }
            }
            });

        searchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult");
        switch (requestCode){
            case 5:
                if(resultCode == Activity.RESULT_OK){
                    leftText.setText(BallApplication.getCity());
                    firstPager.getFirstRefreshLoadLayout()
                            .setRefreshing(true);
                    firstPager.PublishAfterRefresh();
                    secondPager.getSecond_refresh()
                            .setRefreshing(true);
                    secondPager.AfterRefresh();
                }
                break;
        }
    }


    /*    private void readUserToSharePref(){
        SharedPreferences preferences = getActivity().getSharedPreferences("base64",getActivity().MODE_PRIVATE);
        String UserBase64 = preferences.getString("User","");
        if(UserBase64 == ""){
            Log.i(TAG,"readFalse");
            return;
        }
        //读取字节
        byte[] base64 = Base64.decode(UserBase64.getBytes(),Base64.DEFAULT);
        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream ois = new ObjectInputStream(bais);
            //读取对象
            BallApplication.setUser((User)ois.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }*/

    public FourthPager getFourthPager() {
        return fourthPager;
    }

    public FirstPager getFirstPager() {
        return firstPager;
    }

    /**
     * 更新页面
     * @param position 第一个页面为0
     */
    public void UpdatePager(int position){
        switch (position){
            case 0:
                pagers.remove(3);
                pagers.add(new FourthPager(getActivity()));
                mainAdapter.notifyDataSetChanged();
                MainViewPager.setCurrentItem(position, false);
                break;
            case 1:
                pagers.remove(3);
                pagers.add(new FourthPager(getActivity()));
                mainAdapter.notifyDataSetChanged();
                MainViewPager.setCurrentItem(position, false);
                break;
            case 2:
                pagers.remove(3);
                pagers.add(new FourthPager(getActivity()));
                mainAdapter.notifyDataSetChanged();
                MainViewPager.setCurrentItem(position, false);
                break;
            case 3:
                pagers.remove(position);
                pagers.add(new FourthPager(getActivity()));
                mainAdapter.notifyDataSetChanged();
                MainViewPager.setCurrentItem(position - 1, false);
                MainViewPager.setCurrentItem(position, false);
                break;
        }
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                if(location.getCity()!=null&&!location.getCity().trim().equals("")){
                    leftText.setText(location.getCity());
                    BallApplication.setCity(location.getCity());
                    BallApplication.setLng(location.getLongitude());
                    BallApplication.setLat(location.getLatitude());
                    firstPager.getFirstRefreshLoadLayout().setRefreshing(true);
                    firstPager.PublishAfterRefresh();
                    secondPager.getSecond_refresh().setRefreshing(true);
                    secondPager.AfterRefresh();
                    locationService.unregisterListener(mListener);
                    locationService.stop();
                }else{
                    leftText.setText("定位中");
                }
            }
        }
    };

    public Spinner getScreen_ball() {
        return Screen_ball;
    }
}
