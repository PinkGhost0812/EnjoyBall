package com.example.lenovo.Activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.PYQ;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnnounceActivity extends AppCompatActivity {

    ImageView add;

    private OkHttpClient okHttpClient;

    private User user;

    private String imgPath = "0";

    private String content = null;

    private EditText ed= null;

    private PYQ pyq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce);
        user = ((Info) getApplicationContext()).getUser();

        TextView upPic = findViewById(R.id.tv_announce_submit);

        ed = findViewById(R.id.et_anno_write);

        add = findViewById(R.id.img_An_pic);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AnnounceActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
                Log.e("click", "clicK");
            }
        });

        upPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = ed.getText().toString();
                if(content!=null){
                    if(imgPath.equals("0")){
                        upload1(content);
                    }else {
                        upload(imgPath);

                    }
                }else{
                    Looper.prepare();
                    Toast.makeText(AnnounceActivity.this, "请填写内容", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }


        });
    }

    private void upload1(String content) {
        pyq.setUserId(user.getUser_id());
        pyq.setContent(content);
        if(imgPath=="0"){
            pyq.setImg(null);
        }else{
            pyq.setImg(imgPath);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(df.format(new Date()));
        pyq.setTime(df.format(new Date()));


        okHttpClient = new OkHttpClient();
        Gson gson = new GsonBuilder().create();
        String pyqinfo = gson.toJson(pyq);
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/launch?info=" + pyqinfo)
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
                String data = response.body().string();
                if (data.equals("true")) {
                    Toast.makeText(AnnounceActivity.this, "发布朋友圈成功~", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.setClass(PerinfoActivity.this, MainActivity.class);
                    finish();
                    //startActivity(intent);
                } else {
                    Toast.makeText(AnnounceActivity.this, "发布朋友圈失败~请重试", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        });

    }



    private void upload(String imgPath) {
        //上传到服务器
        File file = new File(imgPath);
        Log.e("test-upimgpath", imgPath);
        okHttpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("image/*"),
                file);
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/uploadPyqImg?id=" + imgPath)
                .post(body)
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
                String data = response.body().string();
                if (data.equals("true")) {
                    Toast.makeText(AnnounceActivity.this, "上传图片成功~", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.setClass(PerinfoActivity.this, MainActivity.class);
//                    finish();
                    //startActivity(intent);
                    upload1(content);
                } else {
                    Toast.makeText(AnnounceActivity.this, "上传图片失败~请重试", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        });
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
                Glide.with(AnnounceActivity.this)
                        .load(imgPath)
                        .apply(options)
                        .into(add);
                this.imgPath = imgPath;
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
}
