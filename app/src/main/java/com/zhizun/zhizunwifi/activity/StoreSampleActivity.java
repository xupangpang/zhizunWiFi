package com.zhizun.zhizunwifi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;

import net.duohuo.dhroid.ioc.annotation.InjectView;

/**
 * @author xupp
 * @date 2017/5/25
 */

public class StoreSampleActivity extends BaseActivity {
    @InjectView(id = R.id.headerLeft)
    ImageView headleft;
    @InjectView(id = R.id.headerTitle)
    TextView headerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_sample_main);
        initView();
    }
    private void initView(){
        headerTitle.setText("示例");
        headleft.setOnClickListener(onClickListener);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.headerLeft:
                    finish();
                    break;
            }

        }
    };
}
