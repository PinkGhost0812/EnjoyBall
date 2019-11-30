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

public class HomepageCommentFragment extends Fragment {

    private View getView;

    private ListView lvHomepageComment;

    private List<Map<String, Object>> dataSource = null;

    List<Map<String, Object>> mapList = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_homepage_comment,container, false);

        getView=view;
        
        findView();
        
        getConnect();

        initData();

        HomepageCommentAdapter adapter=new HomepageCommentAdapter
                (getContext(),dataSource,R.layout.listview_item_comment);

        lvHomepageComment.setAdapter(adapter);

        return view;

    }

    private void initData() {

        String[] comments = {"本赛季鲁能究竟能走多远，能否在足协杯上再创辉煌让我们拭目以待","本赛季鲁能究竟能走多远，能否在足协杯上再创辉煌让我们拭目以待"};
        String[] pages = {"【原文】鲁能与申花的足协杯决赛将于十二月举行","【原文】鲁能与申花的足协杯决赛将于十二月举行"};

        dataSource = new ArrayList<>();
        for(int i=0;i<comments.length;++i){
            Map<String,Object> map = new HashMap<>();
            map.put("comments",comments[i]);
            map.put("pages",pages[i]);
            dataSource.add(map);
        }

    }

    private void getConnect() {



    }

    private void findView() {
        lvHomepageComment=getView.findViewById(R.id.lv_homepage_comment);
    }
}
