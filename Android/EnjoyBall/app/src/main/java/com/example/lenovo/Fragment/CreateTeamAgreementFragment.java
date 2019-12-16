package com.example.lenovo.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lenovo.Activity.InviteActivity;
import com.example.lenovo.Adapter.AgreementAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.DemandInfo;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.IOException;
import java.lang.reflect.Type;
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
public class CreateTeamAgreementFragment extends Fragment {
    private Spinner sp_type = null;
    private Spinner sp_address = null;
    private Spinner sp_visiblity = null;
    private EditText et_say = null;
    private GridView gv_team = null;
    private Button btn_invite = null;
    private Button btn_OK = null;
    private AgreementAdapter adapter;
    private List<Map<String, Object>> datasource = new ArrayList<Map<String, Object>>();
    private String url = Info.BASE_URL;
    private int type = -1;
    private Date time = null;
    private String address = null;
    private int visiblity = -1;
    private String description = null;
    private int teamId = 0;
    private Team selectedTeam = null;
    private int oom = 1;
    private TextView tv_getTime;
    private TextView tv_getDate;
    private static  int FALSE = 0;
    private static int NETERROR = -1;
    private static int INITDATA = 1;
    private static int SAVEUSER = 2;
    private static int SAVETEAM = 3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_agreement_team, null);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        getView(view);
        getTeamInfo();
        btn_OK.setOnClickListener(new OnClicked());
        btn_invite.setOnClickListener(new OnClicked());
        tv_getTime.setOnClickListener(new OnClicked());
        tv_getDate.setOnClickListener(new OnClicked());

        return view;

    }

    private void initData(Team team) {
        datasource.clear();

        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map2 = new HashMap<String, Object>();
        if (team != null) {
            Log.e("队伍信息", "true");
            String imageUrl = Info.BASE_URL + team.getTeam_logo();
            map1.put("name", team.getTeam_name());
            map1.put("status", 1);
            map1.put("head", imageUrl);
            map1.put("object", team);
        } else {
            Log.e("队伍信息", "false");
            map1.put("status", 0);
            map1.put("name", "虚位以待");
            map1.put("head", getResources().getDrawable(R.drawable.head_girl));
            Toast.makeText(getContext(), "您没有创建该类型的队伍", Toast.LENGTH_SHORT);
        }
        map2.put("status", 0);
        map2.put("name", "虚位以待");
        map2.put("head", getResources().getDrawable(R.drawable.head_girl));
        datasource.add(map1);
        datasource.add(map2);
        adapter = new AgreementAdapter(datasource, R.layout.listview_item_agreement, getActivity());
        gv_team.setAdapter(adapter);

    }

    private void getView(View view) {
        et_say = view.findViewById(R.id.et_createagreement_say);
        tv_getDate = view.findViewById(R.id.tv_getData);
        tv_getTime = view.findViewById(R.id.tv_getTime);
        btn_invite = view.findViewById(R.id.btn_createagreement_invite);
        btn_OK = view.findViewById(R.id.btn_createagreement_OK);
        gv_team = view.findViewById(R.id.gv_createagreement_team);
        sp_address = view.findViewById(R.id.sp_createagreement_address);
        sp_type = view.findViewById(R.id.sp_createagreement_type);
        sp_visiblity = view.findViewById(R.id.sp_createagreement_visiblity);

    }

    //获取当前登陆的用户的信息
    private User getUser() {
        Info info = (Info)getActivity().getApplication();
        User user = info.getUser();
        return user;
    }

    public class OnClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //确认
                case R.id.btn_createagreement_OK:
                    if (datasource.get(0).get("object") == null) {
                        Toast.makeText(getContext(), "e创建失败", Toast.LENGTH_SHORT);
                        return;
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    String timeStr = tv_getDate.getText().toString() + " " + tv_getTime.getText().toString();
                    Log.e("时间", timeStr);
                    try {
                        time = simpleDateFormat.parse(timeStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (time == null) {
                        Toast.makeText(getContext(), "请选择时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Object object = et_say.getText();
                    if (object == null) {
                        description = new String();
                    } else {
                        description = object.toString();
                    }
                    AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity());
                    adBuilder.setTitle("确认");
                    adBuilder.setMessage("确认创建约球吗");
                    adBuilder.setPositiveButton("加入", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("create？", "true");
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
                //邀请
                case R.id.btn_createagreement_invite:
                    Intent intent = new Intent(getContext(), InviteActivity.class);
                    intent.putExtra("table", "team");
                    intent.putExtra("hint", "请输入队伍名");
                    getContext().startActivity(intent);
                    break;
                //选择日期
                case R.id.tv_getData:
                    Log.e("获取时间", "日期");
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
                                int month = monthOfYear + 1;
                                String monthStr;
                                String dayStr;
                                if (dayOfMonth < 10) {
                                    dayStr = "0" + dayOfMonth;
                                } else {
                                    dayStr = "" + dayOfMonth;
                                }
                                if (month < 10) {
                                    monthStr = "0" + month;
                                } else {
                                    monthStr = "" + month;
                                }

                                tv_getDate.setText(year + "-" + monthStr + "-" + dayStr);

                            }
                        });
                    }
                    break;
                //选择时间
                case R.id.tv_getTime:
                    Log.e("获取时间", "时间");
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
                            if (minute < 10) {
                                minuteStr = "0" + minute;
                            } else {
                                minuteStr = "" + minute;
                            }
                            if (hourOfDay < 10) {
                                hourStr = "0" + hourOfDay;
                            } else {
                                hourStr = "" + hourOfDay;
                            }
                            tv_getTime.setText(hourStr + ":" + minuteStr);
                        }
                    });
                    break;
            }
        }
    }

    //向服务器发送请求
    private void sendToServer() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("invited", Context.MODE_PRIVATE);
        String idList = sharedPreferences.getString("team","");
        DemandInfo info = new DemandInfo();
        info.setDemand_user(getUser().getUser_id());
        info.setDemand_description(description);
        info.setDemand_visibility(visiblity);
        info.setDemand_num(1);
        info.setDemand_place(address);
        info.setDemand_time(time);
        info.setDemand_class(type);
        info.setDemand_teama(selectedTeam.getTeam_id());
        info.setDemand_oom(oom);
        info.setDemand_num(1);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url + "appointment/addAppointmentWithInvite?demandInfo=" + gson.toJson(info)+"&&idList="+idList)
                .build();
        Call call = okHttpClient.newCall(request);
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

    //获取用户输入的约球信息
    private void getTeamInfo() {
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
                getTeams();

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

    //查询队长为当前用户的队伍信息
    private void getTeams() {

        Log.e("url", url + "team/findByIdCls?id=" + getUser().getUser_id() + "&cls=" + type);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url + "team/findByIdCls?id=" + getUser().getUser_id() + "&cls=" + type).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = NETERROR;
                EventBus.getDefault().post(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String teamJson = response.body().string();
                Log.e("查找队伍得到的信息", teamJson);
                if (!teamJson.equals("false")) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd hh:mm").create();
                    selectedTeam = gson.fromJson(teamJson, Team.class);
                    Message message = new Message();
                    message.what = INITDATA;
                    message.obj = selectedTeam;
                    Log.e("队名", selectedTeam.toString());
                    EventBus.getDefault().post(message);

                } else {
                    Message message = new Message();
                    message.what = FALSE;
                    EventBus.getDefault().post(message);
                }

            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setView(Message message) {
        Log.e("收到eventBus", message.obj + "");
        if (message.what == INITDATA) {
            initData(selectedTeam);
        } else if (message.what==FALSE){
            Toast.makeText(getContext(), "您没有创建该类型的球队，无法创建组队约战", Toast.LENGTH_SHORT).show();
            initData(null);
        }else if (message.what==NETERROR){
            Toast.makeText(getContext(), "无法连接到服务器", Toast.LENGTH_SHORT).show();
            initData(null);
        }else if (message.what == SAVETEAM){
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(getUser().getUser_id()+"", Context.MODE_PRIVATE);
            String teamsStr = sharedPreferences.getString("teams","");
            if (teamsStr!=null&&teamsStr.length()>0){
                Type usersType = new TypeToken<List<String>>(){}.getType();
                List<String> users = new Gson().fromJson(teamsStr,usersType);
                String invitedUser = message.obj.toString();
                users.add(invitedUser);
                teamsStr = new Gson().toJson(users);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("teams",teamsStr);


            }else {
                List<String> users = new ArrayList<String>();
                String invitedUser = message.obj.toString();
                users.add(invitedUser);
                teamsStr = new Gson().toJson(users);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("users",teamsStr);

            }


        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("invited",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", "");
        editor.putString("team","");
        editor.apply();
        super.onDestroy();
    }


}
