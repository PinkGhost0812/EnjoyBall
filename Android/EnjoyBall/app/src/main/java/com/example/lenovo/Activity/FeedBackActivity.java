package com.example.lenovo.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lenovo.enjoyball.R;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FeedBackActivity extends AppCompatActivity {
    private EditText mailText;
    private EditText mailPhone;
    private TextView feedback;
    private MyMailTask myMailTask;
    private String content;
    private String phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        findView();



        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = mailText.getText().toString();
                phone = mailPhone.getText().toString();
                myMailTask=new MyMailTask();
                myMailTask.execute();
            }
        });


    }

    private class MyMailTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            content = mailPhone.getText().toString();
            Log.e("content",content);
            send();
            return null;
        }
    }

    private void findView() {
        mailText = findViewById(R.id.et_feedback_write);
        mailPhone = findViewById(R.id.et_feedback_phone);
        feedback = findViewById(R.id.tv_feedback_submit);
    }
    private void send() {
        try {
            Properties props = new Properties();
            // 开启debug调试
            props.setProperty("mail.debug", "true");
            // 发送服务器需要身份验证
            props.setProperty("mail.smtp.auth", "true");
            //设置邮件服务器主机名
            props.setProperty("mail.host", "smtp.163.com");
            // 设置邮件服务器主机名
//            props.setProperty("mail.host", "smtp.qq.com");
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", "smtp");

            // 设置环境信息
            Session session = Session.getInstance(props);

            // 创建邮件对象

//            Message msg = new MimeMessage(session);
            Message msg = new MimeMessage(session);
            msg.setSubject(mailPhone.getText().toString());
            // 设置邮件内容
            msg.setText(mailText.getText().toString());
            // 设置发件人
            msg.setFrom(new InternetAddress("zwt12127@163.com"));
            Transport transport = session.getTransport();
            // 连接邮件服务器
            transport.connect("zwt12127@163.com", "zwt991215");
            // 发送邮件
            transport.sendMessage(msg, new Address[]{new InternetAddress("enjoyballmes@163.com")});
            // 关闭连接
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
