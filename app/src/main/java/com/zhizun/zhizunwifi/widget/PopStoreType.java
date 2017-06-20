package com.zhizun.zhizunwifi.widget;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.StoreTypeListener;


public class PopStoreType extends PopupWindow implements View.OnClickListener{
    private View mainView;
    private TextView store_yl;
    private TextView store_lv;
    private TextView store_jd;
    private TextView store_ms;
    private TextView store_mrbj;
    private TextView store_shfw;
    private TextView store_dy;
    private TextView store_qt;
    private Button store_cancel;
    private Button store_OK;

    public PopStoreType(Context paramActivity, final StoreTypeListener paramOnClickListener){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.store_type_pop, null);

        store_yl = (TextView) mainView.findViewById(R.id.store_yl);
        store_yl.setOnClickListener(this);

        store_lv = (TextView) mainView.findViewById(R.id.store_lv);
        store_lv.setOnClickListener(this);

        store_jd = (TextView) mainView.findViewById(R.id.store_jd);
        store_jd.setOnClickListener(this);

        store_ms = (TextView) mainView.findViewById(R.id.store_ms);
        store_ms.setOnClickListener(this);

        store_mrbj = (TextView) mainView.findViewById(R.id.store_mrbj);
        store_mrbj.setOnClickListener(this);

        store_shfw = (TextView) mainView.findViewById(R.id.store_shfw);
        store_shfw.setOnClickListener(this);

        store_dy = (TextView) mainView.findViewById(R.id.store_dy);
        store_dy.setOnClickListener(this);

        store_qt = (TextView) mainView.findViewById(R.id.store_qt);
        store_qt.setOnClickListener(this);



        store_OK = (Button) mainView.findViewById(R.id.store_OK);
        store_cancel = (Button) mainView.findViewById(R.id.store_cancel);
        store_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        //设置每个子布局的事件监听器
        if (paramOnClickListener != null){
            store_OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (store_yl.isSelected()){
                        paramOnClickListener.onGetStoreTpye(0);
                    }else if (store_lv.isSelected()){
                        paramOnClickListener.onGetStoreTpye(1);
                    }else if (store_jd.isSelected()){
                        paramOnClickListener.onGetStoreTpye(2);
                    }else if (store_ms.isSelected()){
                        paramOnClickListener.onGetStoreTpye(3);
                    }else if (store_mrbj.isSelected()){
                        paramOnClickListener.onGetStoreTpye(4);
                    }else if (store_shfw.isSelected()){
                        paramOnClickListener.onGetStoreTpye(5);
                    }else if (store_dy.isSelected()){
                        paramOnClickListener.onGetStoreTpye(6);
                    }else if (store_qt.isSelected()){
                        paramOnClickListener.onGetStoreTpye(7);
                    }
                    dismiss();
                }
            });
        }
        setContentView(mainView);
        //设置宽度
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置高度
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.store_yl:
                store_yl.setSelected(true);
                store_lv.setSelected(false);
                store_jd.setSelected(false);
                store_ms.setSelected(false);
                store_mrbj.setSelected(false);
                store_shfw.setSelected(false);
                store_dy.setSelected(false);
                store_qt.setSelected(false);
                break;
            case R.id.store_lv:
                store_yl.setSelected(false);
                store_lv.setSelected(true);
                store_jd.setSelected(false);
                store_ms.setSelected(false);
                store_mrbj.setSelected(false);
                store_shfw.setSelected(false);
                store_dy.setSelected(false);
                store_qt.setSelected(false);
                break;
            case R.id.store_jd:
                store_yl.setSelected(false);
                store_lv.setSelected(false);
                store_jd.setSelected(true);
                store_ms.setSelected(false);
                store_mrbj.setSelected(false);
                store_shfw.setSelected(false);
                store_dy.setSelected(false);
                store_qt.setSelected(false);
                break;
            case R.id.store_ms:
                store_yl.setSelected(false);
                store_lv.setSelected(false);
                store_jd.setSelected(false);
                store_ms.setSelected(true);
                store_mrbj.setSelected(false);
                store_shfw.setSelected(false);
                store_dy.setSelected(false);
                store_qt.setSelected(false);
                break;
            case R.id.store_mrbj:
                store_yl.setSelected(false);
                store_lv.setSelected(false);
                store_jd.setSelected(false);
                store_ms.setSelected(false);
                store_mrbj.setSelected(true);
                store_shfw.setSelected(false);
                store_dy.setSelected(false);
                store_qt.setSelected(false);
                break;
            case R.id.store_shfw:
                store_yl.setSelected(false);
                store_lv.setSelected(false);
                store_jd.setSelected(false);
                store_ms.setSelected(false);
                store_mrbj.setSelected(false);
                store_shfw.setSelected(true);
                store_dy.setSelected(false);
                store_qt.setSelected(false);
                break;
            case R.id.store_dy:
                store_yl.setSelected(false);
                store_lv.setSelected(false);
                store_jd.setSelected(false);
                store_ms.setSelected(false);
                store_mrbj.setSelected(false);
                store_shfw.setSelected(false);
                store_dy.setSelected(true);
                store_qt.setSelected(false);
                break;
            case R.id.store_qt:
                store_yl.setSelected(false);
                store_lv.setSelected(false);
                store_jd.setSelected(false);
                store_ms.setSelected(false);
                store_mrbj.setSelected(false);
                store_shfw.setSelected(false);
                store_dy.setSelected(false);
                store_qt.setSelected(true);
                break;
        }
    }
}
