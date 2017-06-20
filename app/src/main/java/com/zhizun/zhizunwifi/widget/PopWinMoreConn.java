package com.zhizun.zhizunwifi.widget;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhizun.zhizunwifi.R;


public class PopWinMoreConn extends PopupWindow{
    private View mainView;
    private LinearLayout see_the_share, main_more_disconnect,main_more_offwifi;

    public PopWinMoreConn(Activity paramActivity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popwin_more_conn, null);

        see_the_share = ((LinearLayout)mainView.findViewById(R.id.see_the_share_pop));

        main_more_disconnect = (LinearLayout)mainView.findViewById(R.id.main_more_disconnect);

        main_more_offwifi = (LinearLayout)mainView.findViewById(R.id.main_more_offwifi);


        //设置每个子布局的事件监听器
        if (paramOnClickListener != null){
            see_the_share.setOnClickListener(paramOnClickListener);
            main_more_disconnect.setOnClickListener(paramOnClickListener);
            main_more_offwifi.setOnClickListener(paramOnClickListener);
        }
        setContentView(mainView);
        //设置宽度
        setWidth(paramInt1);
        //设置高度
        setHeight(paramInt2);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0x00000000));
    }


}
