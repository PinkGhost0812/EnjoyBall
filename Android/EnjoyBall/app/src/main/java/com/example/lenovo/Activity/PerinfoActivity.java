package com.example.lenovo.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PerinfoActivity extends AppCompatActivity {

    private ImageView ivPerinfoPortrait;

    private TextView tvPerinfoNickname;
    private TextView tvPerinfoSex;
    private TextView tvPerinfoAge;
    private TextView tvPerinfoCity;
    private TextView tvPerinfoPhone;
    private TextView tvPerinfoEmail;
    private TextView tvPerinfoSignature;
    private TextView tvPerinfoSave;
    private TextView tvDialogPerinfoMan;
    private TextView tvDialogPerinfoWoman;
    private TextView tvDialogPerinfoSecret;

    private LinearLayout llPerinfoPortrait;
    private LinearLayout llPerinfoNickname;
    private LinearLayout llPerinfoSex;
    private LinearLayout llPerinfoAge;
    private LinearLayout llPerinfoCity;
    private LinearLayout llPerinfoPhone;
    private LinearLayout llPerinfoEmail;
    private LinearLayout llPerinfoSignature;
    private LinearLayout llPerinfoVip;

    private User user;

    private Intent intent;

    private ArrayList<String> perinfoAgeList = new ArrayList<>();

    private ListView lvDialogPerinfoAge;

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_perinfo);
        
        findView();
        
        user= (User) getIntent().getSerializableExtra("user");
        
        setInfo();
        
        setListeners();
        
    }

    private class PerinfoListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_perinfo_portrait:
                    //点击头像
                    ActivityCompat.requestPermissions(PerinfoActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            100);
                    break;
                case R.id.ll_perinfo_nickname:
                    //点击用户名
                    break;
                case R.id.ll_perinfo_sex:
                    //点击性别
                    showSexBottomSheetDialog();
                    break;
                case R.id.ll_perinfo_age:
                    //点击年龄
                    showAgeBottomSheetDialog();
                    break;
                case R.id.ll_perinfo_city:
                    //点击城市
                    break;
                case R.id.ll_perinfo_phone:
                    //点击手机号
                    break;
                case R.id.ll_perinfo_email:
                    //点击邮箱
                    break;
                case R.id.ll_perinfo_signature:
                    //点击个性签名
                    break;
                case R.id.ll_perinfo_vip:
                    //点击vip
                    break;
                case R.id.tv_perinfo_save:
                    //点击保存
                    break;

            }
        }
    }

    private void showSexBottomSheetDialog() {

        bottomSheetDialog= new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(R.layout.dialog_perinfo_sex);

        tvDialogPerinfoMan=bottomSheetDialog.findViewById(R.id.tv_dialog_perinfo_man);
        tvDialogPerinfoWoman=bottomSheetDialog.findViewById(R.id.tv_dialog_perinfo_woman);
        tvDialogPerinfoSecret=bottomSheetDialog.findViewById(R.id.tv_dialog_perinfo_secret);

        bottomSheetDialog.show();

        setDialogListeners();

    }

    private void showAgeBottomSheetDialog() {

        bottomSheetDialog= new BottomSheetDialog(this);

        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(R.layout.dialog_perinfo_age);

        lvDialogPerinfoAge=bottomSheetDialog.findViewById(R.id.lv_dialog_perinfo_age);

        ArrayAdapter<String> agesAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,perinfoAgeList);

        lvDialogPerinfoAge.setAdapter(agesAdapter);

        lvDialogPerinfoAge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvPerinfoAge.setText(perinfoAgeList.get(position)+"");
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();

    }

    private void setDialogListeners() {

        PerinfoDialogListener perinfoDialogListener=new PerinfoDialogListener();

        tvDialogPerinfoMan.setOnClickListener(perinfoDialogListener);
        tvDialogPerinfoWoman.setOnClickListener(perinfoDialogListener);
        tvDialogPerinfoSecret.setOnClickListener(perinfoDialogListener);

    }

    private class PerinfoDialogListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_dialog_perinfo_man:
                    //点击男
                    tvPerinfoSex.setText(tvDialogPerinfoMan.getText());
                    bottomSheetDialog.dismiss();
                    break;
                case R.id.tv_dialog_perinfo_woman:
                    //点击女
                    tvPerinfoSex.setText(tvDialogPerinfoWoman.getText());
                    bottomSheetDialog.dismiss();
                    break;
                case R.id.tv_dialog_perinfo_secret:
                    //点击保密
                    tvPerinfoSex.setText(tvDialogPerinfoSecret.getText());
                    bottomSheetDialog.dismiss();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri,null,null,
                    null,null);
            if (cursor.moveToFirst()){
                String imgPath = cursor.getString(cursor.getColumnIndex("_data"));
                RequestOptions options = new RequestOptions()
                        .circleCrop();
                Glide.with(PerinfoActivity.this)
                        .load(imgPath)
                        .apply(options)
                        .into(ivPerinfoPortrait);
                //上传头像到服务器端
//                File file = new File(imgPath);
//                RequestBody body = RequestBody.create(MediaType.parse("image/*"),
//                        file);
//                Request request = new Request.Builder()
//                        .url(Constant.BASE_URL+"UploadServlet")
//                        .post(body)
//                        .build();
//                Call call = okHttpClient.newCall(request);
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e("上传头像",response.body().string());
//                    }
//                });
            }
        }
    }

    private void setListeners() {

        PerinfoListener perinfoListener=new PerinfoListener();

        llPerinfoPortrait.setOnClickListener(perinfoListener);
        llPerinfoNickname.setOnClickListener(perinfoListener);
        llPerinfoSex.setOnClickListener(perinfoListener);
        llPerinfoAge.setOnClickListener(perinfoListener);
        llPerinfoCity.setOnClickListener(perinfoListener);
        llPerinfoPhone.setOnClickListener(perinfoListener);
        llPerinfoEmail.setOnClickListener(perinfoListener);
        llPerinfoSignature.setOnClickListener(perinfoListener);
        llPerinfoVip.setOnClickListener(perinfoListener);
        tvPerinfoSave.setOnClickListener(perinfoListener);

    }

    private void setInfo() {

        tvPerinfoNickname.setText(user.getUser_nickname());
        tvPerinfoSex.setText(user.getUser_sex());
        //tvPerinfoAge.setText(user.getUser_address());
        tvPerinfoCity.setText(user.getUser_address());
        tvPerinfoPhone.setText(user.getUser_phonenumber());
        tvPerinfoEmail.setText(user.getUser_email());
        tvPerinfoSignature.setText(user.getUser_signature());


        perinfoAgeList.clear();
        perinfoAgeList.add("保密");
        for (int i = 1; i <= 99; i++) {
            perinfoAgeList.add(String.format("%d岁", i));
        }

    }

    private void findView() {

        ivPerinfoPortrait=findViewById(R.id.iv_perinfo_portrait);

        tvPerinfoNickname=findViewById(R.id.tv_perinfo_nickname);
        tvPerinfoSex=findViewById(R.id.tv_perinfo_sex);
        tvPerinfoAge=findViewById(R.id.tv_perinfo_age);
        tvPerinfoCity=findViewById(R.id.tv_perinfo_city);
        tvPerinfoPhone=findViewById(R.id.tv_perinfo_phone);
        tvPerinfoEmail=findViewById(R.id.tv_perinfo_email);
        tvPerinfoSignature=findViewById(R.id.tv_perinfo__signature);
        tvPerinfoSave=findViewById(R.id.tv_perinfo_save);

        llPerinfoPortrait=findViewById(R.id.ll_perinfo_portrait);
        llPerinfoNickname=findViewById(R.id.ll_perinfo_nickname);
        llPerinfoSex=findViewById(R.id.ll_perinfo_sex);
        llPerinfoAge=findViewById(R.id.ll_perinfo_age);
        llPerinfoCity=findViewById(R.id.ll_perinfo_city);
        llPerinfoPhone=findViewById(R.id.ll_perinfo_phone);
        llPerinfoEmail=findViewById(R.id.ll_perinfo_email);
        llPerinfoSignature=findViewById(R.id.ll_perinfo_signature);
        llPerinfoVip=findViewById(R.id.ll_perinfo_vip);

    }
}