package com.example.springsora.balltogether.fragment.modify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.springsora.balltogether.R;
import com.example.springsora.balltogether.base.BaseFragment;
import com.example.springsora.balltogether.base.BasePager;
import com.example.springsora.balltogether.custom_adapter.ModifyPhoneAdapter;
import com.example.springsora.balltogether.custom_viewpager.MyViewPager;
import com.example.springsora.balltogether.pager.modify.ChangePhonePager;
import com.example.springsora.balltogether.pager.modify.NewPhonePager;
import com.example.springsora.balltogether.pager.modify.VerificationPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JJBOOM on 2016/4/23.
 */
public class PhoneFragment extends BaseFragment {

    private View view;

    private MyViewPager modify_phone_viewpager;

    private List<BasePager> pagers;

    private ImageView modify_phone_back;


    @Override
    public void initData(Bundle savedInstanceState) {
        modify_phone_viewpager.setAdapter(new ModifyPhoneAdapter(pagers));
        buttonListener();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.modify_phone,null);
        initControl();
        return view;
    }

    private void initControl(){
        modify_phone_viewpager = (MyViewPager) view.findViewById(R.id.modify_phone_viewpager);
        modify_phone_back = (ImageView) view.findViewById(R.id.modify_phone_back);
        pagers = new ArrayList<>();
        pagers.add(new VerificationPager(getActivity(),modify_phone_viewpager));
        pagers.add(new NewPhonePager(getActivity(),modify_phone_viewpager));
        pagers.add(new ChangePhonePager(getActivity(),modify_phone_viewpager));
    }

    private void buttonListener(){
        modify_phone_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
