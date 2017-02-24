package com.example.springsora.balltogether.fragment.dateball;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.BallGround;
import com.example.springsora.balltogether.bean.DateBall;
import com.example.springsora.balltogether.bean.LocationService;
import com.example.springsora.balltogether.custom_adapter.DateBallAdapter;
import com.example.springsora.balltogether.playballtogether.BallGroundActiviy;
import com.example.springsora.balltogether.utils.BitmapUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by JJBOOM on 2016/4/24.
 */
public class PublishFragment extends BaseFragment {

    private View view;

    private ImageView dateball_back;

    private ListView dateball_listview;

    private EditText dateball_title_edit;

    private EditText dateball_content_edit;

    private Spinner dateball_type;

    private RadioGroup dateball_set_playground;

    private RadioButton radio0;

    private RadioButton radio1;

    private TextView current_date;

    private TextView start_time;

    private TextView end_time;

    private LinearLayout dateball_add_pic;

    private DatePickerDialog datePickerDialog;

    private TimePickerDialog timePickerDialog;

    private String NewDate;

    private String StartTime;

    private String EndTime;

    private LinearLayout playground_layout;

    private EditText dateball_num_edit;

    private int type;

    private String Addr;
    private String city;

    private BallGround playGround;

    private ImageView dateball_imageview;

    private Bitmap bmp;

    private LocationService locationService;

    private Uri imageUri;

    private LinearLayout date_ball_submit;

    private String photo;

    private ProgressDialog progressDialog;


    private int time;


    public PublishFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        dateball_listview.setAdapter(new DateBallAdapter());
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.dateball_fragment,null);

        InitControl();
        initListView();
        return view;
    }

    private void InitControl(){
        dateball_back = (ImageView) view.findViewById(R.id.dateball_back);
        dateball_listview = (ListView) view.findViewById(R.id.dateball_listview);
        date_ball_submit = (LinearLayout) view.findViewById(R.id.date_ball_submit);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("生成订单中...");
        progressDialog.setCancelable(false);
    }

    private void buttonListener(){

        dateball_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        date_ball_submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        date_ball_submit.setBackgroundResource(R.drawable.date_ball_submit_press);
                        break;
                    case MotionEvent.ACTION_UP:
                        date_ball_submit.setBackgroundResource(R.drawable.date_ball_submit_unpress);
                        if(!dateball_title_edit.getText().toString().trim().equals("")){
                            if(!dateball_content_edit.getText().toString().trim().equals("")){
                                if(!dateball_num_edit.getText().toString().trim().equals("")){
                                    if(!start_time.getText().toString().trim().equals("")){
                                        if(!end_time.getText().toString().trim().equals("")){
                                            String[] start = start_time.getText().toString().trim().split(":");
                                            String[] end = end_time.getText().toString().trim().split(":");
                                            int starthour = Integer.valueOf(start[0]);
                                            int endhour = Integer.valueOf(end[0]);
                                            int startmin = Integer.valueOf(start[1]);
                                            int endmin = Integer.valueOf(end[1]);
                                            if(starthour<endhour){
                                                if(((endhour-starthour)*60+(endmin-startmin))>=60){
                                                    BallApplication.setIsPublish(true);
                                                    time = ((endhour-starthour)*60+(endmin-startmin))/60;
                                                    saveDateBallToServer();
                                                }else{
                                                    Toast.makeText(getActivity(),"运动时间不能少于1个小时",Toast.LENGTH_SHORT).show();
                                                }
                                            }else if(starthour==endhour){
                                                if(startmin<endmin){
                                                    Toast.makeText(getActivity(),"运动时间不能少于1个小时",Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(getActivity(),"时间选择不正确",Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(getActivity(),"时间选择不正确",Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(getActivity(),"结束时间不能为空",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(getActivity(),"开始时间不能为空",Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(getActivity(),"约球人数不能为空",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getActivity(),"内容不能为空",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(),"标题不能为空",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        date_ball_submit.setBackgroundResource(R.drawable.date_ball_submit_unpress);
                        break;
                }
                return true;
            }
        });

        dateball_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 2:
                        ShowDatePickDialog();
                        break;
                    case 3:
                        ShowTimePickDialog(3);
                        break;
                    case 4:
                        ShowTimePickDialog(4);
                        break;
                }
            }
        });

        dateball_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),
                                    0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        dateball_add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("选择上传方式")
                        .setItems(new String[]{"相册","拍照" ,"精美配图"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent1.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent1.setType("image/*");
                                        startActivityForResult(intent1, 10);
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

        dateball_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        type = position + 1;
                        break;
                    case 1:
                        type = position + 1;
                        break;
                    case 2:
                        type = position + 1;

                        break;
                    case 3:
                        type = position + 1;

                        break;
                    case 4:
                        type = position + 1;

                        break;
                    case 5:
                        type = position + 1;

                        break;
                    case 6:
                        type = position + 1;

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateball_set_playground.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        radio1.setChecked(false);
                        Intent intent = new Intent(getActivity(), BallGroundActiviy.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", type);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 0);
                        break;
                    case R.id.radio1:
                        playGround = null;
                        break;
                }
            }
        });


    }



    private void initListView(){
        //ITEM1
        View item1 = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_item1,null);
        dateball_title_edit = (EditText) item1.findViewById(R.id.dateball_title_edit);
        dateball_content_edit = (EditText) item1.findViewById(R.id.dateball_content_edit);
        dateball_add_pic = (LinearLayout) item1.findViewById(R.id.dateball_add_pic);
        dateball_imageview = (ImageView) item1.findViewById(R.id.dateball_imageview);
        dateball_imageview = (ImageView) item1.findViewById(R.id.dateball_imageview);

        //ITEM2
        View item2 = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_item2,null);
        dateball_type = (Spinner) item2.findViewById(R.id.dateball_type);
        dateball_set_playground = (RadioGroup) item2.findViewById(R.id.dateball_set_playground);
        radio0 = (RadioButton) item2.findViewById(R.id.radio0);
        radio1 = (RadioButton) item2.findViewById(R.id.radio1);
        playground_layout = (LinearLayout) item2.findViewById(R.id.playground_layout);
        dateball_num_edit = (EditText) item2.findViewById(R.id.dateball_num_edit);
        //ITEM3
        View item3 = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_item3,null);

        current_date = (TextView) item3.findViewById(R.id.current_date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        NewDate = formatter.format(curDate);
        current_date.setText(NewDate);
        //ITEM4
        View item4 = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_item4,null);
        start_time = (TextView) item4.findViewById(R.id.start_time);
        //ITEM5
        View item5 = LayoutInflater.from(getActivity()).inflate(R.layout.dateball_item5,null);
        end_time = (TextView) item5.findViewById(R.id.end_time);
        dateball_listview.addHeaderView(item1);
        dateball_listview.addHeaderView(item2);
        dateball_listview.addHeaderView(item3);
        dateball_listview.addHeaderView(item4);
        dateball_listview.addHeaderView(item5);
    }

    private void ShowDatePickDialog(){
        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                NewDate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                current_date.setText(NewDate);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void ShowTimePickDialog(final int id){
        final Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                switch (id){
                    case 3:
                        if(minute<10){
                            StartTime = hourOfDay+":"+"0"+minute;
                        }else{
                            StartTime = hourOfDay+":"+minute;
                        }
                        start_time.setText(StartTime);
                        break;
                    case 4:
                        if(minute<10){
                            EndTime = hourOfDay+":"+"0"+minute;
                        }else{
                            EndTime = hourOfDay+":"+minute;
                        }
                        end_time.setText(EndTime );
                        break;
                }
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(resultCode == Activity.RESULT_OK){
                    radio0.setChecked(true);
                    playGround  = (BallGround) data.getExtras().getSerializable("ballGround");
                }else{
                    radio1.setChecked(true);
                }
                break;


            case 10:
                if(resultCode == Activity.RESULT_OK){
                    Uri uri = data.getData();
                    Log.i("Photo:uri", uri.toString());
                    ContentResolver cr = getActivity().getContentResolver();
                    try {
                        if(bmp != null)//如果不释放的话，不断取图片，将会内存不够
                            bmp.recycle();
                        bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        dateball_imageview.setImageBitmap(bmp);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                break;
            case 11:
                if(resultCode == Activity.RESULT_OK){
                    if(bmp!=null){
                        bmp.recycle();
                    }
                    try {
                        bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        dateball_imageview.setImageBitmap(bmp);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        locationService.unregisterListener(mListener);
        locationService.stop();
    }

    @Override
    public void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = BallApplication.getLocationService();
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
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
                Addr =location.getDistrict()+location.getStreet()+location.getLocationDescribe();
                city = location.getCity();
            }
        }
    };

    private void saveDateBallToServer(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl+"/WriteDateBallServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(!"false".equals(s)){
                    if(playGround!=null){
                        WriteMyOrder(Integer.valueOf(s));
                    }else{
                        Toast.makeText(getActivity(),"发送成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        getActivity().setResult(Activity.RESULT_OK,intent);
                        getActivity().finish();
                    }
                }else{
                    Toast.makeText(getActivity(),"发送失败",Toast.LENGTH_SHORT).show();
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
                if(bmp!=null){
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
                    map.put("photo",photo);
                }
                    map.put("u_id",BallApplication.getUser().getU_id()+"");
                    map.put("title",dateball_title_edit.getText().toString());
                    map.put("info",dateball_content_edit.getText().toString());
                    map.put("type",type+"");
                    map.put("location",Addr);
                    map.put("num",dateball_num_edit.getText().toString());
                    map.put("city",city);
                    if(playGround!=null){
                        map.put("p_id",playGround.getP_id()+"");
                    }
                    map.put("date",current_date.getText().toString());
                    map.put("start",start_time.getText().toString());
                    map.put("end",end_time.getText().toString());
                return map;
            }
        };
        BallApplication.getQueues().add(request);
    }

    private void WriteMyOrder(final Integer d_id){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/GenerateOrderServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                if(Boolean.valueOf(s)){
                    Toast.makeText(getActivity(),"发送成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(),"支付失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"网路错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("u_id",BallApplication.getUser().getU_id()+"");
                map.put("p_id",playGround.getP_id()+"");
                map.put("o_price",(time*playGround.getGroundPrice()/Integer.valueOf(dateball_num_edit.getText().toString()).intValue())+"");
                map.put("o_date",NewDate);
                map.put("o_starttime",StartTime);
                map.put("o_endtime",EndTime);
                map.put("type","ballground");
                map.put("d_id",d_id.toString());

                return map;
            }
        };
        request.setTag("WriteMyOrder");
        BallApplication.getQueues().add(request);
    }

}
