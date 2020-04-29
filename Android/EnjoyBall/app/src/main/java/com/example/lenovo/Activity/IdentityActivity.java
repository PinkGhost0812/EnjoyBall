package com.example.lenovo.Activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IdentityActivity extends AppCompatActivity {

    //niao
    private OkHttpClient okHttpClient;

    private User user;

    private String imgPath = "0";

    private ImageView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);

        user = ((Info) getApplicationContext()).getUser();

        add = findViewById(R.id.iv_identity);
        Button upPic = findViewById(R.id.btn_identity_uppic);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(IdentityActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            }
        });

        upPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgPath.equals("0")){
                    Looper.prepare();
                    Toast.makeText(IdentityActivity.this, "未选择图片", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else {
                    upload(imgPath);
                }
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
                .url(Info.BASE_URL + "user/uploadIdentify?id=" + user.getUser_id())
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
                    Toast.makeText(IdentityActivity.this, "上传认证信息成功~", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.setClass(PerinfoActivity.this, MainActivity.class);
//                    finish();
                    //startActivity(intent);
                } else {
                    Toast.makeText(IdentityActivity.this, "上传认证信息失败~请重试", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        });

        //将图片保存到本地
        try {
            String path = this.getFilesDir() + "/Identity.jpg";
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
                Glide.with(IdentityActivity.this)
                        .load(imgPath)
                        .apply(options)
                        .into(add);
                this.imgPath = imgPath;
            }
        }
    }


}
