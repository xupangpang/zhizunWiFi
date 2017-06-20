package com.zhizun.zhizunwifi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.Decrypt;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpVerService;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.CountDownTimerUtils;
import com.zhizun.zhizunwifi.utils.JmTools;
import com.zhizun.zhizunwifi.utils.WifiUtils;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.qiujuer.genius.ui.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * @author xupp
 * @date 2017/5/24
 */

public class  AddWiFiMessageActivity extends BaseActivity {
    @InjectView(id = R.id.headerLeft)
    ImageView headleft;
    @InjectView(id = R.id.headerTitle)
    TextView headerTitle;
    @InjectView(id = R.id.change_wifi)
    TextView change_wifi;
    @InjectView(id = R.id.get_verification)
    Button get_verification;
    @InjectView(id = R.id.add_wifi_next)
    Button add_wifi_next;
    @InjectView(id = R.id.add_wifi_ssid)
    TextView add_wifi_ssid;
    @InjectView(id = R.id.add_wifi_phone)
    EditText add_wifi_phone;
    @InjectView(id = R.id.add_wifi_wifipsd)
    EditText add_wifi_wifipsd;
    @InjectView(id = R.id.add_wifi_verification)
    EditText add_wifi_verification;
    private Context mContext = this;
    private WifiUtils wifiUtils;
    protected CompositeSubscription mCompositeSubscription;
    private String macaddr="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_wifi_msg_main);
        initView();
    }
    private void initView(){
        wifiUtils = new WifiUtils(this);
        mCompositeSubscription = new CompositeSubscription();

        headerTitle.setText("新增WiFi信息");
        headleft.setOnClickListener(onClickListener);
        change_wifi.setOnClickListener(onClickListener);
        get_verification.setOnClickListener(onClickListener);
        add_wifi_next.setOnClickListener(onClickListener);

        add_wifi_ssid.setText(wifiUtils.getConnectedSSID(wifiUtils.getConnectionInfo()));



    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.headerLeft:
                    finish();
                    break;
                case R.id.change_wifi:
                    BaseUtils.setSharedPreferences("wifiChang","yes",mContext);
                    Intent intent = new Intent(AddWiFiMessageActivity.this, MainTabActivity.class);
                    startActivity(intent);
                    break;
                case R.id.get_verification:
                    if (TextUtils.isEmpty(add_wifi_phone.getText())){
                        ShowToast("请填写手机号");
                    }else if (!BaseUtils.isMobileNO(add_wifi_phone.getText().toString())){
                        ShowToast("请输入正确的手机号");
                    }else {
                        try {
                            sendMark();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.add_wifi_next:
                    if (TextUtils.isEmpty(add_wifi_wifipsd.getText())){
                        ShowToast("请输入WiFi密码");
                    }else if (TextUtils.isEmpty(add_wifi_verification.getText())){
                        ShowToast("请输入验证码");
                    }else if (TextUtils.isEmpty(add_wifi_ssid.getText())){
                        ShowToast("请连接WiFi");
                    }else {
                        //验证验证码
                        macaddr = wifiUtils.getConnectedMacAddr();
                        baseShowProgressDialog("正在验证验证码...",false);
                        try {
                            checkMark();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
            }

        }
    };

    private void sendMark() throws JSONException {
        HttpVerService apiService =  HttpManager.getVerService();
        Subscription subscription = apiService.SendMark(JmTools.JM(mContext,add_wifi_phone.getText().toString())).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Decrypt>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(Decrypt baseResultEntity) {
                                if (baseResultEntity != null) {
                                    String result = JmTools.DecryptString(baseResultEntity.result,baseResultEntity.T);
                                    if (!TextUtils.isEmpty(result)){
                                        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(get_verification, 60000, 1000);
                                        mCountDownTimerUtils.start();
                                        JSONObject js = null;
                                        try {
                                            js = new JSONObject(result);
                                           ShowToast(js.getString("msg"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }

                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                            }
                        }
                );
        mCompositeSubscription.add(subscription);
    }


    private void checkMark() throws JSONException {
        HttpVerService apiService =  HttpManager.getVerService();
        Subscription subscription = apiService.CheckMark(JmTools.JM_check(mContext,add_wifi_phone.getText().toString(),add_wifi_verification.getText().toString())).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Decrypt>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                basehideProgressDialog();
                            }

                            @Override
                            public void onNext(Decrypt baseResultEntity) {
                                basehideProgressDialog();
                                if (baseResultEntity != null) {
                                    String result = JmTools.DecryptString(baseResultEntity.result,baseResultEntity.T);
                                    if (!TextUtils.isEmpty(result)){
                                        try {
                                            JSONObject js = new JSONObject(result);
                                            ShowToast(js.getString("msg"));
                                            if (js.getString("ret").equals("200")){
                                                finish();
                                                Intent intent = new Intent(mContext,StoreInformationActivity.class);
                                                intent.putExtra("router",macaddr);
                                                intent.putExtra("phone",add_wifi_phone.getText().toString());
                                                intent.putExtra("passwd",add_wifi_wifipsd.getText().toString());
                                                intent.putExtra("ssid",add_wifi_ssid.getText().toString());
                                                startActivity(intent);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }

                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                            }
                        }
                );
        mCompositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null)
            mCompositeSubscription.unsubscribe();
    }
}
