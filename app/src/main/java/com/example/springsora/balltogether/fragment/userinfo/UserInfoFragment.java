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
import android.graphics.Matrix;
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
import com.example.springsora.balltogether.utils.BitmapUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JJBOOM on 2016/4/22.
 */
public class UserInfoFragment extends BaseFragment {
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
    //private TextView user_name;

    private CircleImageView user_info_pic;

    private TextView user_info_nickname1;

    private ListView user_info_list_view;

    private TextView user_info_nickname2;

    private TextView user_info_phone;

    private TextView user_info_sex;

    private TextView user_info_age;

    private TextView user_info_connect;

    private TextView user_info_hobby;

    private RelativeLayout user_info_signature;

    private TextView user_info_signature_text;

    private RelativeLayout modify_nickname;
    private RelativeLayout modify_phone;
    private RelativeLayout modify_pw;
    private RelativeLayout modify_sex;
    private RelativeLayout modify_age;
    private RelativeLayout modify_connect;
    private RelativeLayout modify_hobby;

    private Intent intent;

    private RelativeLayout logout_button;


    private AlertDialog.Builder builder;

    private Uri imageUri;

    private Bitmap bmp;

    private String photo;

    private ProgressDialog progressDialog;


    /**
     * 数据存储
     */
    private SharedPreferences pref;
    /**
     * 数据存储编辑
     */
    private SharedPreferences.Editor editor;


    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
        user_info_list_view.setAdapter(new UserInfoAdapter());
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.userinfo_fragment,null);
        initControl();
        FirstHeader();
        SecondHeader();
        ThirdHeader();
        return view;
    }

    private void initControl(){
        user_info_list_view = (ListView) view.findViewById(R.id.user_info_list_view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("您是否退出当前账号");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClearSharePref();
                Intent intent = new Intent();
                intent.putExtra("User","OK");
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        intent = new Intent(getActivity(), ModifyActivity.class);
    }

    private void buttonListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        user_info_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("选择上传方式")
                        .setItems(new String[]{"相册","拍摄"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent1.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent1.setType("image/*");
                                        startActivityForResult(intent1,10);
                                        break;
                                    case 1:
                                        File outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
                                        try {
                                            if(outputImage.exists()){
                                                outputImage.delete();
                                            }
                                            outputImage.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        imageUri = Uri.fromFile(outputImage);
                                        Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
                                        intent2.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                        startActivityForResult(intent2,11);
                                        break;
                                }
                            }
                        }).show();
            }
        });

        user_info_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Control", R.id.user_info_signature);
                startActivityForResult(intent, R.id.user_info_signature);
            }
        });

        modify_nickname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_nickname.setBackgroundResource(R.color.colorBody);
                        intent.putExtra("Control", R.id.modify_nickname);
                        startActivityForResult(intent, R.id.modify_nickname);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_nickname.setBackgroundResource(R.color.colorWhite);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_nickname.setBackgroundResource(R.color.colorWhite);
                        break;
                }
                return true;
            }
        });


        modify_phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_phone.setBackgroundResource(R.color.colorBody);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_phone.setBackgroundResource(R.color.colorWhite);
                        intent.putExtra("Control", R.id.modify_phone);
                        startActivityForResult(intent, R.id.modify_phone);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_phone.setBackgroundResource(R.color.colorWhite);
                        break;

                }
                return true;
            }
        });

        modify_pw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_pw.setBackgroundResource(R.color.colorBody);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_pw.setBackgroundResource(R.color.colorWhite);
                        intent.putExtra("Control", R.id.modify_pw);
                        startActivityForResult(intent, R.id.modify_pw);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_pw.setBackgroundResource(R.color.colorWhite);
                        break;
                }
                return true;
            }
        });

        modify_sex.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_sex.setBackgroundResource(R.color.colorBody);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_sex.setBackgroundResource(R.color.colorWhite);
                        intent.putExtra("Control", R.id.modify_sex);
                        startActivityForResult(intent, R.id.modify_sex);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_sex.setBackgroundResource(R.color.colorWhite);
                        break;
                }
                return true;
            }
        });

        modify_age.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_age.setBackgroundResource(R.color.colorBody);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_age.setBackgroundResource(R.color.colorWhite);
                        intent.putExtra("Control", R.id.modify_age);
                        startActivityForResult(intent, R.id.modify_age);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_age.setBackgroundResource(R.color.colorWhite);
                        break;
                }
                return true;
            }
        });

        modify_connect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_connect.setBackgroundResource(R.color.colorBody);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_connect.setBackgroundResource(R.color.colorWhite);
                        intent.putExtra("Control", R.id.modify_connect);
                        startActivityForResult(intent, R.id.modify_connect);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_connect.setBackgroundResource(R.color.colorWhite);
                        break;
                }
                return true;
            }
        });
        modify_hobby.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        modify_hobby.setBackgroundResource(R.color.colorBody);
                        break;
                    case MotionEvent.ACTION_UP:
                        modify_hobby.setBackgroundResource(R.color.colorWhite);
                        intent.putExtra("Control", R.id.modify_hobby);
                        startActivityForResult(intent, R.id.modify_hobby);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        modify_hobby.setBackgroundResource(R.color.colorWhite);
                        break;
                }
                return true;
            }
        });

        logout_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        logout_button.setBackgroundResource(R.color.colorWarningPress);
                        break;
                    case MotionEvent.ACTION_UP:
                        logout_button.setBackgroundResource(R.color.colorWarning);
                        builder.show();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        logout_button.setBackgroundResource(R.color.colorWarning);
                        break;
                }
                return true;
            }
        });

    }

    /**
     * 第一头
     */
    private void FirstHeader(){
        View FirstView = LayoutInflater.from(getActivity()).inflate(R.layout.userinfo_listview_view1,null);
        user_info_pic = (CircleImageView) FirstView.findViewById(R.id.user_info_pic);
        user_info_nickname1 = (TextView) FirstView.findViewById(R.id.user_info_nickname1);
        back = (ImageView) FirstView.findViewById(R.id.user_info_back);

        if(BallApplication.getUser().getU_pic()!=null){
            if(BallApplication.getUserPic()!=null){
                user_info_pic.setImageBitmap(BallApplication.getUserPic());
            }
        }
        if(BallApplication.getUser().getU_nickname()==null){
            user_info_nickname1.setText(BallApplication.getUser().getU_phone());
        }else{
            user_info_nickname1.setText(BallApplication.getUser().getU_nickname());
        }
        user_info_signature = (RelativeLayout) FirstView.findViewById(R.id.user_info_signature);
        user_info_signature_text = (TextView) FirstView.findViewById(R.id.user_info_signature_text);
        if(BallApplication.getUser().getSignature()==null){
            user_info_signature_text.setText("简介：点击编辑");
        }else{
            user_info_signature_text.setText("简介："+BallApplication.getUser().getSignature());
        }


        user_info_list_view.addHeaderView(FirstView);
    }

    /**
     * 第二个头
     */
    private void SecondHeader(){
        View SecondView = LayoutInflater.from(getActivity()).inflate(R.layout.userinfo_listview_view2,null);
        //初始化
        user_info_nickname2 = (TextView) SecondView.findViewById(R.id.user_info_nickname2);
        if(BallApplication.getUser().getU_nickname()==null){
            user_info_nickname2.setText("未设置");
        }else{
            user_info_nickname2.setText((BallApplication.getUser().getU_nickname()));
        }
        //初始化
        user_info_phone = (TextView) SecondView.findViewById(R.id.user_info_phone);
        String phone = BallApplication.getUser().getU_phone();
        char[] c = phone.toCharArray();
        c[3]='*';c[4]='*';c[5]='*';c[6]='*';
        user_info_phone.setText(new String(c));
        //初始化
        user_info_sex = (TextView) SecondView.findViewById(R.id.user_info_sex);
        if(BallApplication.getUser().getU_sex()==null){
            user_info_sex.setText("未设置");
        }else{
            if("1".equals(BallApplication.getUser().getU_sex())){
                user_info_sex.setText("男性");
            }else{
                user_info_sex.setText("女性");
            }
        }
        //初始化
        user_info_age = (TextView) SecondView.findViewById(R.id.user_info_age);
        if(BallApplication.getUser().getU_age()!=null&&BallApplication.getUser().getU_age()!=0){
            user_info_age.setText(BallApplication.getUser().getU_age()+"");
        }else{
            user_info_age.setText("未设置");
        }
        //初始化
        user_info_connect = (TextView) SecondView.findViewById(R.id.user_info_connect);
        if(BallApplication.getUser().getU_name()!=null&&!BallApplication.getUser().getU_name().trim().equals("")){
            user_info_connect.setText(BallApplication.getUser().getU_name());
        }else{
            user_info_connect.setText("未设置");
        }
        //
        user_info_hobby = (TextView) SecondView.findViewById(R.id.user_info_hobby);
        if(BallApplication.getUser().getU_playtype()!=null&&!BallApplication.getUser().getU_playtype().trim().equals("")){
            char[] type = BallApplication.getUser().getU_playtype().toCharArray();
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


        //初始化
        modify_nickname = (RelativeLayout) SecondView.findViewById(R.id.modify_nickname);
        modify_phone = (RelativeLayout) SecondView.findViewById(R.id.modify_phone);
        modify_pw = (RelativeLayout) SecondView.findViewById(R.id.modify_pw);
        modify_sex = (RelativeLayout) SecondView.findViewById(R.id.modify_sex);
        modify_age = (RelativeLayout) SecondView.findViewById(R.id.modify_age);
        modify_connect = (RelativeLayout) SecondView.findViewById(R.id.modify_connect);
        modify_hobby = (RelativeLayout) SecondView.findViewById(R.id.modify_hobby);

        user_info_list_view.addHeaderView(SecondView);
    }

    private void ThirdHeader(){
        View ThirdView =  LayoutInflater.from(getActivity()).inflate(R.layout.userinfo_listview_view3,null);
        logout_button = (RelativeLayout) ThirdView.findViewById(R.id.logout_button);
        user_info_list_view.addHeaderView(ThirdView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case R.id.modify_nickname:
                if(BallApplication.getUser().getU_nickname()!=null){
                    user_info_nickname2.setText(BallApplication.getUser().getU_nickname());
                    user_info_nickname1.setText(BallApplication.getUser().getU_nickname());
                }
                break;
            case R.id.modify_phone:
                if(BallApplication.getUser().getU_phone()!=null){
                    char[] c = BallApplication.getUser().getU_phone().toCharArray();
                    c[3]='*';c[4]='*';c[5]='*';c[6]='*';
                    user_info_phone.setText(new String(c));
                }
                break;
            case R.id.modify_pw:
                break;
            case R.id.modify_sex:
                if(BallApplication.getUser().getU_sex()!=null){
                    if("1".equals(BallApplication.getUser().getU_sex())){
                        user_info_sex.setText("男性");
                    }else {
                        user_info_sex.setText("女性");
                    }
                }
                break;
            case R.id.modify_age:
                if(BallApplication.getUser().getU_age()!=0){
                    user_info_age.setText(BallApplication.getUser().getU_age()+"");
                }
                break;
            case R.id.modify_connect:
                if(BallApplication.getUser().getU_name()!=null&&!BallApplication.getUser().getU_name().trim().equals("")){
                    user_info_connect.setText(BallApplication.getUser().getU_name());
                }else{
                    user_info_connect.setText("未设置");
                }
                break;
            case R.id.user_info_signature:
                if(BallApplication.getUser().getSignature()!=null){
                    user_info_signature_text.setText("简介："+BallApplication.getUser().getSignature());
                }else{
                    user_info_signature_text.setText("简介：点击编辑");
                }
                break;
            case R.id.modify_hobby:
                if(BallApplication.getUser().getU_playtype()!=null&&!BallApplication.getUser().getU_playtype().trim().equals("")){
                    char[] type = BallApplication.getUser().getU_playtype().toCharArray();
                    StringBuilder ball = new StringBuilder();
                    for(int i = 0;i<type.length;i++){
                        if (type[i]=='1'){
                            ball.append(BallApplication.getHobbyMap().get((i + 1) + "")).append("、");
                        }
                    }
                    ball.delete(ball.length()-1,ball.length());
                    user_info_hobby.setText(ball);
                }else {
                    user_info_hobby.setText("未设置");
                }
                break;
            case 10:
                //相册
/*                if(resultCode == Activity.RESULT_OK){
                    if(Build.VERSION.SDK_INT >=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }*/
                if(requestCode == Activity.RESULT_OK){
                    progressDialog.show();
                    Uri uri = data.getData();
                    Log.i("Photo:uri",uri.toString());
                    ContentResolver cr = getActivity().getContentResolver();
                    try {
                        if(bmp != null)//如果不释放的话，不断取图片，将会内存不够
                            bmp.recycle();
                        bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    UploadPic(11);
                }

                break;
            case  11:
                //相机
                if(resultCode == Activity.RESULT_OK){
                    Intent newIntent = new Intent("com.android.camera.action.CROP");
                    newIntent.setDataAndType(imageUri,"image/*");
                    newIntent.putExtra("scale", true);
                    newIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(newIntent,12);
                }
                break;
            case 12:
                //裁剪
                if(resultCode== Activity.RESULT_OK){
                    try {
                        if(bmp!=null){
                            bmp.recycle();
                        }
                        bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        UploadPic(12);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    private void ClearSharePref(){
        BallApplication.setUserPic(null);
        BallApplication.setUser(null);
        SharedPreferences preferences = getActivity().getSharedPreferences("base64",getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("User");
        editor.commit();
    }

    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(getActivity(),uri)){
            //如果document类型的URI,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath = getImagePath(uri,null);
            }
            displayImage(imagePath);
        }
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return  path;
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            user_info_pic.setImageBitmap(bitmap);
        }else{
            Toast.makeText(getActivity(),"fail",Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadPic(int type){
        switch (type){
            case 10:
                StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/UpLoadPicServlet", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        if(!"".equals(s)){
                            Log.i("UserInfoFragment",s);
                            user_info_pic.setImageBitmap(bmp);
                            BallApplication.getUser().setU_pic(s);
                            saveUserToSharePref(BallApplication.getUser());
                            BallApplication.setUserPic(bmp);
                            Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(),"上传失败",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String ,String> map = new HashMap<>();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        BitmapUtils.scaleBitmap(bmp).compress(Bitmap.CompressFormat.PNG, 100, baos);
                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        byte[] buffer = baos.toByteArray();
                        System.out.println("图片的大小：" + buffer.length);
                        //将图片的字节流数据加密成base64字符输出
                        photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
                        map.put("u_id", BallApplication.getUser().getU_id() + "");
                        map.put("photo", photo);
                        return map;
                    }
                };
                request.setRetryPolicy(new DefaultRetryPolicy(500000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                BallApplication.getQueues().add(request);
                break;
            case 12:
                StringRequest request1 = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/UpLoadPicServlet", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        if(!"".equals(s)){
                            Log.i("UserInfoFragment",s);
                            user_info_pic.setImageBitmap(bmp);
                            BallApplication.getUser().setU_pic(s);
                            saveUserToSharePref(BallApplication.getUser());
                            BallApplication.setUserPic(bmp);
                            Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(),"上传失败",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String ,String> map = new HashMap<>();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        BitmapUtils.scaleBitmap(bmp).compress(Bitmap.CompressFormat.PNG, 100, baos);
                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        byte[] buffer = baos.toByteArray();
                        //将图片的字节流数据加密成base64字符输出
                        photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
                        map.put("u_id", BallApplication.getUser().getU_id() + "");
                        map.put("photo", photo);
                        return map;
                    }
                };
                request1.setRetryPolicy(new DefaultRetryPolicy(500000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                BallApplication.getQueues().add(request1);
                break;
        }

    }
   /* ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
    try {
        baos.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    byte[] buffer = baos.toByteArray();
    System.out.println("图片的大小：" + buffer.length);
    String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = new RequestParams();
    params.put("file", photo);
    client.post(BallApplication.ServerUrl+"UpLoadPicServlet",params,new AsyncHttpResponseHandler(){
    });*/



    private void saveUserToSharePref(User user){
        pref = getActivity().getSharedPreferences("base64",getActivity().MODE_PRIVATE);
        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            //创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            //将对象写入字节流
            oos.writeObject(user);
            //将字节流编码成base64字符串
            String UserBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor = pref.edit();
            editor.putString("User",UserBase64);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
