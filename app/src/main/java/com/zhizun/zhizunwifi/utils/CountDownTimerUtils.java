package com.zhizun.zhizunwifi.utils;

import android.os.CountDownTimer;

import com.zhizun.zhizunwifi.R;

import net.qiujuer.genius.ui.widget.Button;

/**
 * @author xupp
 * @date 2017/5/24
 */

public class CountDownTimerUtils extends CountDownTimer {
    private Button mButton;

    /**
     *          The TextView
     *
     *
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(Button button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mButton = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mButton.setClickable(false); //设置不可点击
        mButton.setText("重新发送"+"("+ millisUntilFinished / 1000 +")");  //设置倒计时时间
        mButton.setBackgroundResource(R.drawable.btn_grey_big_shape_bg); //设置按钮为灰色，这时是不能点击的
    }

    @Override
    public void onFinish() {
        mButton.setText("重新获取验证码");
        mButton.setClickable(true);//重新获得点击
        mButton.setBackgroundResource(R.drawable.btn_blue_big_shape_bg);  //还原背景色
    }
}
