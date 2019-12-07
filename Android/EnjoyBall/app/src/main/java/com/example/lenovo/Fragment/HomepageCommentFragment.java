package com.example.lenovo.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lenovo.Adapter.HomepageCommentAdapter;
import com.example.lenovo.enjoyball.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

<<<<<<< Updated upstream
public class HomepageCommentFragment extends Fragment {

    private View getView;

    private ListView lvHomepageComment;
=======
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomepageCommentFragment extends Fragment {

    private View getView;

    private ListView lvHomepageComment;

    private List<Map<String, Object>> dataSource = null;
    private List<CommentAndNews> list;

    private OkHttpClient okHttpClient;
>>>>>>> Stashed changes

    private List<Map<String, Object>> dataSource = null;

<<<<<<< Updated upstream
    List<Map<String, Object>> mapList = null;
=======
    private Info info;

    private User user = null;

    private Handler handler;
>>>>>>> Stashed changes

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_homepage_comment, container, false);

<<<<<<< Updated upstream
        getView=view;
        
=======
        getView = view;

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        info = new Info();

        user = info.getUser();

        user = new User();

        user = new User(1, "2", "3", "4", "5", "6", "7", "8", "9", 10, 11, 12, 13);

>>>>>>> Stashed changes
        findView();
        
        getConnect();

<<<<<<< Updated upstream
        initData();
=======
        getComment();

        lvHomepageComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getContext(), NewsDetailActivity.class);
//                Log.e("test", list.toString());
//                Log.e("test", position + "");
//                Log.e("test", list.get(position).getNews().getNews_id().toString());

                intent.putExtra("homepage_news_id", list.get(position).getNews().getNews_id().toString());
                startActivity(intent);
            }
        });

        return view;

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setInfo(Message msg) {

        List<CommentAndNews> list = (List<CommentAndNews>) msg.obj;

        initData(list);
>>>>>>> Stashed changes

        HomepageCommentAdapter adapter = new HomepageCommentAdapter
                (getContext(), dataSource, R.layout.listview_item_comment);

        lvHomepageComment.setAdapter(adapter);

        return view;

    }

    private void initData() {

        String[] comments = {"本赛季鲁能究竟能走多远，能否在足协杯上再创辉煌让我们拭目以待","本赛季鲁能究竟能走多远，能否在足协杯上再创辉煌让我们拭目以待"};
        String[] pages = {"【原文】鲁能与申花的足协杯决赛将于十二月举行","【原文】鲁能与申花的足协杯决赛将于十二月举行"};

<<<<<<< Updated upstream
        dataSource = new ArrayList<>();
        for(int i=0;i<comments.length;++i){
            Map<String,Object> map = new HashMap<>();
            map.put("comments",comments[i]);
            map.put("pages",pages[i]);
=======
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("comments", list.get(i).getComment().getComment_content());
            map.put("news", list.get(i).getNews().getNews_title());
>>>>>>> Stashed changes
            dataSource.add(map);
        }

    }

<<<<<<< Updated upstream
    private void getConnect() {


=======
    private void getComment() {

        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Info.BASE_URL + "information/findSaying?id=" + 1)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getActivity().getApplicationContext(), "获取评论信息失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<CommentAndNews>>() {}.getType();
                list = gson.fromJson(response.body().string(), type);
                Log.e("test", list.toString());
                Message msg = new Message();
                msg.obj = list;
                EventBus.getDefault().post(msg);
            }
        });
>>>>>>> Stashed changes

    }

    private void findView() {
        lvHomepageComment = getView.findViewById(R.id.lv_homepage_comment);
    }
}
