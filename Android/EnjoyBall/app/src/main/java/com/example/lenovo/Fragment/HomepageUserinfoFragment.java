package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomepageUserinfoFragment extends Fragment {

    private View getView;

    private TextView tvUserinfoNickname;
    private TextView tvUserinfoSex;
    private TextView tvUserinfoAge;
    private TextView tvUserinfoCity;
    private TextView tvUserinfoPhone;
    private TextView tvUserinfoEmail;
    private TextView tvUserinfoSignature;

    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_homepage_userinfo,container, false);

        getView=view;

        findView();

        user= (User) getActivity().getIntent().getSerializableExtra("user");

        setInfo();

        return view;

    }

    private void findView() {
        tvUserinfoNickname=getView.findViewById(R.id.tv_userinfo_nickname);
        tvUserinfoSex=getView.findViewById(R.id.tv_userinfo_sex);
        tvUserinfoAge=getView.findViewById(R.id.tv_userinfo_age);
        tvUserinfoCity= getView.findViewById(R.id.tv_userinfo_city);
        tvUserinfoPhone=getView.findViewById(R.id.tv_userinfo_phone);
        tvUserinfoEmail=getView.findViewById(R.id.tv_userinfo_email);
        tvUserinfoSignature=getView.findViewById(R.id.tv_userinfo_signature);
    }

    public void setInfo() {

        tvUserinfoNickname.setText(user.getUser_nickname());
        tvUserinfoSex.setText(user.getUser_sex());
        tvUserinfoCity.setText(user.getUser_address());
        tvUserinfoPhone.setText(user.getUser_phonenumber());
        tvUserinfoEmail.setText(user.getUser_email());
        tvUserinfoSignature.setText(user.getUser_signature());

    }

}
