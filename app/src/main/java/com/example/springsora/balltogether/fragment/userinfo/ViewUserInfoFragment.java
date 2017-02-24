package com.example.springsora.balltogether.fragment.userinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.User;
import com.example.springsora.balltogether.custom_adapter.UserInfoAdapter;
import com.example.springsora.balltogether.custom_view.CircleImageView;
import com.example.springsora.balltogether.playballtogether.ModifyActivity;
import com.example.springsora.balltogether.utils.AsyncImageLoader;
import com.example.springsora.balltogether.utils.BitmapUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JJBOOM on 2016/5/5.
 */
public class ViewUserInfoFragment extends BaseFragment {
    /**
     * 加载的页面
     */
    private View view;

    /**
     *返回键
     */

    private ImageView back;

    /**
     * 用户名
     */

    private CircleImageView user_info_pic;

    private TextView user_info_nickname1;

    private ListView user_info_list_view;

    private TextView user_info_nickname2;

    private TextView user_info_phone;

    private TextView user_info_sex;

    private TextView user_info_age;

    private TextView user_info_connect;

    private TextView user_info_hobby;


    private TextView user_info_signature_text;


    private User user;

    private ImageView user_info_back;


    @Override
    public void initData(Bundle savedInstanceState) {
        user_info_list_view.setAdapter(new UserInfoAdapter());
        user_info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.userinfo_fragment,null);
        initControl();
        FirstHeader();
        SecondHeader();
        return view;
    }

    private void initControl(){
        user_info_list_view = (ListView) view.findViewById(R.id.user_info_list_view);
        user = (User) getArguments().getSerializable("User");
    }


    /**
     * 第一头
     */
    private void FirstHeader(){
        View FirstView = LayoutInflater.from(getActivity()).inflate(R.layout.userinfo_listview_view1,null);
        user_info_pic = (CircleImageView) FirstView.findViewById(R.id.user_info_pic);
        user_info_nickname1 = (TextView) FirstView.findViewById(R.id.user_info_nickname1);
        back = (ImageView) FirstView.findViewById(R.id.user_info_back);
        user_info_back = (ImageView) FirstView.findViewById(R.id.user_info_back);

        if(user.getU_pic()!=null){
            //user_info_pic.setImageBitmap(BallApplication.getUserPic());
            user_info_pic.setTag(BallApplication.ServerUrl + user.getU_pic());
            Drawable drawable = new AsyncImageLoader().loadDrawable(BallApplication.ServerUrl + user.getU_pic(), new AsyncImageLoader.ImageCallback() {
                @Override
                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                    user_info_pic.setImageDrawable(imageDrawable);
                }
            });
            if(drawable!=null){
                user_info_pic.setImageDrawable(drawable);
            }else{
                user_info_pic.setImageResource(R.mipmap.default_logo);
            }
        }
        if(user.getU_nickname()==null){
            user_info_nickname1.setText(user.getU_phone());
        }else{
            user_info_nickname1.setText(user.getU_nickname());
        }
        user_info_signature_text = (TextView) FirstView.findViewById(R.id.user_info_signature_text);
        if(user.getSignature()==null){
            user_info_signature_text.setText("简介：无");
        }else{
            user_info_signature_text.setText("简介："+user.getSignature());
        }


        user_info_list_view.addHeaderView(FirstView);
    }

    /**
     * 第二个头
     */
    private void SecondHeader(){
        View SecondView = LayoutInflater.from(getActivity()).inflate(R.layout.viewuserinfo_listview_view2,null);
        //初始化
        user_info_nickname2 = (TextView) SecondView.findViewById(R.id.user_info_nickname2);
        if(user.getU_nickname()==null){
            user_info_nickname2.setText("未设置");
        }else{
            user_info_nickname2.setText((user.getU_nickname()));
        }
        //初始化
        user_info_phone = (TextView) SecondView.findViewById(R.id.user_info_phone);
        String phone = user.getU_phone();
        char[] c = phone.toCharArray();
        c[3]='*';c[4]='*';c[5]='*';c[6]='*';
        user_info_phone.setText(new String(c));
        //初始化
        user_info_sex = (TextView) SecondView.findViewById(R.id.user_info_sex);
        if(user.getU_sex()==null){
            user_info_sex.setText("未设置");
        }else{
            if("1".equals(user.getU_sex())){
                user_info_sex.setText("男性");
            }else{
                user_info_sex.setText("女性");
            }
        }
        //初始化
        user_info_age = (TextView) SecondView.findViewById(R.id.user_info_age);
        if(user.getU_age()!=null&&user.getU_age()!=0){
            user_info_age.setText(user.getU_age()+"");
        }else{
            user_info_age.setText("未设置");
        }
        //初始化
        user_info_connect = (TextView) SecondView.findViewById(R.id.user_info_connect);
        if(user.getU_name()!=null&&!user.getU_name().trim().equals("")){
            user_info_connect.setText(user.getU_name());
        }else{
            user_info_connect.setText("未设置");
        }
        //
        user_info_hobby = (TextView) SecondView.findViewById(R.id.user_info_hobby);
        if(user.getU_playtype()!=null&&!user.getU_playtype().trim().equals("")){
            char[] type = user.getU_playtype().toCharArray();
            StringBuilder ball = new StringBuilder();
            for(int i = 0;i<type.length;i++){
                if (type[i]=='1'){
                    ball.append(BallApplication.getHobbyMap().get((i + 1) + "")).append("、");
                }
            }
            ball.delete(ball.length()-1,ball.length());
            user_info_hobby.setText(ball);
        }else{
            user_info_hobby.setText("未设置");
        }
        user_info_list_view.addHeaderView(SecondView);
    }
}
