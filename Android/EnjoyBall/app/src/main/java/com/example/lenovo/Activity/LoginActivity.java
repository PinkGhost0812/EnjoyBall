package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import cn.smssdk.gui.util.GUISPDB;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.lenovo.enjoyball.R;

public class LoginActivity extends AppCompatActivity {

    private LoginListeners loginListeners;
    private Button btLogin;
    private ImageView ivEye;
    private TextView tvRegistered;
    private TextView tvForget;
    private EditText etPhone;
    private EditText etPwd;
    private String phone;
    private String pwd;
    private int eye = 0;
    private OkHttpClient okHttpClient;

    private Handler handler;
    List<Map<String, Object>> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_login);
        findView();

        setListeners();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }
        };
    }

    private void setListeners() {
        loginListeners = new LoginListeners();
        btLogin.setOnClickListener(loginListeners);
        tvRegistered.setOnClickListener(loginListeners);
        tvForget.setOnClickListener(loginListeners);
        ivEye.setOnClickListener(loginListeners);

    }
    private void findView(){
        okHttpClient = new OkHttpClient();
        btLogin = findViewById(R.id.bt_login_lg);
        tvRegistered = findViewById(R.id.tv_login_register);
        tvForget = findViewById(R.id.tv_login_forget);
        etPhone = findViewById(R.id.et_login_phone);
        etPwd = findViewById(R.id.et_login_pwd);
        ivEye = findViewById(R.id.iv_login_eye);
    }

    private class LoginListeners implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_login_lg:
                    landing();
//                    Intent intent=new Intent();
//                    intent.setClass(LoginActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
                    break;
                case R.id.tv_login_forget:
                    Intent intent1=new Intent();
                    intent1.setClass(LoginActivity.this,ForgetActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.tv_login_register:
                    Intent intent2=new Intent();
                    intent2.setClass(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.iv_login_eye:
                    if(eye==0){
                        eye=1;
                        ivEye.setImageResource(R.drawable.eye);
                        etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }else{
                        eye=0;
                        ivEye.setImageResource(R.drawable.noteye);
                        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    break;
            }
        }
    }

    public void flush(){
        finish();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    public void landing() {
        phone = etPhone.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();

        //手机号密码规则
        if (phone.length() == 11 && pwd.length() != 0) {
//            User user = new User(null,null,pwd,null,null,phone,null,null,null,null,null,null);
//            String info = new Gson().toJson(user);
            Request request = new Request.Builder().url(com.example.lenovo.enjoyball.Info.BASE_URL + "user/findByPhoneNumber?phone=" + phone).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "登录失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String userJsonStr = response.body().string();
                    Log.e("获取",userJsonStr);
                    if (userJsonStr.equals( "false")){
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "手机号未注册", Toast.LENGTH_SHORT).show();
                        flush();
                        Looper.loop();
                    }else {
                        User u = new Gson().fromJson(userJsonStr,User.class);
                        if (u.getUser_password().equals(pwd)) {
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Log.e("用户",u.toString());


                            /*20191208之前
                                Info info=new Info();
                                info.setUser(u);
                                intent.putExtra("user",u);
                             */
                            ((Info)getApplication()).setUser(u);

                            Intent intent=new Intent();
                            intent.setClass(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();


                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }

                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "账号或密码格式错误", Toast.LENGTH_LONG).show();
        }
    }
}
