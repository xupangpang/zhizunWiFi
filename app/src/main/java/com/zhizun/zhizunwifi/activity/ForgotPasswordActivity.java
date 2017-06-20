package com.zhizun.zhizunwifi.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.Decrypt;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.http.HttpVerService;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.CountDownTimerUtils;
import com.zhizun.zhizunwifi.utils.JmTools;

import net.duohuo.dhroid.ioc.annotation.InjectView;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ForgotPasswordActivity extends BaseActivity {
    @InjectView(id = R.id.headerLeft)
    ImageView headleft;
    @InjectView(id = R.id.headerTitle)
    TextView headerTitle;
    @InjectView(id = R.id.forgot_password_phone)
    EditText forgot_password_phone;
    @InjectView(id = R.id.forgot_password_verification)
    EditText forgot_password_verification;
    @InjectView(id = R.id.forgot_password_newpsd)
    EditText forgot_password_newpsd;
    @InjectView(id = R.id.btn_reg_new)
    net.qiujuer.genius.ui.widget.Button btn_reg_new;
    @InjectView(id = R.id.get_verification)
    net.qiujuer.genius.ui.widget.Button get_verification;
    private Context mContext = this;
    protected CompositeSubscription mCompositeSubscription;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password_main);
		initView();
        mCompositeSubscription = new CompositeSubscription();

	}

	private void initView() {
        headerTitle.setText("忘记密码");
        headleft.setOnClickListener(onClickListener);
        get_verification.setOnClickListener(onClickListener);
        btn_reg_new.setOnClickListener(onClickListener);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.headerLeft:
                    finish();
                    break;
                case R.id.get_verification:
                    if (TextUtils.isEmpty(forgot_password_phone.getText())){
                        ShowToast("请填写手机号");
                    }else if (!BaseUtils.isMobileNO(forgot_password_phone.getText().toString())){
                        ShowToast("请输入正确的手机号");
                    }else {
                        try {
                            sendMark();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.btn_reg_new:
                    if (TextUtils.isEmpty(forgot_password_verification.getText())){
                        ShowToast("请填写验证码");
                    }else if (TextUtils.isEmpty(forgot_password_newpsd.getText())){
                        ShowToast("请输入新密码");
                    }else if (forgot_password_newpsd.getText().length() < 6){
                            ShowToast("密码必须大于6位");
                    }else {
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


    private void checkMark() throws JSONException {
        baseShowProgressDialog("正在提交...",false);
        HttpVerService apiService =  HttpManager.getVerService();
        Subscription subscription = apiService.CheckMark(JmTools.JM_check(mContext,forgot_password_phone.getText().toString(),forgot_password_verification.getText().toString())).subscribeOn(Schedulers.io())
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
                                if (baseResultEntity != null) {
                                    String result = JmTools.DecryptString(baseResultEntity.result,baseResultEntity.T);
                                    if (!TextUtils.isEmpty(result)){
                                        try {
                                            JSONObject js = new JSONObject(result);
                                            if (js.getString("ret").equals("200")){
                                                ResetPasswd();
                                            }else {
                                                basehideProgressDialog();
                                                ShowToast(js.getString("msg"));
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

    private void sendMark() throws JSONException {
        HttpVerService apiService =  HttpManager.getVerService();
        Subscription subscription = apiService.SendMark(JmTools.JM(mContext,forgot_password_phone.getText().toString())).subscribeOn(Schedulers.io())
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



    private void ResetPasswd() {
        HttpService apiService =  HttpManager.getService();
        Subscription subscription = apiService.ResetPasswd(forgot_password_phone.getText().toString(),forgot_password_newpsd.getText().toString()).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<BaseResultEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                basehideProgressDialog();
                            }

                            @Override
                            public void onNext(BaseResultEntity baseResultEntity) {
                                basehideProgressDialog();
                                if (baseResultEntity.ret == 200){
                                    ShowToast("重置密码成功");
                                    finish();
                                }else {
                                    ShowToast(baseResultEntity.msg);
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
        if (mCompositeSubscription != null){
            mCompositeSubscription.unsubscribe();
        }
    }
}
