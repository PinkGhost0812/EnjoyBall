package com.example.lenovo.Activity;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lenovo.enjoyball.R;

import org.greenrobot.eventbus.EventBus;

public class PerinfoSignatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_perinfo_signature);

        Button btnPerinfoSignature=findViewById(R.id.btn_perinfo_signature);
        final EditText etPerinfoSignature=findViewById(R.id.et_perinfo_signature);

        btnPerinfoSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String signature=etPerinfoSignature.getText().toString();
                Message msg=new Message();
                msg.what=6;
                msg.obj=signature;
                EventBus.getDefault().post(msg);
                finish();
            }
        });

    }
}
