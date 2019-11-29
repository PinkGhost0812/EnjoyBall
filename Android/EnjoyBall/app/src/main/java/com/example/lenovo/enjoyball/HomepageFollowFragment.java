package com.example.lenovo.enjoyball;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    private void getConnect() {
    }

    private void findView() {
        lvHomepageFollow=getView.findViewById(R.id.lv_homepage_follow);
    }
}
