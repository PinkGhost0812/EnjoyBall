package com.example.lenovo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;

import java.util.List;
import java.util.Map;

public class ModifyLocationAdapter extends BaseAdapter {
    private List<Map<String,Object>> datasource = null;
    private Context context = null;
    private int item_layout_id;


    public ModifyLocationAdapter(Context context,
                                 List<Map<String,Object>> datasource,
                                 int item_layout_id){
            this.context = context;
            this.datasource = datasource;
            this.item_layout_id = item_layout_id;
    }
    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null == convertView){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_layout_id,null);
            viewHolder = new ViewHolder();
            viewHolder.headphoto = convertView.findViewById(R.id.iv_location_headphoto);
            viewHolder.nickname = convertView.findViewById(R.id.tv_location_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Map<String,Object> map = datasource.get(position);
        if(map.get("nickname")!=null){
            viewHolder.nickname .setText(map.get("nickname").toString());
        }else{
            viewHolder.nickname.setText("待加入");
        }
        if(map.get("headsphoto")!=null){
            Glide.with(convertView).load(Info.BASE_URL+map.get("headsphoto")).into(viewHolder.headphoto);
        }else{
            viewHolder.headphoto.setImageResource(R.drawable.addportrait);
        }
        return convertView;
    }


    private class ViewHolder{
        public TextView nickname;
        public ImageView headphoto;
    }
}
