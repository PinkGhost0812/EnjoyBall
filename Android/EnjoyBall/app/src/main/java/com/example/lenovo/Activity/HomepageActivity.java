package com.example.lenovo.Activity;

import android.graphics.Color;
import android.os.Looper;
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
import com.example.lenovo.entity.UserFans;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private User curUser;

    private OkHttpClient okHttpClient;

    private List<User> userList;

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
    private String[] tabStrId = {"信息", "评论", "关注", "粉丝"};
    private Fragment curFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_homepage);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        findView();

        user = (User) getIntent().getSerializableExtra("user");
        curUser = ((Info) getApplicationContext()).getUser();

        setInfo();

        initData();

        setListeners();

        //judgeIsFollow();

        changeTab(tabStrId[0]);

        if (getIntent().getStringExtra("tag") != null && getIntent().getStringExtra("tag").equals("comment")) {
            changeTab(tabStrId[1]);
        } else if (getIntent().getStringExtra("tag") != null && getIntent().getStringExtra("tag").equals("follow")) {
            changeTab(tabStrId[2]);
        } else if (getIntent().getStringExtra("tag") != null && getIntent().getStringExtra("tag").equals("fans")) {
            changeTab(tabStrId[3]);
        }

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不能关注自己
                if (user.getUser_id().equals(curUser.getUser_id())) {
                    Log.e("test user 1", user.getUser_nickname());
                    Log.e("test curuser 1", curUser.getUser_nickname());
                    Log.e("test user 1", user.getUser_id() + "");
                    Log.e("test curuser 1", curUser.getUser_id() + "");
                    Toast.makeText(HomepageActivity.this, "不能自己关注自己凹~", Toast.LENGTH_SHORT).show();
                    btnFollow.setText("√ 关注");
                } else {
                    Log.e("test user 2", user.getUser_nickname());
                    Log.e("test curuser 2", curUser.getUser_nickname());
                    Log.e("test user 2", user.getUser_id() + "");
                    Log.e("test curuser 2", curUser.getUser_id() + "");
                    follow();
                }
            }
        });

    }

    private void follow() {

        okHttpClient = new OkHttpClient();
        Log.e("test", user.getUser_id() + "");
        UserFans userFans = new UserFans();
        userFans.setFans_id(curUser.getUser_id());
        userFans.setUser_id(user.getUser_id());
        Gson gson = new GsonBuilder().create();
        String jsonStr = gson.toJson(userFans);
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/follow?userFans=" + jsonStr)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(HomepageActivity.this, "获取关注列表失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data = response.body().string();

                Log.e("test is follow", data);
                Message msg = new Message();
                msg.what = 22;
                msg.obj = data;

                EventBus.getDefault().post(msg);

            }
        });

    }

    private void setInfo() {

        homepagePortraitPath = Info.BASE_URL + user.getUser_headportrait();

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

        HomepageListener homepageListener = new HomepageListener();
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

        btnFollow = findViewById(R.id.btn_homepage_follow);

        tvHomepagePerinfo = findViewById(R.id.tv_homepage_perinfo);
        tvHomepageComment = findViewById(R.id.tv_homepage_comment);
        tvHomepageFollow = findViewById(R.id.tv_homepage_follow);
        tvHomepageFans = findViewById(R.id.tv_homepage_fans);
        tvHomepageNickname = findViewById(R.id.tv_homepage_nickname);

        specHomepageInfo = findViewById(R.id.spec_homepage_perinfo);
        specHomepageComment = findViewById(R.id.spec_homepage_comment);
        specHomepageFollow = findViewById(R.id.spec_homepage_follow);
        specHomepageFans = findViewById(R.id.spec_homepage_fans);

        ivHomepagePortrait = findViewById(R.id.iv_homepage_portrait);

    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


//    private void judgeIsFollow() {
//
//        //如果已经关注了这个人，则显示√ 关注
//        //获得当前用户的所有关注的用户
//        okHttpClient = new OkHttpClient();
//        Log.e("test", user.getUser_id().toString());
//        Request request = new Request.Builder()
//                .url(Info.BASE_URL + "user/getfollow?id=" + user.getUser_id())
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Looper.prepare();
//                Toast.makeText(HomepageActivity.this, "获取关注列表失败~", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                String data = response.body().string();
//
//                Log.e("test follow", data);
//
//                if (data.equals("false")) {
//                    //Toast.makeText(getActivity(), "用户无关注~", Toast.LENGTH_SHORT).show();
//                } else {
//                    Gson gson = new GsonBuilder()
//                            .create();
//                    Type listType = new TypeToken<List<User>>() {
//                    }.getType();
//                    userList = gson.fromJson(data, listType);
//                    Message msg = new Message();
//                    msg.what = 22;
//                    msg.obj = userList;
//                    EventBus.getDefault().post(msg);
//                }
//
//            }
//        });
//    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void changeButton(Message msg) {

        if (msg.what == 22) {
            if (msg.obj.equals("true")) {
                Log.e("test success", "success");
                Toast.makeText(getApplication(), "关注成功~", Toast.LENGTH_SHORT).show();
                btnFollow.setText("√ 关注");
            } else {
                Log.e("test fail", "fali");
                Toast.makeText(getApplication(), "您已经过关注该用户了哦", Toast.LENGTH_SHORT).show();
                btnFollow.setText("√ 关注");
            }

        }

    }

}
