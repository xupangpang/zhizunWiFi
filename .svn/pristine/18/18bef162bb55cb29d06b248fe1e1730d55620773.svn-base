package com.zhizun.zhizunwifi.widget;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;


public class PopWinMore extends PopupWindow{
    private View mainView;
    private LinearLayout main_more_wifi, main_more_hotspot,main_more_router;
    private ImageView wifi_switch;
    private TextView wifi_switch_text;

    public PopWinMore(Activity paramActivity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popwin_more, null);
        //wifi布局
        main_more_wifi = ((LinearLayout)mainView.findViewById(R.id.main_more_wifi));
        //个人热点布局
        main_more_hotspot = (LinearLayout)mainView.findViewById(R.id.main_more_hotspot);
        //路由器管理
        main_more_router = (LinearLayout)mainView.findViewById(R.id.main_more_router);

        wifi_switch = (ImageView)mainView.findViewById(R.id.wifi_switch);

        wifi_switch_text = (TextView)mainView.findViewById(R.id.wifi_switch_text);


        //设置每个子布局的事件监听器
        if (paramOnClickListener != null){
            main_more_wifi.setOnClickListener(paramOnClickListener);
            main_more_hotspot.setOnClickListener(paramOnClickListener);
            main_more_router.setOnClickListener(paramOnClickListener);
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

    public void setwifiSwitch(boolean isOn){
        if (isOn){
            wifi_switch.setImageResource(R.drawable.ico_wifi_off);
            wifi_switch_text.setText("关闭WiFi");
        }else {
            wifi_switch.setImageResource(R.drawable.ico_wifi_on);
            wifi_switch_text.setText("打开WiFi");
        }

    }

}
