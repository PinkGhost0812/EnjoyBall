package com.example.lenovo.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lenovo.Adapter.HomepageFollowAdapter;
import com.example.lenovo.enjoyball.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomepageFollowFragment extends Fragment {

    private View getView;

    private ListView lvHomepageFollow;

    private List<Map<String, Object>> dataSource = null;

    List<Map<String, Object>> mapList = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_homepage_follow,container, false);

        getView=view;

<<<<<<< Updated upstream
=======
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        info=new Info();

        user=info.getUser();

        user=new User();
        user=new User(1,"2","3","4","5","6","7","8","9",10,11,12,13);

>>>>>>> Stashed changes
        findView();

        getConnect();

        initData();

        HomepageFollowAdapter adapter=new HomepageFollowAdapter
                (getContext(),dataSource,R.layout.listview_item_follow);

        lvHomepageFollow.setAdapter(adapter);

        return view;

    }

    private void initData() {

        String[] nicknames= {"派大汤","派大汤","派大汤"};
        String[] sexs = {"男","女","男"};
        String[] ages={"18","19","20"};

        dataSource = new ArrayList<>();
        for(int i=0;i<nicknames.length;++i){
            Map<String,Object> map = new HashMap<>();
            map.put("nickname",nicknames[i]);
            map.put("sex",sexs[i]);
            map.put("age",ages[i]);
            dataSource.add(map);
        }
    }

<<<<<<< Updated upstream
    private void getConnect() {
=======
    private void getFollow() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/getfollow?id=" + user.getUser_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getActivity().getApplicationContext(), "获取关注失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new GsonBuilder()
                        .create();
                Type listType = new TypeToken<List<User>>(){}.getType();
                userList = gson.fromJson(response.body().string(),listType);
                EventBus.getDefault().post(userList);
            }
        });

>>>>>>> Stashed changes
    }

    private void findView() {
        lvHomepageFollow=getView.findViewById(R.id.lv_homepage_follow);
    }
}
