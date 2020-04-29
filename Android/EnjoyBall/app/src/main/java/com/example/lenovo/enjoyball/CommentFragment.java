package com.example.lenovo.enjoyball;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.Util.AuthorAndComment;
import com.example.lenovo.entity.Comment;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
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

import static com.mob.tools.utils.DeviceHelper.getApplication;

public class CommentFragment extends Fragment {
    private int gameid;
    private OkHttpClient okHttpClient;
    private List<Map<String, Object>> dataSource = null;
    private String jsonarray = null;
    private List<Comment> Battlecomments = null;
    private User user = null;
    private ListView listView;
    private List<String> name;
    private List<String> head;
    private List<User> authors;
    private ImageView likenum;
    private ImageView send;
    private EditText ed_comment;
    private Comment newcomment;
    private CommentAdapter adapter;
    private List<AuthorAndComment> list;
    private int type;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment,container,false);
        listView = view.findViewById(R.id.lv_contestDetail_comment);
        send = view.findViewById(R.id.iv_fragcom_releaseComment);
        ed_comment = view.findViewById(R.id.ed_fragcom_commentContent);
        EventBus.getDefault().register(this);
        gameid = getArguments().getInt("game");
        getCommentAndAuthor();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });

        return view;
    }

    private void init(List<AuthorAndComment> lists) {
        dataSource = new ArrayList<>();
        for(int i=0;i<lists.size();++i){
            Map<String,Object> map= new HashMap<>();
            map.put("users",lists.get(i).getAuthor());
            map.put("ids",lists.get(i).getComment().getComment_id());
            map.put("likenums",lists.get(i).getComment().getComment_likenum());
            map.put("nicknames",lists.get(i).getAuthor().getUser_nickname());
            map.put("heads",lists.get(i).getAuthor().getUser_headportrait());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("times",simpleDateFormat.format(lists.get(i).getComment().getComment_time()));
            map.put("contents",lists.get(i).getComment().getComment_content());
            dataSource.add(map);
        }
    }

//    private void FindName(int userid) {
//        okHttpClient = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .url(Info.BASE_URL+"user/find?id="+userid)
//                .build();
//
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                User author = new User();
//                authors = new ArrayList<>();
//                name = new ArrayList<>();
//                head = new ArrayList<>();
//                jsonarray = response.body().string();
//                Log.e("mes",jsonarray);
//                Gson gson = new GsonBuilder()
//                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
//                        .setPrettyPrinting()
//                        .serializeNulls()
//                        .create();
//                author = gson.fromJson(jsonarray,User.class);
//                authors.add(author);
//                name.add(author.getUser_nickname());
//                head.add(author.getUser_headportrait());
//                if(num==Battlecomments.size()-1)
//                    EventBus.getDefault().post("作者名字已查到");
//            }
//        });
//    }

//    private void FindComment(int gameid) {
//        okHttpClient = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .url(Info.BASE_URL+"information/findByContestId?id="+gameid)
//                .build();
//
//
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Battlecomments = new ArrayList<>();
//                jsonarray = response.body().string();
//                Gson gson = new GsonBuilder()
//                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
//                        .setPrettyPrinting()
//                        .serializeNulls()
//                        .create();
//                Log.e("comment",jsonarray);
//                Type listType = new TypeToken<List<Comment>>(){}.getType();
//                Battlecomments= gson.fromJson(jsonarray,listType);
//                EventBus.getDefault().post("查询成功");
//            }
//        });
//    }

    private void getCommentAndAuthor() {
        Log.e("test gameid",gameid+"");
        okHttpClient=new OkHttpClient();
        Request request = new Request.Builder().url(Info.BASE_URL + "information/getCommentAndAuthorByContest?id=" + gameid).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getActivity(), "服务器被炸了，小李正在修复呢", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data=response.body().string();
                Log.e("test data",data.toString());
                Message msg = new Message();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-mm-dd hh:mm:ss")
                        .create();
                Type listType = new TypeToken<List<AuthorAndComment>>(){}.getType();
                list=gson.fromJson(data,listType);
                msg.what = 105;
                msg.obj = list;
                Log.e("test data",msg.obj.toString());
                EventBus.getDefault().post(msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void inits(String msg){
        switch (msg){
            case "查询成功":
                Log.e("test",Battlecomments.size()+"");
                for(int i=0;i<Battlecomments.size();++i){
//                    FindName(Battlecomments.get(i).getComment_author());
                }
                break;
            case "作者名字已查到" :
//                init();

//                adapter = new CommentAdapter(
//                        Battlecomments,
//                        authors,
//                        this.getContext(),
//                        dataSource,
//                        R.layout.comment_item
//                );

                //listView.setAdapter(adapter);
            case "添加新的评论":
                User user = ((Info)getActivity().getApplicationContext()).getUser();
                Map<String,Object> map= new HashMap<>();
                map.put("users",user);
                map.put("ids",newcomment.getComment_id());
                map.put("likenums",0);
                map.put("nicknames",user.getUser_nickname());
                map.put("heads",user.getUser_headportrait());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                map.put("times",simpleDateFormat.format(newcomment.getComment_time()));
                map.put("contents",newcomment.getComment_content());
                dataSource.add(map);
                adapter.notifyDataSetChanged();

        }
    }

    private void sendComment(){
        String content = ed_comment.getText().toString();
        if(content.equals("") || content == null){
            Looper.prepare();
            Toast.makeText(getContext(), "啥也没写，发啥评论", Toast.LENGTH_SHORT).show();
            Looper.loop();
            return;
        }

        Info info = (Info)getApplication();
        newcomment = new Comment();
        Integer author = info.getUser().getUser_id();
        Integer cla = getArguments().getInt("type");
        Integer likeNum = 0;
        Date date = new Date(System.currentTimeMillis());
        Log.e("mes",date.toString());
        Integer contest = getArguments().getInt("game");

        //属性赋值
        newcomment.setComment_author(author);
        newcomment.setComment_class(cla);
        newcomment.setComment_content(content);
        newcomment.setComment_likenum(likeNum);
        newcomment.setComment_time(date);
        newcomment.setComment_contest(contest);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        Request request = new Request.Builder().url(Info.BASE_URL + "information/addComment?info=" + gson.toJson(newcomment)).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getContext(), "实训项目写完了？还在这发评论", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ans = response.body().string();
                if(ans!="false")
                    EventBus.getDefault().post("添加新的评论");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setCommentAndAuthor(Message msg){
        if (msg.what==105){
            List<AuthorAndComment> lists= (List<AuthorAndComment>) msg.obj;
            init(lists);
            adapter = new CommentAdapter(
                    dataSource,
                    getContext(),
                    R.layout.comment_item
            );

            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
