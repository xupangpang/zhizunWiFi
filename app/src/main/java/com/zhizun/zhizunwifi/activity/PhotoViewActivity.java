package com.zhizun.zhizunwifi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.TranslucentStatusBarUtils;

/**
 * @author xupp
 * @date 2017/5/26
 */

public class PhotoViewActivity extends Activity {
    private Context Mcontext = this;
    private ImageView headerLeft;
    private TextView headerTitle;
    private TextView headerRightTextView;
    private PhotoView photo_view;
    private Intent intent0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentStatusBarUtils.setTranslucentStatusBlack(Mcontext);
        setContentView(R.layout.photo_view_main);
        intent0 = this.getIntent();
        initView();
    }
    private void initView(){
        headerLeft = (ImageView)findViewById(R.id.headerLeft);
        headerLeft.setOnClickListener(onClickListener);
        headerTitle = (TextView)findViewById(R.id.headerTitle);
        headerTitle.setText(intent0.getStringExtra("photoTitle"));
        headerRightTextView = (TextView)findViewById(R.id.headerRightTextView);
        headerRightTextView.setOnClickListener(onClickListener);
        if (intent0.getBooleanExtra("photoShow",false)){
            headerRightTextView.setVisibility(View.VISIBLE);
        }else {
            headerRightTextView.setVisibility(View.GONE);
        }
        photo_view = (PhotoView)findViewById(R.id.photo_view);
        Glide.with(Mcontext).load(intent0.getStringExtra("photoUrl")).into(photo_view);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           int id = view.getId();
            switch (id){
                case R.id.headerLeft:
                    finish();
                    break;
                case R.id.headerRightTextView:
                    Intent intent = new Intent();
                    intent.putExtra("imageType",intent0.getStringExtra("imageType"));
                    setResult(RESULT_OK,intent);
                    finish();
                    break;

            }
        }
    };
}
