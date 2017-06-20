package com.zhizun.zhizunwifi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.GlideImageLoader;
import com.zhizun.zhizunwifi.utils.TranslucentStatusBarUtils;

import java.util.List;

/**
 * @author xupp
 * @date 2017/5/27
 */

public class StoreReviewActivity extends Activity {
    private Banner store_review_banner;
    private Intent intent0;
    private TextView store_title_name;
    private TextView store_title_type;
    private ImageView headerMenu;
    private List<String> imageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusBarUtils.transparencyBar(this);
        setContentView(R.layout.store_review_main);
        intent0 = this.getIntent();
        initView();
    }
    private void initView(){
        store_review_banner = (Banner)findViewById(R.id.store_review_banner);
        store_title_name = (TextView) findViewById(R.id.store_title_name);
        store_title_type = (TextView) findViewById(R.id.store_title_type);
        headerMenu = (ImageView)findViewById(R.id.headerMenu);

        imageList = intent0.getStringArrayListExtra("images");
        if (imageList != null){
            for (int i = 0; i < imageList.size(); i++) {
                if (TextUtils.isEmpty(imageList.get(i))){
                    imageList.remove(imageList.get(i));
                }
            }
        }

        //设置banner样式
        store_review_banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        store_review_banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        store_review_banner.setImages(imageList);
        //设置banner动画效果
        store_review_banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        store_review_banner.isAutoPlay(true);
        //设置轮播时间
        store_review_banner.setDelayTime(5000);
        //banner设置方法全部调用完毕时最后调用
        store_review_banner.start();

        store_title_name.setText(intent0.getStringExtra("storename"));
        store_title_type.setText(intent0.getStringExtra("storetype"));

        headerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        store_review_banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        store_review_banner.stopAutoPlay();
    }
}
