package com.example.lenovo.enjoyball;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class LinearLayoutForListView extends LinearLayout {
    private BaseAdapter adapter;
    private List<View> viewList = new ArrayList<>();

    public LinearLayoutForListView(Context context) {
        super(context);
    }

    public LinearLayoutForListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutForListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        bindLinearLayout();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }



    /**
     * 绑定布局
     */
    public void bindLinearLayout() {
        if(adapter == null){
            return;
        }
        int count = adapter.getCount();
        this.removeAllViews();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);
//            viewList.add(v);
//            Log.e("addView", "view" + i);
//            v.setOnClickListener(this.onClickListener);
            addView(v, i);
        }
        Log.v("countTAG", "" + count);
    }


}
