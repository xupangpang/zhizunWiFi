package com.zhizun.zhizunwifi.widget;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.activity.MainTabActivity;

import net.qiujuer.genius.ui.widget.Loading;

import java.util.Timer;
import java.util.TimerTask;

public class PopWinConnect extends PopupWindow{
    private View mainView;
    private ImageView connect_ico;
    private TextView wifi_connect_ssid;
    private CircleView wifi_connect_circleView;
    private Loading establish_connection_loading;
    private Loading authenticate_loading;
    private Loading assign_ip_loading;
    private TextView establish_connection;
    private TextView authenticate;
    private TextView assign_ip;
    private Button connect_exit;
    private Context context;
    private String ssid;
    private boolean renzType = false;
    private ImageView  speed_connect;
    private Animation operatingAnim;
    private View nac_layout;

    public PopWinConnect(Activity paramActivity, int paramInt1, int paramInt2,View nac){
        super(paramActivity);
        //窗口布局
        this.context = paramActivity;
        this.nac_layout = nac;
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.wifi_connect_main, null);
        connect_exit = (Button) mainView.findViewById(R.id.connect_exit);
        connect_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        connect_ico = (ImageView)mainView.findViewById(R.id.connect_ico);
        wifi_connect_ssid = (TextView)mainView.findViewById(R.id.wifi_connect_ssid);
        wifi_connect_circleView = (CircleView)mainView.findViewById(R.id.wifi_connect_circleView);

        establish_connection_loading = (Loading)mainView.findViewById(R.id.establish_connection_loading);
        authenticate_loading = (Loading)mainView.findViewById(R.id.authenticate_loading);
        assign_ip_loading = (Loading)mainView.findViewById(R.id.assign_ip_loading);

        establish_connection = (TextView)mainView.findViewById(R.id.establish_connection);
        authenticate = (TextView)mainView.findViewById(R.id.authenticate);
        assign_ip = (TextView)mainView.findViewById(R.id.assign_ip);
        speed_connect = (ImageView)mainView.findViewById(R.id.speed_connect);

        operatingAnim = AnimationUtils.loadAnimation(paramActivity, R.anim.rotate_fast);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);


        //设置每个子布局的事件监听器
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
    public void  setrenz(boolean type){
        this.renzType = type;
    }

    public void setStag(int type,Object ssid){
        if (ssid != null) {
            String[] strs0 = (String[]) ssid;
            wifi_connect_ssid.setText(strs0[0]);
        }

        //0 连接中  1 认证中 2 获取IP  3连接成功
        if (type == 0) {
            wifi_connect_circleView.isFinsh(false);
            wifi_connect_circleView.start();
            speed_connect.setAnimation(operatingAnim);
            speed_connect.setVisibility(View.VISIBLE);


            establish_connection_loading.start();
            authenticate_loading.stop();
            assign_ip_loading.stop();

            establish_connection.setTextColor(context.getResources().getColor(R.color.white));
            authenticate.setTextColor(context.getResources().getColor(R.color.white_alpha_144));
            assign_ip.setTextColor(context.getResources().getColor(R.color.white_alpha_144));

            //加载动画XML文件,生成动画指令
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            //开始执行动画
            connect_ico.startAnimation(animation);
            connect_ico.setImageResource(R.drawable.ico_connect);
        }else if (type == 1){
            wifi_connect_circleView.isFinsh(false);
            wifi_connect_circleView.start();
            speed_connect.setAnimation(operatingAnim);
            speed_connect.setVisibility(View.VISIBLE);

            establish_connection_loading.stop();
            authenticate_loading.start();
            assign_ip_loading.stop();

            establish_connection.setTextColor(context.getResources().getColor(R.color.white_alpha_144));
            authenticate.setTextColor(context.getResources().getColor(R.color.white));
            assign_ip.setTextColor(context.getResources().getColor(R.color.white_alpha_144));

            //加载动画XML文件,生成动画指令
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            //开始执行动画
            connect_ico.startAnimation(animation);
            connect_ico.setImageResource(R.drawable.ico_connect);

        }else if (type == 2){

            wifi_connect_circleView.isFinsh(false);
            wifi_connect_circleView.start();
            speed_connect.setAnimation(operatingAnim);
            speed_connect.setVisibility(View.VISIBLE);

            establish_connection_loading.stop();
            authenticate_loading.stop();
            assign_ip_loading.start();

            establish_connection.setTextColor(context.getResources().getColor(R.color.white_alpha_144));
            authenticate.setTextColor(context.getResources().getColor(R.color.white_alpha_144));
            assign_ip.setTextColor(context.getResources().getColor(R.color.white));

            //加载动画XML文件,生成动画指令
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            //开始执行动画
            connect_ico.startAnimation(animation);
            connect_ico.setImageResource(R.drawable.ico_ip);

        }else if (type == 3){

            wifi_connect_circleView.isFinsh(true);
            speed_connect.clearAnimation();
            speed_connect.setVisibility(View.GONE);

            establish_connection_loading.stop();
            authenticate_loading.stop();
            assign_ip_loading.stop();

            //加载动画XML文件,生成动画指令
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            //开始执行动画
            connect_ico.startAnimation(animation);
            connect_ico.setImageResource(R.drawable.ico_identity);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            },1000);

        }

        update();

    }
   private android.os.Handler handler = new android.os.Handler(){
       public void handleMessage(android.os.Message msg) {
           if (this != null){
               nac_layout.setAlpha(0);
               dismiss();
               if (!renzType){
                   try{
                       MainTabActivity parentActivity = (MainTabActivity)context;
                       parentActivity.transformCheckFrament();
                   }catch (Exception e){
                       e.printStackTrace();
                   };

               }
           }
       }
   };
}
