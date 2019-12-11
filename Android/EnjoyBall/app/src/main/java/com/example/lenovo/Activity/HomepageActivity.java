package com.example.lenovo.Activity;

import android.graphics.Color;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.lenovo.Fragment.HomepageCommentFragment;
import com.example.lenovo.Fragment.HomepageFansFragment;
import com.example.lenovo.Fragment.HomepageFollowFragment;
import com.example.lenovo.Fragment.HomepageUserinfoFragment;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class HomepageActivity extends AppCompatActivity {

    private Button btnFollow;

    private TextView tvHomepagePerinfo;
    private TextView tvHomepageComment;
    private TextView tvHomepageFollow;
    private TextView tvHomepageFans;
    private TextView tvHomepageNickname;

    private LinearLayout specHomepageInfo;
    private LinearLayout specHomepageComment;
    private LinearLayout specHomepageFollow;
    private LinearLayout specHomepageFans;

    private ImageView ivHomepagePortrait;

    private String homepagePortraitPath;

    private User user;
    private User visitUser;

    private class HomepageTabSpec {
        private TextView textView = null;
        private Fragment fragment = null;

        private void setSelect(boolean b) {
            if (b) {
                textView.setTextColor(
                        Color.parseColor("#000000"));
            } else {
                textView.setTextColor(
                        Color.parseColor("#C0C0C0"));
            }
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }
    }

    private Map<String, HomepageTabSpec> map = new HashMap<>();
    private String[] tabStrId = {"信息", "评论", "关注","粉丝"};
    private Fragment curFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_homepage);

        findView();

        user= (User) getIntent().getSerializableExtra("user");

        setInfo();

        initData();

        setListeners();

        changeTab(tabStrId[0]);

        if (getIntent().getStringExtra("tag")!=null&&getIntent().getStringExtra("tag").equals("comment")){
            changeTab(tabStrId[1]);
        }else if (getIntent().getStringExtra("tag")!=null&&getIntent().getStringExtra("tag").equals("follow")){
            changeTab(tabStrId[2]);
        }else if (getIntent().getStringExtra("tag")!=null&&getIntent().getStringExtra("tag").equals("fans")){
            changeTab(tabStrId[3]);
        }
//
//        btnFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //不能关注自己
//                if (visitUser==null){
//                    Toast.makeText(HomepageActivity.this,"不能自己关注自己凹~",Toast.LENGTH_SHORT).show();
//                } else{
//                    //todo:对其他人关注
//                    Toast.makeText(HomepageActivity.this,"关注成功~",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private void setInfo() {
        homepagePortraitPath= Info.BASE_URL+user.getUser_headportrait();

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .circleCrop();
        Glide.with(HomepageActivity.this)
                .load(homepagePortraitPath)
                .apply(options)
                .into(ivHomepagePortrait);

        tvHomepageNickname.setText(user.getUser_nickname());

    }

    private void changeTab(String s) {
        
        changeFragment(s);

        changeText(s);
    }

    private void changeText(String s) {
        for (String key : map.keySet()) {
            map.get(key).setSelect(false);
        }

        map.get(s).setSelect(true);
    }

    private void changeFragment(String s) {

        Fragment fragment = map.get(s).getFragment();

        if (curFragment == fragment) return;

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        if (curFragment != null)
            transaction.hide(curFragment);

        if (!fragment.isAdded()) {
            transaction.add(R.id.fl_homepage_content, fragment);
        }
        transaction.show(fragment);
        curFragment = fragment;

        transaction.commit();

    }

    private void setListeners() {

        HomepageListener homepageListener=new HomepageListener();
        specHomepageInfo.setOnClickListener(homepageListener);
        specHomepageComment.setOnClickListener(homepageListener);
        specHomepageFollow.setOnClickListener(homepageListener);
        specHomepageFans.setOnClickListener(homepageListener);

    }


    private class HomepageListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.spec_homepage_perinfo:
                    changeTab(tabStrId[0]);
                    break;
                case R.id.spec_homepage_comment:
                    changeTab(tabStrId[1]);
                    break;
                case R.id.spec_homepage_follow:
                    changeTab(tabStrId[2]);
                    break;
                case R.id.spec_homepage_fans:
                    changeTab(tabStrId[3]);
                    break;
            }
        }
    }


    private void setFragment() {

        map.get(tabStrId[0]).setFragment(new HomepageUserinfoFragment());
        map.get(tabStrId[1]).setFragment(new HomepageCommentFragment());
        map.get(tabStrId[2]).setFragment(new HomepageFollowFragment());
        map.get(tabStrId[3]).setFragment(new HomepageFansFragment());

    }


    private void initData() {

        map.put(tabStrId[0], new HomepageTabSpec());
        map.put(tabStrId[1], new HomepageTabSpec());
        map.put(tabStrId[2], new HomepageTabSpec());
        map.put(tabStrId[3], new HomepageTabSpec());

        setFragment();

        map.get(tabStrId[0]).setTextView(tvHomepagePerinfo);
        map.get(tabStrId[1]).setTextView(tvHomepageComment);
        map.get(tabStrId[2]).setTextView(tvHomepageFollow);
        map.get(tabStrId[3]).setTextView(tvHomepageFans);

    }

    private void findView() {

        btnFollow=findViewById(R.id.btn_homepage_follow);

        tvHomepagePerinfo=findViewById(R.id.tv_homepage_perinfo);
        tvHomepageComment=findViewById(R.id.tv_homepage_comment);
        tvHomepageFollow=findViewById(R.id.tv_homepage_follow);
        tvHomepageFans=findViewById(R.id.tv_homepage_fans);
        tvHomepageNickname=findViewById(R.id.tv_homepage_nickname);

        specHomepageInfo=findViewById(R.id.spec_homepage_perinfo);
        specHomepageComment=findViewById(R.id.spec_homepage_comment);
        specHomepageFollow=findViewById(R.id.spec_homepage_follow);
        specHomepageFans=findViewById(R.id.spec_homepage_fans);

        ivHomepagePortrait=findViewById(R.id.iv_homepage_portrait);

    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
