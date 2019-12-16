package com.example.lenovo.Util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class ImageLoadBanner extends ImageLoader {

//    @Override
//    public void displayImage(Context context, Object path, ImageView imageView) {
//        imageView.setImageResource(Integer.parseInt(path.toString()));
//    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context.getApplicationContext())
                .load((String) path)
                .into(imageView);
    }
}