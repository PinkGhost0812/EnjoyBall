package com.example.lenovo.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lenovo.Adapter.DemandAdapter;
import com.example.lenovo.enjoyball.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeFragment extends Fragment {

    private List<Map<String,Object>> dataSource = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab_demand_layout,
                container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

        ListView listView = getActivity().findViewById(R.id.lv_time_demand);
        final DemandAdapter adapter = new DemandAdapter(
                getContext(),
                dataSource,
                R.layout.listview_item_demand
        );

        listView.setAdapter(adapter);
    }

    private void initData() {

        String[] times = {"2019/11/8      14:00","2019/11/8      14:00","2019/11/8      14:00","2019/11/8      14:00"};
        String[] place = {"河北师范大学篮球场","河北师范大学篮球场","河北师范大学篮球场","河北师范大学篮球场"};
        String[] team = {"巴塞罗那","巴塞罗那","巴塞罗那","巴塞罗那"};
        String[] dp = {"缺少2名队友，求躺之ID反击拿福利君安法律","缺少2名队友，求躺之ID反击拿福利君安法律","缺少2名队友，求躺","缺少2名队友，求躺"};
        dataSource = new ArrayList<>();
        for(int i=0; i<times.length; ++i) {
            Map<String, Object> map = new HashMap<>();
            map.put("time", times[i]);
            map.put("place", place[i]);
            map.put("team", team[i]);
            map.put("dp", dp[i]);
            dataSource.add(map);
        }
    }
}
