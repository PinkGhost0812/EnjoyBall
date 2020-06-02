package com.example.lenovo.Fragment;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AddGameDiaglog extends DialogFragment {

    private TextView tvTeama;
    private TextView tvTeamb;
    private EditText etScorea;
    private EditText etScoreb;
    private Button btnAdd;
    private OkHttpClient okHttpClient;
    private String Teama;
    private String Teamb;
    private int gameId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gamedialog_layout,container,false);

        Teama = (String)getArguments().get("teama");
        Teamb = (String)getArguments().get("teamb");
        //gameId = (int)getArguments().get("gameid");
        gameId = getActivity().getIntent().getIntExtra("gameid",10);
        MyListener myListener = new MyListener();
       tvTeama = view.findViewById(R.id.tv_team_a);
        tvTeamb = view.findViewById(R.id.tv_team_b);
        etScorea = view.findViewById(R.id.et_score_a);
        etScoreb = view.findViewById(R.id.et_score_b);
        btnAdd = view.findViewById(R.id.btn_add_score);
        btnAdd.setOnClickListener(myListener);
        tvTeama.setText(Teama);
        tvTeamb.setText(Teamb);

        return view;
    }

    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_add_score:
                    if (!etScorea.getText().toString().isEmpty() && !etScoreb.getText().toString().isEmpty()){
                        String a = etScorea.getText().toString().trim();
                        String b = etScoreb.getText().toString().trim();
                        String result = a+"-"+b;
                        Log.e("弹窗比赛id",gameId+"");
                        Log.e("弹窗内容",result);
                        String score = "1-3";
                        okHttpClient = new OkHttpClient();
                        Request request = new Request.Builder().url(Info.BASE_URL + "contest/upload?id=" + gameId + "&result=" + score).build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(getContext(), "上传失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Looper.prepare();
                                Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                                getDialog().dismiss();//关闭窗口
                                Looper.loop();
                            }
                        });

                    }else {
                        Toast.makeText(getContext(), "得分不能为空", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

}
