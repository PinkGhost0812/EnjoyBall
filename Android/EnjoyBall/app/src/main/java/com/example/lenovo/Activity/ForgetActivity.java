package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.Util.CodeTimeUtil;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.OnSendMessageHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.lenovo.enjoyball.R;

public class ForgetActivity extends AppCompatActivity {

    private ForgetLiseners forgetLiseners;
    private ImageView ivBack;
    private ImageView ivEye;
    private EditText etForgetPhone;
    private EditText etForgetCode;
    private EditText etForgetPwd;
    private Button btnCode;
    private Button btnChangeCode;
    private Handler handler;
    private String phone;
    private CodeTimeUtil codeTimeUtil;
    private int eye = 0;
    private int num = 0;
    private boolean go = false;
    private OkHttpClient okHttpClient;
    private String pwd;
    private OnSendMessageHandler onSendMessageHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_forget);



        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0){
                    Intent intent=new Intent(ForgetActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        onSendMessageHandler = new OnSendMessageHandler() {
            @Override
            public boolean onSendMessage(String s, String s1) {
                return false;
            }
        };

        findView();
        setListeners();
    }

    private void setListeners(){
        forgetLiseners = new ForgetLiseners();
        ivBack.setOnClickListener(forgetLiseners);
        btnCode.setOnClickListener(forgetLiseners);
        btnChangeCode.setOnClickListener(forgetLiseners);
        ivEye.setOnClickListener(forgetLiseners);
    }

    private void findView(){
        okHttpClient = new OkHttpClient();
        ivBack = findViewById(R.id.iv_forget_back);
        ivEye = findViewById(R.id.iv_forget_eye);
        btnCode = findViewById(R.id.btn_forget_code);
        btnChangeCode = findViewById(R.id.btn_forget_changecode);
        etForgetPhone = findViewById(R.id.et_forget_phone);
        etForgetCode = findViewById(R.id.et_forget_code);
        etForgetPwd = findViewById(R.id.et_forget_pwd);
    }

    public class ForgetLiseners implements View.OnClickListener{

        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.iv_forget_back:
                    Intent intent = new Intent();
                    intent.setClass(ForgetActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                case R.id.btn_forget_code:
                    regist();//调用验证短信发送的回调接口
                    //判断是否为null或“”
                    if (TextUtils.isEmpty(etForgetPhone.getText().toString())) {
                        Toast.makeText(ForgetActivity.this, "请输入合法的手机号", Toast.LENGTH_SHORT).show();
                    } else {
                        if (onSendMessageHandler.onSendMessage("86",etForgetPhone.getText().toString()))
                            Toast.makeText(ForgetActivity.this, "请您输入合法的手机号", Toast.LENGTH_SHORT).show();
                        else
                            SMSSDK.getVerificationCode("86", etForgetPhone.getText().toString());
                    }
                    break;
                case R.id.btn_forget_changecode:
                    if (TextUtils.isEmpty(etForgetPhone.getText().toString()) || etForgetPhone.getText().toString().length()!=11){
                        Toast.makeText(ForgetActivity.this, "请检验您输入的手机号", Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.isEmpty(etForgetCode.getText().toString())) {
                            Toast.makeText(ForgetActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            if (TextUtils.isEmpty(etForgetPwd.getText().toString())) {
                                Toast.makeText(ForgetActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                regist();
                                SMSSDK.submitVerificationCode("86", etForgetPhone.getText().toString(), etForgetCode.getText().toString());
//                                if (go)
                                    //forgrt(num);
                            }
                        }
                    }

                    break;
                case R.id.iv_forget_eye:
                    if(eye==0){
                        eye=1;
                        ivEye.setImageResource(R.drawable.eye);
                        etForgetPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }else{
                        eye=0;
                        ivEye.setImageResource(R.drawable.noteye);
                        etForgetPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    break;
            }
        }
    }

    public void regist(){
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                num = 1;
                                forgrt(num);
                                return ;
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ForgetActivity.this, "已发送验证码，请查收", Toast.LENGTH_SHORT).show();
                                codeTimeUtil = new CodeTimeUtil(btnCode,31000,1000);
                                codeTimeUtil.start();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (num!=1)
                                Toast.makeText(ForgetActivity.this, "操作失败，请重新获取验证码", Toast.LENGTH_SHORT).show();
                        }
                    });
                    ((Throwable) data).printStackTrace();
                }

            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    public void forgrt(int x){
        if (x==1){
            phone = etForgetPhone.getText().toString().trim();
            pwd = etForgetPwd.getText().toString().trim();
//            JPushInterface.setDebugMode(true);
//            JPushInterface.init(this);
//            String r = JPushInterface.getRegisterationID(this);
//            Log.e("1099","id"+r);

            //手机号密码规则
            if (phone.length() == 11 && pwd.length() != 0) {
                final Request request = new Request.Builder().url(com.example.lenovo.enjoyball.Info.BASE_URL +"user/updatePwd?phone="+phone+"&pwd="+pwd).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "服务器出错了，请稍后重试", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Looper.prepare();
                        String x = response.body().string();
                        Log.e("注册",x);
                        if (x.equals("true")){
                            Toast.makeText(ForgetActivity.this, "修改密码成功，即将返回登录界面", Toast.LENGTH_SHORT).show();
                            new Thread(){
                                @Override
                                public void run() {
                                    try{
                                        Thread.sleep(2000);
                                        Message message=new Message();
                                        message.what=0;
                                        handler.sendMessage(message);
                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }
                                }
                            }.start();

                        }else {
                            Toast.makeText(ForgetActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                        }

                        Looper.loop();

                    }
                });
            }
            num=0;
        }
    }

}
