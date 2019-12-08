package com.example.lenovo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;

import java.text.SimpleDateFormat;

public class TeamDetailActivity extends AppCompatActivity {

    private TextView tvTeamDetailName;
    private TextView tvTeamDetailCaptain;
    private TextView tvTeamDetailAddress;
    private TextView tvTeamDetailTime;
    private TextView tvTeamDetailSlogan;

    private ImageView ivTeamDetailLogo;

    private ListView lvTeamDetailMember;

    private Button btnTeamDetailDissolve;

    private Team team;

    private User captain;
    private User user;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_detail);

        team= (Team) getIntent().getSerializableExtra("team");
        user= (User) getIntent().getSerializableExtra("captain");

        findView();

        setInfo();

    }

    private void setInfo() {
        tvTeamDetailName.setText(team.getTeam_name());
        tvTeamDetailCaptain.setText(user.getUser_nickname());
        tvTeamDetailAddress.setText(team.getTeam_region());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        tvTeamDetailTime.setText(sf.format(team.getTeam_time()));
        tvTeamDetailSlogan.setText(team.getTeam_slogan());

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .circleCrop();
        Glide.with(TeamDetailActivity.this)
                .load(Info.BASE_URL+team.getTeam_logo())
                .apply(options)
                .into(ivTeamDetailLogo);
    }

    private void findView() {

        tvTeamDetailName = findViewById(R.id.tv_team_detail_name);
        tvTeamDetailCaptain = findViewById(R.id.tv_team_detail_captain);
        tvTeamDetailAddress = findViewById(R.id.tv_team_detail_address);
        tvTeamDetailTime = findViewById(R.id.tv_team_detail_time);
        tvTeamDetailSlogan = findViewById(R.id.tv_team_detail_slogan);

        ivTeamDetailLogo = findViewById(R.id.iv_team_detail_logo);

        lvTeamDetailMember = findViewById(R.id.lv_team_detail_member);

        btnTeamDetailDissolve = findViewById(R.id.btn_team_detail_dissolve);

    }
}
