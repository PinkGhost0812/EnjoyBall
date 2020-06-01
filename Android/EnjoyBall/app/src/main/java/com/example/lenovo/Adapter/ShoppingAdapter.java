package com.example.lenovo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.Fragment.ShoppingDiaglog;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.StuffInfo;
import com.example.lenovo.entity.User;

import java.util.List;

public class ShoppingAdapter extends BaseAdapter {

    private List<StuffInfo> dataSource = null;

    private Context context = null;

    private int item_layout_id;
    private User user;


    public ShoppingAdapter(Context context,
                           List<StuffInfo> dataSource,
                           int item_layout_id,
                           User user) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
        this.user = user;
    }


    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null == convertView) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = convertView.findViewById(R.id.tv_shopping_name);
            viewHolder.tv_score = convertView.findViewById(R.id.tv_shopping_score);
            viewHolder.tv_num = convertView.findViewById(R.id.tv_shopping_num);
            viewHolder.iv_shopping = convertView.findViewById(R.id.iv_shopping_img);
            viewHolder.btn_delete = convertView.findViewById(R.id.btn_shopping_delete);
            viewHolder.btn_add = convertView.findViewById(R.id.btn_shopping_add);
            viewHolder.btn_buy = convertView.findViewById(R.id.btn_shopping_buy);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = dataSource.get(position).getNumber();
                if(num>1){
                    dataSource.get(position).setNumber(num-1);
                    //更新Adapter
                    notifyDataSetChanged();
                }
            }
        });

        viewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = dataSource.get(position).getNumber();
                if(num<99){
                    dataSource.get(position).setNumber(num+1);
                    //更新Adapter
                    notifyDataSetChanged();
                }
            }
        });

        viewHolder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buyscore = 0;
                buyscore = dataSource.get(position).getNumber()*dataSource.get(position).getPrice();
                OutDialog(position,buyscore,user);
            }
        });

        try {
            viewHolder.tv_name.setText(dataSource.get(position).getName());
            viewHolder.tv_score.setText(dataSource.get(position).getPrice()+"");
            viewHolder.tv_num.setText(dataSource.get(position).getNumber()+"");
            //viewHolder.iv_shopping.setImageResource(dataSource.get(position).getShopping_img());
            Glide.with(convertView).load(Info.BASE_URL + dataSource.get(position).getImg().toString()).into(viewHolder.iv_shopping);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
    private class ViewHolder{
        TextView tv_name;
        TextView tv_score;
        TextView tv_num;
        ImageView iv_shopping;
        Button btn_delete;
        Button btn_add;
        Button btn_buy;
    }

    private void OutDialog(int i,int buy,User user){
        FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ShoppingDiaglog shoppingDiaglog = new ShoppingDiaglog();
        if (!shoppingDiaglog.isAdded()){
            Bundle bundle = new Bundle();
            bundle.putSerializable("shop",dataSource.get(i));
//            bundle.putInt("num",dataSource.get(i).getShopping_num());
//            bundle.putString("name",dataSource.get(i).getShopping_name().toString());
            bundle.putInt("buyscore",buy);
            bundle.putSerializable("user",user);
            shoppingDiaglog.setArguments(bundle);
            transaction.add(shoppingDiaglog,"dialog_tag");
        }
        transaction.show(shoppingDiaglog);
        transaction.commit();

    }

}
