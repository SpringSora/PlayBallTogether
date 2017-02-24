package com.example.springsora.balltogether.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.util.Base64;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.bean.LocationService;
import com.example.springsora.balltogether.bean.User;
import com.example.springsora.balltogether.sql.CityHelper;
import com.example.springsora.balltogether.sql.HistoryHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;


/**
 * Created by JJBOOM on 2016/4/21.
 */
public class BallApplication extends Application {
    /**
     * 网络队列
     */
    private static RequestQueue queues;
    /**
     * 服务器地址
     */
    public static final String ServerUrl = "http://192.168.191.1:8080/PlayBallTogether";
    /**
     * 中国区号
     */
    public static final String ChinaCode = "86";
    /**
     * 电话号码
     */
    public static String phonenum = "";
    /**
     * 用户对象
     */
    private static User user;
    /**
     * 是否注册
     */
    private static boolean isRegister;

    /**
     * 是否登陆
     */
    private static boolean isLogin = false;

    private static boolean isPublish = false;

    public static final int Modify_Nick_Tag = 0;

    public static final int Modify_Phone_Tag = 1;

    public static final int Modify_Password_Tag = 2;

    public static final int Modify_Sex_Tag = 3;

    public static final int Modify_Age_Tag = 4;

    public static final int Modify_Connect_Tag = 5;

    public static final int Modify_Sign_Tag = 6;

    public static final int Modify_Hobby_Tag = 7;


    public static final String Football = "足球";

    public static final String Basketball = "篮球";

    public static final String Pingpong = "乒乓球";

    public static final String Billiards = "台球";

    public static final String Badminton = "羽毛球";

    public static final String Tennis = "网球";

    public static final String Volleyball = "排球";

    private static HashMap<String,String> HobbyMap;

    private static Bitmap UserPic;

    private static LocationService locationService;

    public static final String DateBallItem = "4";

    public static final String BallGroundItem = "4";

    public static final String CommentItem = "10";

    public static final String UserItem = "10";

    public static final String PromiseItem = "3";

    private static String city;

    private static HashMap<Integer,Integer> mipmapHobby;

    private static double lng;

    private static double lat;

    public static HistoryHelper historyHelper;

    public static CityHelper cityHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        locationService = new LocationService(getApplicationContext());
        historyHelper = new HistoryHelper(getApplicationContext(),"History.db",null,1);
        cityHelper = new CityHelper(getApplicationContext(),"T_City.db",null,1);
        readUserToSharePref();
        HobbyMap = new HashMap<>();
        mipmapHobby = new HashMap<>();
        initHobbyMap();
        mipmapHobby();
        SDKInitializer.initialize(this);
    }
    public static RequestQueue getQueues() {
        return queues;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        BallApplication.user = user;
    }

    public static String getPhonenum() {
        return phonenum;
    }

    public static void setPhonenum(String phonenum) {
        BallApplication.phonenum = phonenum;
    }

    public static boolean isRegister() {
        return isRegister;
    }

    public static void setIsRegister(boolean isRegister) {
        BallApplication.isRegister = isRegister;
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        BallApplication.isLogin = isLogin;
    }

    public static boolean isPublish() {
        return isPublish;
    }

    public static void setIsPublish(boolean isPublish) {
        BallApplication.isPublish = isPublish;
    }

    public static HistoryHelper getHistoryHelper() {
        return historyHelper;
    }

    public static CityHelper getCityHelper() {
        return cityHelper;
    }

    private void initHobbyMap(){
        HobbyMap.put("1",Football);
        HobbyMap.put("2",Basketball);
        HobbyMap.put("3",Pingpong);
        HobbyMap.put("4",Billiards);
        HobbyMap.put("5",Badminton);
        HobbyMap.put("6",Tennis);
        HobbyMap.put("7",Volleyball);
    }

    private void mipmapHobby(){
        mipmapHobby.put(1, R.mipmap.football);
        mipmapHobby.put(2,R.mipmap.basketball);
        mipmapHobby.put(3,R.mipmap.pingpong);
        mipmapHobby.put(4,R.mipmap.billiard);
        mipmapHobby.put(5,R.mipmap.badminton);
        mipmapHobby.put(6,R.mipmap.tennis);
        mipmapHobby.put(7,R.mipmap.volleyball);
    }

    public static HashMap<String, String> getHobbyMap() {
        return HobbyMap;
    }

    public static Bitmap getUserPic() {
        return UserPic;
    }

    public static void setUserPic(Bitmap userPic) {
        UserPic = userPic;
    }

    public static LocationService getLocationService() {
        return locationService;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        BallApplication.city = city;
    }

    public static HashMap<Integer, Integer> getMipmapHobby() {
        return mipmapHobby;
    }

    public static double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        BallApplication.lat = lat;
    }

    public static double getLng() {
        return lng;
    }

    public static void setLng(double lng) {
        BallApplication.lng = lng;
    }

    private void readUserToSharePref(){
        SharedPreferences preferences = getSharedPreferences("base64", MODE_PRIVATE);
        String UserBase64 = preferences.getString("User","");
        if(UserBase64 == ""){
            return;
        }
        //读取字节
        byte[] base64 = Base64.decode(UserBase64.getBytes(), Base64.DEFAULT);
        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream ois = new ObjectInputStream(bais);
            //读取对象
            User user = (User) ois.readObject();
            BallApplication.setUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
