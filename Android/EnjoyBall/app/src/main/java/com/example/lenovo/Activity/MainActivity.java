package com.example.lenovo.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.lenovo.Activity.PersonalcenterActivity;
import com.example.lenovo.Fragment.GameFragment;
import com.example.lenovo.Fragment.HomeFragment;
import com.example.lenovo.Fragment.MessageFragment;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.Fragment.TimeFragment;
import com.example.lenovo.entity.Contest;
import com.example.lenovo.entity.DemandInfo;
import com.example.lenovo.entity.News;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private ImageView ivMainPortrait;
    private ImageView ivSearch;
    private EditText etSearch;
    private LinearLayout ll_top;
    private int tabtop = 0;
    private int tab = 0;
    private int refresh = 0;
    private Contest contest;
    private News news;
    private DemandInfo demandInfo;
    private long startTime=1;

    private User user;


    private class MyTabSpec {
        private ImageView imageView = null;
        private TextView textView = null;
        private int normalImage;
        private int selectImage;
        private Fragment fragment = null;

        private void setSelect(boolean b) {
            if (b){
                imageView.setImageResource(selectImage);
                textView.setTextColor(
                        Color.parseColor("#1afa29"));
            } else {
                imageView.setImageResource(normalImage);
                textView.setTextColor(
                        Color.parseColor("#707070")
                );
            }
        }

        private void setSelectTop(boolean b) {
            if (b){
                imageView.setImageResource(selectImage);
                textView.setTextColor(
                        Color.parseColor("#1afa29"));
            } else {
                imageView.setImageResource(normalImage);
                textView.setTextColor(
                        Color.parseColor("#000000")
                );
            }
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public int getNormalImage() {
            return normalImage;
        }

        public void setNormalImage(int normalImage) {
            this.normalImage = normalImage;
        }

        public int getSelectImage() {
            return selectImage;
        }

        public void setSelectImage(int selectImage) {
            this.selectImage = selectImage;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }
    }

    private Map<String,MyTabSpec> map = new HashMap<>();
    private Map<String,MyTabSpec> mapTop = new HashMap<>();
    private String[] tabStrTopId = {"全部","足球","篮球","排球","羽毛球","乒乓球"};
    private String[] tabStrId = {"首页","比赛","约球","消息"};
    private Fragment curFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_main);

        user=((Info)getApplicationContext()).getUser();

        initData();

        setListener();

        changeTab(tabStrId[0],tabtop);

        changeImageTop(tabStrTopId[0]);

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .circleCrop();

        Log.e("test",user.getUser_id().toString());
        Glide.with(this)
                .load(Info.BASE_URL+user.getUser_headportrait())
                .apply(options)
                .into(ivMainPortrait);
    }

    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tab_spec_main_home:
                    tab = 0;
                    ll_top.setVisibility(View.VISIBLE);
                    changeTab(tabStrId[0],tabtop);
                    Log.e("下一步",tabStrId[tab]+"");
                    break;
                case R.id.tab_spec_main_game:
                    tab =  1;
                    ll_top.setVisibility(View.VISIBLE);
                    changeTab(tabStrId[1],tabtop);
                    break;
                case R.id.tab_spec_main_time:
                    tab =  2;
                    ll_top.setVisibility(View.VISIBLE);
                    changeTab(tabStrId[2],tabtop);
                    break;
                case R.id.tab_spec_main_message:
                    tab =  3;
                    ll_top.setVisibility(View.GONE);
                    changeTab(tabStrId[3],tabtop);
                    break;
                case R.id.tab_spec_main_topall:
                    tabtop =  0;
                    changeTabTop(tabStrTopId[0],tab,tabtop);
                    break;
                case R.id.tab_spec_main_football:
                    tabtop =  1;
                    changeTabTop(tabStrTopId[1],tab,tabtop);
                    break;
                case R.id.tab_spec_main_basketball:
                    tabtop =  2;
                    changeTabTop(tabStrTopId[2],tab,tabtop);
                    break;
                case R.id.tab_spec_main_volleyball:
                    tabtop =  3;
                    Log.e("球类",tabStrTopId[tabtop]);
                    changeTabTop(tabStrTopId[3],tab,tabtop);
                    break;
                case R.id.tab_spec_main_badminton:
                    tabtop =  4;
                    Log.e("球类",tabStrTopId[tabtop]);
                    changeTabTop(tabStrTopId[4],tab,tabtop);
                    break;
                case R.id.tab_spec_main_tabletennis:
                    tabtop =  5;
                    Log.e("球类",tabStrTopId[tabtop]);
                    changeTabTop(tabStrTopId[5],tab,tabtop);
                    break;
                case R.id.iv_main_portrait:
                    //跳转到个人中心页面
                    if(startTime!=0){
                        long endTime = System.currentTimeMillis();
                        if(endTime-startTime<500){
                            Toast.makeText(getApplicationContext(), "点的太快了噢~", Toast.LENGTH_LONG).show();
                        }else{
                            Intent intent =new Intent();
                            intent.setClass(MainActivity.this, PersonalcenterActivity.class);
                            overridePendingTransition(R.anim.personalcenter_in, R.anim.personalcenter_out);
                            startActivity(intent);
                        }
                    }
                    startTime = System.currentTimeMillis();
                    break;
                case R.id.iv_main_serch:
                    Intent intent1 =new Intent();
                    intent1.setClass(MainActivity.this, SearchActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.et_main_search:
                    Intent intent2 =new Intent();
                    intent2.setClass(MainActivity.this, SearchActivity.class);
                    overridePendingTransition(R.anim.personalcenter_in, R.anim.personalcenter_out);
                    startActivity(intent2);
                    break;

            }
        }
    }

    // 根据Tab ID 切换Tab
    private void changeTab(String s,int i) {
        // 1 切换Fragment
        changeFragment(s,i);

        // 2 切换图标及字体颜色
        changeImage(s);
    }
    private void changeTabTop(String s,int x,int y) {
        // 1 切换Fragment
        //removeTop();
        changeFragmentTop(x,y);
        //changeBall(x,y);
        // 2 切换图标及字体颜色
        changeImageTop(s);
    }

    // 根据Tab ID 切换 Tab显示的Fragment
    private void changeFragment(String s,int i) {
        Fragment fragment = map.get(s).getFragment();
        if(curFragment == fragment) return;

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        if(curFragment!=null)
            transaction.hide(curFragment);

        if(!fragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putInt("ball",i);
            fragment.setArguments(bundle);
            transaction.replace(R.id.tab_content,fragment);
            refresh = 0;
        }
        // 显示对应Fragment
        transaction.show(fragment);
        curFragment = fragment;

        transaction.commit();
    }

    private void changeFragmentTop(int x,int y) {
        Fragment fragment = map.get(tabStrId[x]).getFragment();
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putInt("ball",y);
        fragment.setArguments(bundle);
        transaction.replace(R.id.tab_content,fragment);
        // 显示对应Fragment
        transaction.show(fragment);
        curFragment = fragment;
        transaction.commit();
        fragment.onResume();
    }

    // 根据Tab ID 切换 Tab显示的图片及字体颜色
    private void changeImage(String s) {
        // 1 所有Tab的图片和字体颜色恢复默认
        for (String key : map.keySet()) {
            map.get(key).setSelect(false);
        }

        // 2 设置选中的Tab的图片和字体颜色
        map.get(s).setSelect(true);
    }
    private void changeImageTop(String s) {
        // 1 所有Tab的图片和字体颜色恢复默认
        for (String key : mapTop.keySet()) {
            mapTop.get(key).setSelectTop(false);
        }

        // 2 设置选中的Tab的图片和字体颜色
        mapTop.get(s).setSelectTop(true);
    }

    // 设置监听器
    private void setListener(){

        //vpBanner = (ViewPager)findViewById(R.id.et_login_pwd);

        ll_top = findViewById(R.id.ll_top);
        LinearLayout linearLayouttop1 = findViewById(R.id.tab_spec_main_topall);
        LinearLayout linearLayouttop2 = findViewById(R.id.tab_spec_main_football);
        LinearLayout linearLayouttop3 = findViewById(R.id.tab_spec_main_basketball);
        LinearLayout linearLayouttop4 = findViewById(R.id.tab_spec_main_volleyball);
        LinearLayout linearLayouttop5 = findViewById(R.id.tab_spec_main_badminton);
        LinearLayout linearLayouttop6 = findViewById(R.id.tab_spec_main_tabletennis);
        LinearLayout linearLayout1 = findViewById(R.id.tab_spec_main_home);
        LinearLayout linearLayout2 = findViewById(R.id.tab_spec_main_game);
        LinearLayout linearLayout3 = findViewById(R.id.tab_spec_main_time);
        LinearLayout linearLayout4 = findViewById(R.id.tab_spec_main_message);

        MyListener listener = new MyListener();
        linearLayouttop1.setOnClickListener(listener);
        linearLayouttop2.setOnClickListener(listener);
        linearLayouttop3.setOnClickListener(listener);
        linearLayouttop4.setOnClickListener(listener);
        linearLayouttop5.setOnClickListener(listener);
        linearLayouttop6.setOnClickListener(listener);
        linearLayout1.setOnClickListener(listener);
        linearLayout2.setOnClickListener(listener);
        linearLayout3.setOnClickListener(listener);
        linearLayout4.setOnClickListener(listener);

        ivMainPortrait.setOnClickListener(listener);
        etSearch.setOnClickListener(listener);
        ivSearch.setOnClickListener(listener);

    }

    // 初始化，初始化MyTabSpec对象
    private void initData() {
        // 1 创建MyTabSpec对象
        map.put(tabStrId[0], new MyTabSpec());
        map.put(tabStrId[1], new MyTabSpec());
        map.put(tabStrId[2], new MyTabSpec());
        map.put(tabStrId[3], new MyTabSpec());

        mapTop.put(tabStrTopId[0], new MyTabSpec());
        mapTop.put(tabStrTopId[1], new MyTabSpec());
        mapTop.put(tabStrTopId[2], new MyTabSpec());
        mapTop.put(tabStrTopId[3], new MyTabSpec());
        mapTop.put(tabStrTopId[4], new MyTabSpec());
        mapTop.put(tabStrTopId[5], new MyTabSpec());

        // 2 设置Fragment
        setFragment();

        // 3 设置ImageView和TextView
        findView();

        // 4 设置图片资源
        setImage();
    }

    // 创建Fragment对象并放入map的MyTabSpec对象中
    private void setFragment() {
        map.get(tabStrId[0]).setFragment(new HomeFragment());
        map.get(tabStrId[1]).setFragment(new GameFragment());
        map.get(tabStrId[2]).setFragment(new TimeFragment());
        map.get(tabStrId[3]).setFragment(new MessageFragment());

    }

    // 将图片资源放入map的MyTabSpec对象中
    private void setImage(){
//        map.get(tabStrId[0]).setNormalImage(R.drawable.);
//        map.get(tabStrId[0]).setSelectImage(R.drawable.);
        map.get(tabStrId[0]).setNormalImage(R.drawable.home2);
        map.get(tabStrId[0]).setSelectImage(R.drawable.home);
        map.get(tabStrId[1]).setNormalImage(R.drawable.game2);
        map.get(tabStrId[1]).setSelectImage(R.drawable.game);
        map.get(tabStrId[2]).setNormalImage(R.drawable.time2);
        map.get(tabStrId[2]).setSelectImage(R.drawable.time);
        map.get(tabStrId[3]).setNormalImage(R.drawable.message2);
        map.get(tabStrId[3]).setSelectImage(R.drawable.message);
        for (int i = 0;i<6;i++){
            mapTop.get(tabStrTopId[i]).setNormalImage(R.drawable.underline11);
            mapTop.get(tabStrTopId[i]).setSelectImage(R.drawable.underlinegreen);
        }
    }

    // 将ImageView和TextView放入map中的MyTabSpec对象
    private void findView() {
        ivMainPortrait=findViewById(R.id.iv_main_portrait);
        etSearch = findViewById(R.id.et_main_search);
        ivSearch = findViewById(R.id.iv_main_serch);
        ImageView ivHome = findViewById(R.id.img_main_home);
        ImageView ivGame = findViewById(R.id.img_main_game);
        ImageView ivTime = findViewById(R.id.img_main_time);
        ImageView ivMessage = findViewById(R.id.img_main_message);
        ImageView ivAll = findViewById(R.id.img_main_topall);
        ImageView ivFootball = findViewById(R.id.img_main_football);
        ImageView ivBasketball = findViewById(R.id.img_main_basketball);
        ImageView ivBadminton = findViewById(R.id.img_main_badminton);
        ImageView ivTabletennis = findViewById(R.id.img_main_tabletennis);
        ImageView ivVolleyball = findViewById(R.id.img_main_volleyball);
        TextView tvHome = findViewById(R.id.tv_main_home);
        TextView tvGame = findViewById(R.id.tv_main_game);
        TextView tvTime = findViewById(R.id.tv_main_time);
        TextView tvMessage = findViewById(R.id.tv_main_message);
        TextView tvAll = findViewById(R.id.tv_main_topall);
        TextView tvFootball = findViewById(R.id.tv_main_football);
        TextView tvBasketbal = findViewById(R.id.tv_main_basketball);
        TextView tvBadminton = findViewById(R.id.tv_main_badminton);
        TextView tvTabletennis = findViewById(R.id.tv_main_tabletennis);
        TextView tvVolleyball = findViewById(R.id.tv_main_volleyball);

        map.get(tabStrId[0]).setImageView(ivHome);
        map.get(tabStrId[0]).setTextView(tvHome);

        map.get(tabStrId[1]).setImageView(ivGame);
        map.get(tabStrId[1]).setTextView(tvGame);

        map.get(tabStrId[2]).setImageView(ivTime);
        map.get(tabStrId[2]).setTextView(tvTime);

        map.get(tabStrId[3]).setImageView(ivMessage);
        map.get(tabStrId[3]).setTextView(tvMessage);

        mapTop.get(tabStrTopId[0]).setImageView(ivAll);
        mapTop.get(tabStrTopId[0]).setTextView(tvAll);

        mapTop.get(tabStrTopId[1]).setImageView(ivFootball);
        mapTop.get(tabStrTopId[1]).setTextView(tvFootball);

        mapTop.get(tabStrTopId[2]).setImageView(ivBasketball);
        mapTop.get(tabStrTopId[2]).setTextView(tvBasketbal);

        mapTop.get(tabStrTopId[3]).setImageView(ivVolleyball);
        mapTop.get(tabStrTopId[3]).setTextView(tvVolleyball);

        mapTop.get(tabStrTopId[4]).setImageView(ivBadminton);
        mapTop.get(tabStrTopId[4]).setTextView(tvBadminton);

        mapTop.get(tabStrTopId[5]).setImageView(ivTabletennis);
        mapTop.get(tabStrTopId[5]).setTextView(tvTabletennis);

    }

}

