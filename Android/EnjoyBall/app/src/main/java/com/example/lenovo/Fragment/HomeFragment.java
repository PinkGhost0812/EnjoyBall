package com.example.lenovo.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lenovo.Adapter.NewsAdapter;
import com.example.lenovo.enjoyball.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private List<Map<String,Object>> dataSource = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab_home_layout,
                container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

        ListView listView = getActivity().findViewById(R.id.lv_home_news);
        final NewsAdapter adapter = new NewsAdapter(
                getContext(),
                dataSource,
                R.layout.listview_item_home
        );

        listView.setAdapter(adapter);
    }

    private void initData() {
        int[] imgs = {R.drawable.test1,R.drawable.test1,R.drawable.test1,R.drawable.test1,R.drawable.test1,R.drawable.test1};
        String[] contents = {"欧洲冠军大逃亡:2连败 净胜球...","本地测试数据...","欧洲冠军大逃亡:2连败 净胜球...","欧洲冠军大逃亡:2连败 净胜球...","本地测试数据..","本地测试数据..."};
        String[] heat = {"1222","55","8888","9999","5554","8886"};
        dataSource = new ArrayList<>();
        for(int i=0; i<imgs.length; ++i) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", imgs[i]);
            map.put("content", contents[i]);
            map.put("heat", heat[i]);
            dataSource.add(map);
        }
    }
}