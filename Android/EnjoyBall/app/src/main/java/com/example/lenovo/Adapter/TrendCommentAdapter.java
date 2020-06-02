package com.example.lenovo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.enjoyball.GlideApp;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.PYQ;
import com.example.lenovo.entity.PYQComment;

import java.util.List;

public class TrendCommentAdapter extends BaseAdapter {
    private List<PYQComment> dataSource;
    private int itemResId;
    private Context mContext;

    public TrendCommentAdapter(List<PYQComment> dataSource, int itemResId, Context mContext) {
        this.dataSource = dataSource;
        this.itemResId = itemResId;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        if (null != dataSource) {
            return dataSource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != dataSource) {
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
        ViewHolder viewHolder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(itemResId, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_content.setText(dataSource.get(position).getContent() + "");
            viewHolder.tv_commentName.setText(dataSource.get(position).getUserName());
            GlideApp.with(mContext)
                    .load(dataSource.get(position).getUserImg())
                    .circleCrop()
                    .error(mContext.getResources().getDrawable(R.drawable.member))
                    .into(viewHolder.iv_commentHead);
        }
        return convertView;
    }
    private class ViewHolder {
        public ImageView iv_commentHead;
        public TextView tv_commentName;
        public TextView tv_time;
        public TextView tv_content;
    }
}
