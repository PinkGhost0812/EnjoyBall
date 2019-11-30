package com.example.lenovo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.enjoyball.R;

import java.util.List;
import java.util.Map;

public class HomepageCommentAdapter extends BaseAdapter {

    private List<Map<String,Object>> dataSource = null;
    private Context context = null;
    private int item_layout_id;

    public HomepageCommentAdapter(Context context,
                                  List<Map<String, Object>> dataSource,
                                  int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
    }

    @Override
    public int getCount() {
        if (dataSource != null) {
            return dataSource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (dataSource != null) {
            return dataSource.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            viewHolder = new ViewHolder();
            viewHolder.tvHomepageCommentText = convertView.findViewById(R.id.tv_homepage_comment_text);
            viewHolder.tvHomepageCommentOriginal = convertView.findViewById(R.id.tv_homepage_comment_original);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvHomepageCommentText.setText(dataSource.get(position).get("comments").toString());
        viewHolder.tvHomepageCommentOriginal.setText(dataSource.get(position).get("pages").toString());

        return convertView;
    }

    private class ViewHolder {
        public TextView tvHomepageCommentText;
        public TextView tvHomepageCommentOriginal;
    }

}
