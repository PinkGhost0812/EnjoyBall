package com.example.lenovo.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.Activity.ManageMessageActivity;
import com.example.lenovo.Adapter.AgreementAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.DemandInfo;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreatePersonAgreementFragment extends Fragment {
    private Spinner sp_type = null;
    private Spinner sp_address = null;
    private Spinner sp_num = null;
    private Spinner sp_visiblity = null;
    private EditText et_say = null;
    private GridView gv_team = null;
    private Button btn_invite = null;
    private Button btn_OK = null;
    private AgreementAdapter adapter;
    private List<Map<String, Object>> datasource = new ArrayList<Map<String, Object>>();
    private String url = Info.BASE_URL + "appointment/add";
    private int type = -1;
    private Date time = null;
    private String address = null;
    private int visiblity = -1;
    private String description = null;
    private int num = 0;
    private int oom = 0;
    private TextView tv_getTime;
    private TextView tv_getData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agreement_person, null);
        getView(view);
        getTeamInfo();
        btn_OK.setOnClickListener(new OnClicked());
        btn_invite.setOnClickListener(new OnClicked());
        tv_getTime.setOnClickListener(new OnClicked());
        tv_getData.setOnClickListener( new OnClicked());
        return view;
    }

    //获取用户输入的约球信息的值
    private void getTeamInfo() {
        sp_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] nums = getResources().getStringArray(R.array.num);
                num = Integer.parseInt(nums[position]);
                initData(num);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_visiblity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                visiblity = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] addresses = getResources().getStringArray(R.array.address);
                address = addresses[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //初始化当前页面的内容
    private void initData(int num) {
        datasource.clear();
        Log.e("num", num + "");
        //获取当前登录用户的信息
        User user = getUser();
        String name = user.getUser_nickname();
        Log.e("登陆的用户名", name);
        /*若头像存储在本地*/
       /* File file = new File(head);
        if (file.exists()){
            bitmap = BitmapFactory.decodeFile(head);
        }else {

        }
        headDrawable = new BitmapDrawable(bitmap);*/
        /*若头像存储在服务器*/
        String imgUrl = Info.BASE_URL + user.getUser_headportrait();
        Map map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("head", imgUrl);
        map.put("status", "1");
        datasource.add(map);
        for (int i = 0; i < (2 * num - 1); i++) {
            name = "虚位以待";
            Log.e("默认的名字", name);
            Drawable drawable = getResources().getDrawable(R.drawable.head_girl);
            Map<String, Object> emptyUser = new HashMap<String, Object>();
            emptyUser.put("name", name);
            emptyUser.put("head", drawable);
            emptyUser.put("status", "0");
            datasource.add(emptyUser);
            adapter = new AgreementAdapter(datasource, R.layout.listview_item_agreement, getActivity());
            gv_team.setAdapter(adapter);

        }

    }


    private void getView(View view) {
        tv_getData = view.findViewById(R.id.tv_getData);
        tv_getTime = view.findViewById(R.id.tv_getTime);
        et_say = view.findViewById(R.id.et_createagreement_say);
        btn_OK = view.findViewById(R.id.btn_createagreement_OK);
        btn_invite = view.findViewById(R.id.btn_createagreement_invite);
        gv_team = view.findViewById(R.id.gv_createagreement_team);
        sp_address = view.findViewById(R.id.sp_createagreement_address);
        sp_num = view.findViewById(R.id.sp_createagreement_num);
        sp_type = view.findViewById(R.id.sp_createagreement_type);
        sp_visiblity = view.findViewById(R.id.sp_createagreement_visiblity);
    }

    public class OnClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_createagreement_OK:
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    String timeStr = tv_getData.getText().toString()+" "+tv_getTime.getText().toString();
                    Log.e("时间",timeStr);
                    try {
                        time = simpleDateFormat.parse(timeStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (time==null) {
                        Toast.makeText(getContext(), "请选择时间", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    Object object = et_say.getText();
                    if (object==null){
                        description = new String();
                    }else {
                        description = object.toString();
                    }
                    AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity());
                    adBuilder.setTitle("确认");
                    adBuilder.setMessage("确认创建约球吗");
                    adBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //向服务器发送信息
                            sendToServer();
                        }
                    });
                    adBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("create?", "false");
                        }
                    });
                    AlertDialog alertDialog = adBuilder.create();
                    alertDialog.show();
                    break;
                case R.id.btn_createagreement_invite:
                    Intent intent = new Intent(getActivity(), ManageMessageActivity.class);
                    intent.putExtra("table", "user");
                    intent.putExtra("hint", "请输入用户的手机号码");
                    startActivity(intent);
                    break;
                case R.id.tv_getData:
                    Log.e("获取时间","日期");
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                    bottomSheetDialog.setContentView(R.layout.layout_bottomsheetdialog_data);
                    bottomSheetDialog.setCanceledOnTouchOutside(false);
                    bottomSheetDialog.show();
                    Button btn_OK = bottomSheetDialog.findViewById(R.id.btn_bottomsheetdialog_OK);
                    btn_OK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                        }
                    });
                    DatePicker datePicker = bottomSheetDialog.findViewById(R.id.dp_data);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear+1;
                                String monthStr;
                                String dayStr;
                                if (dayOfMonth<10){
                                    dayStr ="0"+dayOfMonth;
                                }else {
                                    dayStr = ""+dayOfMonth;
                                }
                                if (month<10){
                                    monthStr ="0"+month;
                                }else {
                                    monthStr = ""+month;
                                }

                                tv_getData.setText(year + "-" +monthStr  + "-" + dayStr);

                            }
                        });
                    }
                    break;
                case R.id.tv_getTime:
                    Log.e("获取时间","时间");
                    final BottomSheetDialog bottomSheetDialogTime = new BottomSheetDialog(getContext());
                    bottomSheetDialogTime.setContentView(R.layout.layout_bottomsheetdialog_time);
                    bottomSheetDialogTime.setCanceledOnTouchOutside(false);
                    bottomSheetDialogTime.show();
                    Button btn_OKTime = bottomSheetDialogTime.findViewById(R.id.btn_bottomsheetdialog_OK);
                    btn_OKTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialogTime.cancel();
                        }
                    });
                    TimePicker timePicker = bottomSheetDialogTime.findViewById(R.id.tp_time);
                    timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            String hourStr;
                            String minuteStr;
                            if (minute<10){
                                minuteStr = "0"+minute;
                            }else {
                                minuteStr = ""+minute;
                            }
                            if (hourOfDay<10){
                                hourStr = "0"+hourOfDay;
                            }else {
                                hourStr = ""+hourOfDay;
                            }
                            tv_getTime.setText(hourStr+":"+minuteStr);
                        }
                    });
                    break;



            }
        }
    }

    //向服务器发送请求
    private void sendToServer() {
        User user = getUser();
        int userId = user.getUser_id();
        DemandInfo info = new DemandInfo();
        info.setDemand_user(userId);
        info.setDemand_class(type);
        info.setDemand_time(time);
        info.setDemand_place(address);
        info.setDemand_visibility(visiblity);
        info.setDemand_description(description);
        info.setDemand_num(num);
        info.setDemand_oom(oom);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url + "?info=" + gson.toJson(info))
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getContext(), "服务器连接失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.equals("true")) {
                    Looper.prepare();
                    Toast.makeText(getContext(), "约球队伍创建成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    Looper.prepare();
                    Toast.makeText(getContext(), "约球队伍创建失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }
            }
        });

    }

    //获取当前登陆的用户的信息
    private User getUser() {
        /*User user = getContext().getApplicationContext().getUser();*/
        User user = new User(1, "李烦烦", "990812", "img/pm.png", "男", "18103106427", "505", "631530326@qq.com", "我是你爹", 500, 600, 1,18,"111");
        return user;
    }
}
