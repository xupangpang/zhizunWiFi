package com.zhizun.zhizunwifi.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;
import com.zhizun.zhizunwifi.R;

/**
 * @author xupp
 * @date 2017/5/22
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).placeholder(R.drawable.bg_position).error(R.drawable.bg_position).into(imageView);
    }
}
