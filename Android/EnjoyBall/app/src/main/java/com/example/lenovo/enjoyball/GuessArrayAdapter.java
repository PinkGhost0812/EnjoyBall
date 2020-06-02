package com.example.lenovo.enjoyball;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.Activity.HomepageActivity;
import com.example.lenovo.Activity.IdentityActivity;
import com.example.lenovo.entity.User;

import java.util.List;
import java.util.Map;

public class GuessArrayAdapter extends BaseAdapter {
    // 原始数据
    private List<Map<String, Object>> dataSource = null;
    // 上下文环境
    private Context context = null;
    // item对应的布局文件
    private int item_layout_id;

    private int flag=0;
    private int score;
    @Override
    public int getCount() {
        return dataSource.size();
    }

    public GuessArrayAdapter(
            Context context,
            List<Map<String, Object>> dataSource,
            int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
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
        score = (int)dataSource.get(0).get("userscore");
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.tv_guess_name);
            viewHolder.leftname = convertView.findViewById(R.id.tv_guess_one);
            viewHolder.rightname = convertView.findViewById(R.id.tv_guess_two);
            viewHolder.status = convertView.findViewById(R.id.tv_guess_status);
            viewHolder.odds = convertView.findViewById(R.id.tv_guess_scale);
            viewHolder.left1 = convertView.findViewById(R.id.btn_guess_left10);
            viewHolder.left2 = convertView.findViewById(R.id.btn_guess_left100);
            viewHolder.left3 = convertView.findViewById(R.id.btn_guess_left1000);
            viewHolder.right1 = convertView.findViewById(R.id.btn_guess_right10);
            viewHolder.right2 = convertView.findViewById(R.id.btn_guess_right100);
            viewHolder.right3 = convertView.findViewById(R.id.btn_guess_right1000);
            viewHolder.score = convertView.findViewById(R.id.tv_guess_score);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设值
        viewHolder.score.setText("用户当前积分"+score);
        //设置点击事件
        final View finalConvertView = convertView;
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.left1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<10){
                    Looper.prepare();
                    Toast.makeText(finalConvertView.getContext(), "当前用户积分不足", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    showAlertDialog(finalConvertView);
//                    if(flag==1){
                        score=score-10;
                        finalViewHolder.score.setText("用户当前积分"+score);
                        dataSource.get(0).put("left",(int)dataSource.get(0).get("left")+10);
                        double leftscore=(double)dataSource.get(0).get("left");
                        double rightscore=(double)dataSource.get(0).get("right");
                        if(leftscore>rightscore){
                            double rate = leftscore/rightscore;
                            finalViewHolder.odds.setText("当前支持率:"+rate+":"+1);
                        }else{
                            double rate = rightscore/leftscore;
                            finalViewHolder.odds.setText("当前支持率:"+1+":"+rate);
                        }
//                        //flag=0;
                        Looper.prepare();
                        Toast.makeText(finalConvertView.getContext(), "支持成功，感谢您的支持", Toast.LENGTH_SHORT).show();
                        Looper.loop();
//                    }

                }
            }
        });
        viewHolder.left2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<100){
                    Looper.prepare();
                    Toast.makeText(finalConvertView.getContext(), "当前用户积分不足", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    showAlertDialog(finalConvertView);
                    if(flag==1){
                        score=score-100;
                        finalViewHolder.score.setText("用户当前积分"+score);
                        dataSource.get(0).put("left",(int)dataSource.get(0).get("left")+100);
                        double leftscore=(double)dataSource.get(0).get("left");
                        double rightscore=(double)dataSource.get(0).get("right");
                        if(leftscore>rightscore){
                            double rate = leftscore/rightscore;
                            finalViewHolder.odds.setText("当前支持率:"+rate+":"+1);
                        }else{
                            double rate = rightscore/leftscore;
                            finalViewHolder.odds.setText("当前支持率:"+1+":"+rate);
                        }
                        flag=0;
                        Looper.prepare();
                        Toast.makeText(finalConvertView.getContext(), "支持成功，感谢您的支持", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                }
            }
        });
        viewHolder.left3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<1000){
                    Looper.prepare();
                    Toast.makeText(finalConvertView.getContext(), "当前用户积分不足", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    showAlertDialog(finalConvertView);
                    if(flag==1){
                        score=score-1000;
                        finalViewHolder.score.setText("用户当前积分"+score);
                        dataSource.get(0).put("left",(int)dataSource.get(0).get("left")+1000);
                        double leftscore=(double)dataSource.get(0).get("left");
                        double rightscore=(double)dataSource.get(0).get("right");
                        if(leftscore>rightscore){
                            double rate = leftscore/rightscore;
                            finalViewHolder.odds.setText("当前支持率:"+rate+":"+1);
                        }else{
                            double rate = rightscore/leftscore;
                            finalViewHolder.odds.setText("当前支持率:"+1+":"+rate);
                        }
                        flag=0;
                        Looper.prepare();
                        Toast.makeText(finalConvertView.getContext(), "支持成功，感谢您的支持", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                }
            }
        });
        viewHolder.right1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<10){
                    Looper.prepare();
                    Toast.makeText(finalConvertView.getContext(), "当前用户积分不足", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    showAlertDialog(finalConvertView);
                    if(flag==1){
                        score=score-10;
                        finalViewHolder.score.setText("用户当前积分"+score);
                        dataSource.get(0).put("right",(int)dataSource.get(0).get("right")+10);
                        double leftscore=(double)dataSource.get(0).get("left");
                        double rightscore=(double)dataSource.get(0).get("right");
                        if(leftscore>rightscore){
                            double rate = leftscore/rightscore;
                            finalViewHolder.odds.setText("当前支持率:"+rate+":"+1);
                        }else{
                            double rate = rightscore/leftscore;
                            finalViewHolder.odds.setText("当前支持率:"+1+":"+rate);
                        }
                        flag=0;
                        Looper.prepare();
                        Toast.makeText(finalConvertView.getContext(), "支持成功，感谢您的支持", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                }
            }
        });
        viewHolder.right2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<100){
                    Looper.prepare();
                    Toast.makeText(finalConvertView.getContext(), "当前用户积分不足", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    showAlertDialog(finalConvertView);
                    if(flag==1){
                        score=score-10;
                        finalViewHolder.score.setText("用户当前积分"+score);
                        dataSource.get(0).put("right",(int)dataSource.get(0).get("right")+100);
                        double leftscore=(double)dataSource.get(0).get("left");
                        double rightscore=(double)dataSource.get(0).get("right");
                        if(leftscore>rightscore){
                            double rate = leftscore/rightscore;
                            finalViewHolder.odds.setText("当前支持率:"+rate+":"+1);
                        }else{
                            double rate = rightscore/leftscore;
                            finalViewHolder.odds.setText("当前支持率:"+1+":"+rate);
                        }
                        flag=0;
                        Looper.prepare();
                        Toast.makeText(finalConvertView.getContext(), "支持成功，感谢您的支持", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                }
            }
        });
        viewHolder.right3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score<1000){
                    Looper.prepare();
                    Toast.makeText(finalConvertView.getContext(), "当前用户积分不足", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    showAlertDialog(finalConvertView);
                    if(flag==1){
                        score=score-10;
                        finalViewHolder.score.setText("用户当前积分"+score);
                        dataSource.get(0).put("right",(int)dataSource.get(0).get("right")+1000);
                        double leftscore=(double)dataSource.get(0).get("left");
                        double rightscore=(double)dataSource.get(0).get("right");
                        if(leftscore>rightscore){
                            double rate = leftscore/rightscore;
                            finalViewHolder.odds.setText("当前支持率:"+rate+":"+1);
                        }else{
                            double rate = rightscore/leftscore;
                            finalViewHolder.odds.setText("当前支持率:"+1+":"+rate);
                        }
                        flag=0;
                        Looper.prepare();
                        Toast.makeText(finalConvertView.getContext(), "支持成功，感谢您的支持", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                }
            }
        });
        return convertView;
    }

    private void showAlertDialog(View convertView) {
        //创建Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(convertView.getContext());
        //设置对话框属性
        builder.setTitle("温馨提示");//标题
        builder.setMessage("确定要支持这支队伍吗");//显示的提示内容
        builder.setNegativeButton("取消",null);//设置取消按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //退出当前的Activity
                flag =1;
//                finish();
            }
        }); //设置确定按钮
        //创建对话框对象
        AlertDialog alertDialog = builder.create();
        //显示对话框
        alertDialog.show();
//        new AlertDialog.Builder(context).setTitle("温馨提示")
//                .setMessage("确定要支持这支队伍吗")
//                .setNegativeButton("取消",null)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        flag = 1;
//                    }
//                }).create().show();
    }


    private class ViewHolder{
        public TextView name;
        public TextView status;
        public TextView leftname;
        public TextView rightname;
        public TextView odds;
        public Button left1;
        public Button left2;
        public Button left3;
        public Button right1;
        public Button right2;
        public Button right3;
        public TextView score;
    }
}
