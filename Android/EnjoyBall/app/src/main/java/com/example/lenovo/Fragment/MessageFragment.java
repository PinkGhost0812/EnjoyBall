package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lenovo.Activity.ManageMessageActivity;
import com.example.lenovo.Activity.NotifyMessageActivity;
import com.example.lenovo.enjoyball.R;

public class MessageFragment extends Fragment {
    private LinearLayout ll_notification = null;
    private LinearLayout ll_manage = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainmessage,
                container, false);
        setView(view);
        ll_manage.setOnClickListener(new Onclicked());
        ll_notification.setOnClickListener(new Onclicked());
        return view;
    }

    private void setView(View view) {

        ll_manage = view.findViewById(R.id.ll_mainmessage_manage);
        ll_notification = view.findViewById(R.id.ll_mainmessage_notification);

    }

    public class Onclicked implements View.OnClickListener {
        Intent intent = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_mainmessage_manage:
                    intent = new Intent(getContext(), ManageMessageActivity.class);
                    break;
                case R.id.ll_mainmessage_notification:
                    intent = new Intent(getContext(), NotifyMessageActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}