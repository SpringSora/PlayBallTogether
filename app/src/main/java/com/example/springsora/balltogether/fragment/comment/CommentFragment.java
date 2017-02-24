package com.example.springsora.balltogether.fragment.comment;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.application.BallApplication;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.bean.BallGround;
import com.example.springsora.balltogether.bean.MyOrder;
import com.example.springsora.balltogether.custom_view.CircleImageView;
import com.example.springsora.balltogether.utils.AsyncImageLoader;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Springsora on 2016/5/9.
 */
public class CommentFragment extends BaseFragment {
    private View view;
    private CircleImageView comment_ballground_pic;
    private TextView comment_ballground_name;
    private EditText comment_edit;
    private ImageView comment_back;
    private TextView comment_commit;
    private MyOrder order;
    private AsyncImageLoader asyncImageLoader;

    private ProgressDialog dialog;
    @Override
    public void initData(Bundle savedInstanceState) {
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.comment_fragment,null);
        initControl();
        return view;
    }

    private void  initControl(){
        order = (MyOrder) getArguments().getSerializable("order");
        asyncImageLoader = new AsyncImageLoader();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在发表评论...");
        dialog.setCancelable(false);
        comment_back = (ImageView) view.findViewById(R.id.comment_back);
        comment_commit = (TextView) view.findViewById(R.id.comment_commit);
        comment_ballground_pic = (CircleImageView) view.findViewById(R.id.comment_ballground_pic);
        comment_ballground_name = (TextView) view.findViewById(R.id.comment_ballground_name);
        comment_ballground_name.setText(order.getBallGround().getGroundName());
        Drawable drawable = asyncImageLoader.loadDrawable(BallApplication.ServerUrl + "/" + order.getBallGround().getBallGroundPic1Path(), new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                comment_ballground_pic.setImageDrawable(imageDrawable);
            }
        });
        if(drawable!=null){
            comment_ballground_pic.setImageDrawable(drawable);
        }else{
            comment_ballground_pic.setImageResource(R.mipmap.preview);
        }
        comment_edit = (EditText) view.findViewById(R.id.comment_edit);
    }

    private void buttonListener(){
        comment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        comment_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment_edit.getText().toString()!=null&&!comment_edit.getText().toString().trim().equals("")){
                    dialog.show();
                    WriteComment();
                }else{
                    Toast.makeText(getActivity(),"评论不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void WriteComment(){
        StringRequest request = new StringRequest(Request.Method.POST, BallApplication.ServerUrl + "/WriteCommentServlet", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                if(Boolean.valueOf(s)){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order",order);
                    bundle.putInt("position",getArguments().getInt("position"));
                    intent.putExtras(bundle);
                    getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(),"评论失败",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("u_id",BallApplication.getUser().getU_id()+"");
                map.put("p_id",order.getBallGround().getP_id()+"");
                map.put("content",comment_edit.getText().toString());
                map.put("isevaluate","0");
                map.put("o_id",order.getO_id()+"");
                return map;
            }
        };
        request.setTag("WriteComment");
        BallApplication.getQueues().add(request);
    }

    @Override
    public void onStop() {
        super.onStop();
        BallApplication.getQueues().cancelAll("WriteComment");
    }
}
