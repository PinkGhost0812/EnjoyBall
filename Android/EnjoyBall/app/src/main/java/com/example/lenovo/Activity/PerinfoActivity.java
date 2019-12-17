package com.example.lenovo.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
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

    private OkHttpClient okHttpClient;
    private OkHttpClient okHttpClientHeadPortrait;

    private String imgPath = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_perinfo);

        findView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        user = ((Info) getApplicationContext()).getUser();

        setInfo();

        setListeners();

    }

    private class PerinfoListener implements View.OnClickListener {
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
                    intent = new Intent();
                    intent.putExtra("id", user.getUser_id());
                    intent.setClass(PerinfoActivity.this, PerinfoNicknameActivity.class);
                    startActivity(intent);
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
                    Toast.makeText
                            (PerinfoActivity.this, "修改手机号功能开发人员正在加班研究，敬请期待凹~", Toast.LENGTH_LONG).show();
                    break;
                case R.id.ll_perinfo_email:
                    //点击邮箱
                    intent = new Intent();
                    intent.setClass(PerinfoActivity.this, PerinfoEmailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_perinfo_signature:
                    //点击个性签名
                    intent = new Intent();
                    intent.setClass(PerinfoActivity.this, PerinfoSignatureActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_perinfo_vip:
                    //点击vip
                    Toast.makeText
                            (PerinfoActivity.this, "开通会员功能开发人员正在加班研究，敬请期待凹~", Toast.LENGTH_LONG).show();
                    break;
                case R.id.tv_perinfo_save:
                    //点击保存
                    save();
                    break;

            }
        }
    }

    private void save() {

        //更新用户基本信息
        okHttpClient = new OkHttpClient();
        Gson gson = new GsonBuilder().create();
        String userJson = gson.toJson(user);
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/update?info=" + userJson)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "世界上最远的距离就是没网络o(╥﹏╥)o", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                if (response.body().string().equals("true")) {
                    Toast.makeText
                            (PerinfoActivity.this, "更新成功凹~", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(PerinfoActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText
                            (PerinfoActivity.this, "更新失败凹~", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        });

        //更新头像信息
        if (imgPath.equals("0")) {
            Log.e("test", "未更新头像");
        } else {
            uploadHeadPortrait(imgPath);
        }
    }

    private void uploadHeadPortrait(String imgPath) {

        //上传到服务器
        File file = new File(imgPath);
        Log.e("test-upimgpath", imgPath);
        okHttpClientHeadPortrait = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("image/*"),
                file);
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/uploadImg?id=" + user.getUser_id())
                .post(body)
                .build();
        Call call = okHttpClientHeadPortrait.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "世界上最远的距离就是没网络o(╥﹏╥)o", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                String data = response.body().string();
                if (data.equals("true")) {
                    Toast.makeText(PerinfoActivity.this, "更新信息成功~", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(PerinfoActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(PerinfoActivity.this, "更新信息失败~请重试", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        });

        //将头像保存到本地
        try {
            String path = this.getFilesDir() + "/HeadPortrait.jpg";
            Log.e("test", path);
            File fileOutput = new File(path);
            OutputStream outputStream = new FileOutputStream(fileOutput);
            File fileInput = new File(imgPath);
            InputStream inputStream = new FileInputStream(fileInput);
            byte[] buf = new byte[1024];
            int num = 0;
            while ((num = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, num);
            }
            outputStream.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setMsgInfo(Message msg) {
        switch (msg.what) {
            case 4:
                Log.e("testnickname", msg.obj.toString());
                tvPerinfoNickname.setText(msg.obj.toString());
                user.setUser_nickname(msg.obj.toString());
                break;
            case 5:
                tvPerinfoEmail.setText(msg.obj.toString());
                user.setUser_email(msg.obj.toString());
                break;
            case 6:
                tvPerinfoSignature.setText(msg.obj.toString());
                user.setUser_signature(msg.obj.toString());
                break;
        }
    }

    private void showSexBottomSheetDialog() {

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(R.layout.dialog_perinfo_sex);

        tvDialogPerinfoMan = bottomSheetDialog.findViewById(R.id.tv_dialog_perinfo_man);
        tvDialogPerinfoWoman = bottomSheetDialog.findViewById(R.id.tv_dialog_perinfo_woman);
        tvDialogPerinfoSecret = bottomSheetDialog.findViewById(R.id.tv_dialog_perinfo_secret);

        bottomSheetDialog.show();

        setDialogListeners();

    }

    private void showAgeBottomSheetDialog() {

        bottomSheetDialog = new BottomSheetDialog(this);

        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(R.layout.dialog_perinfo_age);

        lvDialogPerinfoAge = bottomSheetDialog.findViewById(R.id.lv_dialog_perinfo_age);

        ArrayAdapter<String> agesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, perinfoAgeList);

        lvDialogPerinfoAge.setAdapter(agesAdapter);

        lvDialogPerinfoAge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvPerinfoAge.setText(perinfoAgeList.get(position) + "");
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();

    }

    private void setDialogListeners() {

        PerinfoDialogListener perinfoDialogListener = new PerinfoDialogListener();

        tvDialogPerinfoMan.setOnClickListener(perinfoDialogListener);
        tvDialogPerinfoWoman.setOnClickListener(perinfoDialogListener);
        tvDialogPerinfoSecret.setOnClickListener(perinfoDialogListener);

    }

    private class PerinfoDialogListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_dialog_perinfo_man:
                    //点击男
                    tvPerinfoSex.setText(tvDialogPerinfoMan.getText());
                    user.setUser_sex(tvDialogPerinfoMan.getText().toString());
                    bottomSheetDialog.dismiss();
                    break;
                case R.id.tv_dialog_perinfo_woman:
                    //点击女
                    tvPerinfoSex.setText(tvDialogPerinfoWoman.getText());
                    user.setUser_sex(tvDialogPerinfoWoman.getText().toString());
                    bottomSheetDialog.dismiss();
                    break;
                case R.id.tv_dialog_perinfo_secret:
                    //点击保密
                    tvPerinfoSex.setText(tvDialogPerinfoSecret.getText());
                    user.setUser_sex(tvDialogPerinfoSecret.getText().toString());
                    bottomSheetDialog.dismiss();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null,
                    null, null);
            if (cursor.moveToFirst()) {
                String imgPath = cursor.getString(cursor.getColumnIndex("_data"));
                RequestOptions options = new RequestOptions()
                        .circleCrop();
                Glide.with(PerinfoActivity.this)
                        .load(imgPath)
                        .apply(options)
                        .into(ivPerinfoPortrait);
                this.imgPath = imgPath;
            }
        }
    }

    private void setListeners() {

        PerinfoListener perinfoListener = new PerinfoListener();

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

        //todo:设置头像，先从本地拿取头像信息，如果没有再从服务器上拿
//        RequestOptions options = new RequestOptions()
//                .signature(new ObjectKey(System.currentTimeMillis()))
//                .circleCrop();
//        Glide.with(PerinfoActivity.this)
//                .load(this.getFilesDir()+"/HeadPortrait.jpg")
//                .apply(options)
//                .into(ivPerinfoPortrait);

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .circleCrop();
        Glide.with(PerinfoActivity.this)
                .load(Info.BASE_URL+user.getUser_headportrait())
                .apply(options)
                .into(ivPerinfoPortrait);

<<<<<<< HEAD
        Log.e("test,setinfoglideurl",this.getFilesDir()+"/HeadPortrait.jpg");
        Log.e("user",user.toString());
=======
        Log.e("test,setinfoglideurl", this.getFilesDir() + "/HeadPortrait.jpg");

>>>>>>> f0662de151f70c0a92901e8a667bd848160b15b1
        tvPerinfoNickname.setText(user.getUser_nickname());
        tvPerinfoSex.setText(user.getUser_sex());
        tvPerinfoAge.setText(user.getUser_age().toString());
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

        ivPerinfoPortrait = findViewById(R.id.iv_perinfo_portrait);

        tvPerinfoNickname = findViewById(R.id.tv_perinfo_nickname);
        tvPerinfoSex = findViewById(R.id.tv_perinfo_sex);
        tvPerinfoAge = findViewById(R.id.tv_perinfo_age);
        tvPerinfoCity = findViewById(R.id.tv_perinfo_city);
        tvPerinfoPhone = findViewById(R.id.tv_perinfo_phone);
        tvPerinfoEmail = findViewById(R.id.tv_perinfo_email);
        tvPerinfoSignature = findViewById(R.id.tv_perinfo__signature);
        tvPerinfoSave = findViewById(R.id.tv_perinfo_save);

        llPerinfoPortrait = findViewById(R.id.ll_perinfo_portrait);
        llPerinfoNickname = findViewById(R.id.ll_perinfo_nickname);
        llPerinfoSex = findViewById(R.id.ll_perinfo_sex);
        llPerinfoAge = findViewById(R.id.ll_perinfo_age);
        llPerinfoCity = findViewById(R.id.ll_perinfo_city);
        llPerinfoPhone = findViewById(R.id.ll_perinfo_phone);
        llPerinfoEmail = findViewById(R.id.ll_perinfo_email);
        llPerinfoSignature = findViewById(R.id.ll_perinfo_signature);
        llPerinfoVip = findViewById(R.id.ll_perinfo_vip);

    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
