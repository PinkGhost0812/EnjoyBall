package com.example.lenovo.Activity;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.lenovo.Fragment.CreatePersonAgreementFragment;
import com.example.lenovo.Fragment.CreateTeamAgreementFragment;
import com.example.lenovo.enjoyball.R;

import java.util.HashMap;
import java.util.Map;

public class CreateAgreementAgainActivity extends AppCompatActivity {
    private FragmentTabHost fragmentTabHost= null;
    Map<String, View> map = new HashMap<>();
    private String[] tabArr = {"个人约球","队伍约战"};
    private Class[] fragmentArr = {CreatePersonAgreementFragment.class,CreateTeamAgreementFragment.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_create_agreement_again);
        initTabHost();
        setCurrentTab(tabArr[0]);
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i =0; i <tabArr.length;i++){
                    TextView textView = map.get(tabArr[i]).findViewById(R.id.tv_createagreement_type);
                    if (tabArr[i] == tabId) {
                        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_selected_circle));
                    } else {
                        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_unselected_circle));
                    }
                }

            }
        });


    }

    private void setCurrentTab(String id) {
        TextView textView = map.get(id).findViewById(R.id.tv_createagreement_type);
        textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_selected_circle));

    }

    private void initTabHost() {
        fragmentTabHost = findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this,
                getSupportFragmentManager(),
                android.R.id.tabcontent);
        for (int i = 0; i <fragmentArr.length; ++i) {
            View newView = getTabSpecView(i);
            TabHost.TabSpec tabSpec

                    =fragmentTabHost.newTabSpec(tabArr[i])
                    .setIndicator(newView);
            fragmentTabHost.addTab(tabSpec, fragmentArr[i], null);
            map.put(tabArr[i], newView);
        }
    }
    private View getTabSpecView(int i) {
        View view = getLayoutInflater()
                .inflate(R.layout.tabhost_spec_createagreement, null);
        TextView textView = view.findViewById(R.id.tv_createagreement_type);
        textView.setText(tabArr[i]);

        return view;
    }
}
