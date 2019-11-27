package com.example.lenovo.enjoyball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreatyballActivity extends AppCompatActivity {
    private List<Map<String,Object>> datasource = null;
    private Spinner spinner;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatyball);
        list=new ArrayList<String>();
        list.add("倒序");
        list.add("正序");
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.spin_treatyball_match);
        spinner.setAdapter(adapter);
        final TextView spin = findViewById(R.id.tv_treatyball_order);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter.getItem(position);
                spin.setText(name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        init();


        TreatyballListviewAdapter myadapter = new TreatyballListviewAdapter(
                this,
                datasource,
                R.layout.treatyball_item
        );
        ListView listView = findViewById(R.id.lv_treatyball_match);
        listView.setAdapter(myadapter);
    }

    private void init() {
        String[] teams1 = {"我是你爹","大头儿子"};
        String[] teams2 = {"我是你爹","小头爸爸"};
        String[] scores1 = {"?","52"};
        String[] scores2 = {"?","63"};
        String[] dates = {"2019/11/1","2018/11/1"};
        String[] times = {"17;00","08:00"};
        String[] places = {"河北师范大学篮球场","河北师范大学羽毛球场"};
        String[] symbols = {"未完成","已完成"};
        String[] types = {"篮球","羽毛球"};

        datasource = new ArrayList<>();
        for(int i=0;i<dates.length;++i){
            Map<String,Object> map = new HashMap<>();
            map.put("teams1",teams1[i]);
            map.put("teams2",teams2[i]);
            map.put("scores1",scores1[i]);
            map.put("scores2",scores2[i]);
            map.put("dates",dates[i]);
            map.put("times",times[i]);
            map.put("places",places[i]);
            map.put("symbols",symbols[i]);
            map.put("types",types[i]);
            datasource.add(map);
        }
    }
}
