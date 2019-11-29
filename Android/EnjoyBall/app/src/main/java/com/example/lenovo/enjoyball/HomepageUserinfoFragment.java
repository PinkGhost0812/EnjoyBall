package com.example.lenovo.enjoyball;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomepageUserinfoFragment extends Fragment {

    private View getView;

    private TextView tvUserinfoNickname;
    private TextView tvUserinfoSex;
    private TextView tvUserinfoAge;
    private TextView tvUserinfoCity;
    private TextView tvUserinfoPhone;
    private TextView tvUserinfoEmail;
    private TextView tvUserinfoSignature;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_homepage_userinfo,container, false);

        getView=view;

        findView();

        getInfo();

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

    public void getInfo() {
    }
}
