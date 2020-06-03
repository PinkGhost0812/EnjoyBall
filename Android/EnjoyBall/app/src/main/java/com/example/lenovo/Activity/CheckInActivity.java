package com.example.lenovo.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckInActivity extends AppCompatActivity {

    private final int UPDATE_TEXT = 1;
    private int flag;//已签到为1，未签到为0
    private int id ;
    private User user = null;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_check_in);

        user = ((Info) getApplicationContext()).getUser();
        id = user.getUser_id();
        Date date = new Date();
        String week = getWeekOfDate(date);
        Calendar cale = null;
        cale = Calendar.getInstance();
        int month = cale.get(Calendar.MONTH) + 1;
        int day = cale.get(Calendar.DATE);

        TextView days = findViewById(R.id.tv_check_days);
        TextView mouths = findViewById(R.id.tv_check_mouthsandweek);

        //设置当天日期
        Log.e("day",""+day);
        Log.e("month",""+month);
        Log.e("week",""+week);
        if(day<10){
            days.setText("0"+day);
        }else{
            days.setText(""+day);
        }
        mouths.setText(month+"月 "+week);

        final Button checkin = findViewById(R.id.btn_checkin);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(df.format(new Date()));

        SharedPreferences sp=getSharedPreferences("" + id, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        flag = sp.getInt("flag",0);
        String predate = sp.getString("time","0");
        try {
            if(subDay(predate,df.format(new Date()))>0){
                Log.e("falg",""+subDay(predate,df.format(new Date())));
                flag=0;
                editor.putInt("flag",0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(predate.equals("0")||flag==1){
            checkin.setText("已签到");
            checkin.getBackground().setAlpha(100);
        }





        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 0){

                    //上次签到时间的存储
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    System.out.println(df.format(new Date()));
                    SharedPreferences sharedPreferences = getSharedPreferences(""+id, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("time",df.format(new Date()));
                    editor.putInt("flag",1);
                    editor.apply();
                    checkin.setText("已签到");
                    checkin.getBackground().setAlpha(100);
                    checkin.setEnabled(false);
                    addscore();
                }else{

                }
            }
        });


    }

    public void addscore() {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/addScore?id=" + user.getUser_id()+"&score="+10)
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
                    Toast.makeText(CheckInActivity.this, "签到成功，积分+10", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.setClass(PerinfoActivity.this, MainActivity.class);
//                    finish();
                    //startActivity(intent);
                } else {
                    Toast.makeText(CheckInActivity.this, "签到失败~请重试", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        });
    }

    public static int subDay(String lastTime, String nowTime) throws ParseException {
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = sdf.parse(nowTime);
        Date lastDate = sdf.parse(lastTime);
        return subtract(nowDate, lastDate);
    }


    public static int subtract(Date date1, Date date2) {

        return  (Math.abs((int) date1.getTime() - (int) date2.getTime()) / 3600000) / 24;
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param date
     * @return 当前日期是星期几
     */
    public String getWeekOfDate(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

}
