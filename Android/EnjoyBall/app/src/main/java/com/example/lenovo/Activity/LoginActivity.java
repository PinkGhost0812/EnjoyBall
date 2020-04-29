package com.example.lenovo.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.lenovo.enjoyball.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends AppCompatActivity {

    private LoginListeners loginListeners;
    private Button btLogin;
    private ImageView ivEye;
    private TextView tvRegistered;
    private TextView tvForget;
    private EditText etPhone;
    private EditText etPwd;
    private int eye = 0;
    private User u;
    private OkHttpClient okHttpClient;

    public int MSG_WHAT_INFO_AUTOLOGIN = 1;

    private Handler handler;
    List<Map<String, Object>> list = null;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_login);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        findView();

        setListeners();

        sharedPreferences = getSharedPreferences("loginInfo",
                MODE_PRIVATE);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }
        };

        autoLogin();
    }

    private void autoLogin() {

        if (sharedPreferences.getBoolean("isAuto", false)) {
            final String phoneAuto = sharedPreferences.getString("phone", "");
            final String passwordAuto = sharedPreferences.getString("password", "");

            Log.e("test phone", phoneAuto);
            Log.e("test pwd", passwordAuto);

            landing(phoneAuto, passwordAuto);

        } else {
            return;
        }
    }

    public void landing(final String phone, final String password) {

        Log.e("test phone", phone);
        Log.e("test pwd", password);

        //手机号密码规则
        if (phone.length() == 11 && password.length() != 0) {
            Request request = new Request.Builder().url(Info.BASE_URL + "user/findByPhoneNumber?phone=" + phone).build();
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
                    if (userJsonStr.equals("false")) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "手机号未注册", Toast.LENGTH_SHORT).show();
                        flush();
                        Looper.loop();
                    } else {
                        u = new Gson().fromJson(userJsonStr, User.class);
                        if (u.getUser_password().equals(password)) {
                            Looper.prepare();
                            OkHttpClient okHttpClient1 = new OkHttpClient();
                            Request request = new Request.Builder().url(Info.BASE_URL + "user/updateJpush?phone=" + phone + "&jpushId=" + Info.registrationId).build();
                            Call call1 = okHttpClient1.newCall(request);
                            call1.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(), "登录失败了，请稍后重试", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Looper.prepare();
                                    String user = response.body().string();
                                    if (user.equals("true")) {
                                        u.setUser_jpushid(Info.registrationId);
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                        ((Info) getApplication()).setUser(u);

                                        Message msg = new Message();
                                        msg.what = MSG_WHAT_INFO_AUTOLOGIN;
                                        String[] info = new String[2];
                                        info[0] = phone;
                                        info[1] = password;
                                        msg.obj = info;
                                        EventBus.getDefault().post(msg);

                                        Intent intent = new Intent();
                                        intent.putExtra("user", u);
                                        intent.setClass(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    Looper.loop();
                                }
                            });


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

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void saveAutoInfo(Message msg) {
        Log.e("test", "saveinfo");
        if (msg.what == MSG_WHAT_INFO_AUTOLOGIN) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            String[] info = (String[]) msg.obj;

            Log.e("test phone", info[0]);
            Log.e("test pwd", info[1]);

            editor.putString("phone", info[0]);
            editor.putString("password", info[1]);
            editor.putBoolean("isAuto", true);

            editor.commit();

            Log.e("test spphone bus", sharedPreferences.getString("phone", ""));
        }
    }

    private void setListeners() {
        loginListeners = new LoginListeners();
        btLogin.setOnClickListener(loginListeners);
        tvRegistered.setOnClickListener(loginListeners);
        tvForget.setOnClickListener(loginListeners);
        ivEye.setOnClickListener(loginListeners);

    }

    private void findView() {
        okHttpClient = new OkHttpClient();
        btLogin = findViewById(R.id.bt_login_lg);
        tvRegistered = findViewById(R.id.tv_login_register);
        tvForget = findViewById(R.id.tv_login_forget);
        etPhone = findViewById(R.id.et_login_phone);
        etPwd = findViewById(R.id.et_login_pwd);
        ivEye = findViewById(R.id.iv_login_eye);
    }

    public void flush() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private class LoginListeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_login_lg:
                    String phone = etPhone.getText().toString().trim();
                    String password = etPwd.getText().toString().trim();
                    landing(phone, password);
                    break;
                case R.id.tv_login_forget:
                    Intent intent1 = new Intent();
                    intent1.setClass(LoginActivity.this, ForgetActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.tv_login_register:
                    Intent intent2 = new Intent();
                    intent2.setClass(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.iv_login_eye:
                    if (eye == 0) {
                        eye = 1;
                        ivEye.setImageResource(R.drawable.eye);
                        etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        eye = 0;
                        ivEye.setImageResource(R.drawable.noteye);
                        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    break;
            }
        }
    }

}
