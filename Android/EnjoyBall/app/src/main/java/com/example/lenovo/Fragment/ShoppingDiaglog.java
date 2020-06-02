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
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.StuffInfo;
import com.example.lenovo.entity.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ShoppingDiaglog extends DialogFragment {

    private TextView tvContent;
    private Button btnCancel;
    private Button btnConfirm;
    private OkHttpClient okHttpClient;
    private int buyscore;
    private int myscore;
    private int num;
    private int id;
    private String name;
    private User user;
    private StuffInfo shop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shoppingdiaglog_layout,container,false);

        buyscore = (int)getArguments().get("buyscore");
        num = (int) getArguments().get("num");
        id = (int) getArguments().get("shop_id");
        name = (String) getArguments().get("name");
//        user = (User)getArguments().get("user");
//        shop = (StuffInfo) getArguments().getSerializable("shop");
//        user = (User) getArguments().getSerializable("user");
        user = ((Info) getActivity().getApplicationContext()).getUser();
        Log.e("user",user.toString());
        Log.e("商品数量",num+"");
        myscore  =user.getUser_score();
        MyListener myListener = new MyListener();
        tvContent = view.findViewById(R.id.tv_shopping_dialog);
        btnCancel = view.findViewById(R.id.btn_shopping_cancel);
        btnConfirm = view.findViewById(R.id.btn_shopping_confirm);
        btnCancel.setOnClickListener(myListener);
        btnConfirm.setOnClickListener(myListener);
        tvContent.setText("尊敬的用户您好，您将要花费"+buyscore+"积分购买"+num+"张"+name+"，是否确认？");
//        tvContent.setText("尊敬的用户您好，您将要花费"+buyscore+"积分购买"+shop.getNumber()+"张"+shop.getName()+"，是否确认？");

        return view;
    }

    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_shopping_cancel:
                    getDialog().dismiss();
                    break;
                case R.id.btn_shopping_confirm:
                    int x=0;
                    if (myscore>buyscore){
                        x = myscore-buyscore;
                        Log.e("剩余积分",x+"");
                        okHttpClient =new OkHttpClient();
                        Request request = new Request.Builder().url(Info.BASE_URL + "user/buy?userId="+user.getUser_id()+"&packageId="+user.getUser_package()+"&stuffId="+id+"&num="+num+"&score=" + x).build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(getContext(), "购买失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                getDialog().dismiss();
                                Looper.loop();
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Looper.prepare();
                                Toast.makeText(getContext(), "恭喜您，购买成功！", Toast.LENGTH_SHORT).show();
                                getDialog().dismiss();//关闭窗口
                                Looper.loop();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getContext(), "抱歉您的积分不足，请努力做任务换取更多的积分！", Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    }
                    break;
            }
        }
    }

}
