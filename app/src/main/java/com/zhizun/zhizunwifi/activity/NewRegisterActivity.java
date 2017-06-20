package com.zhizun.zhizunwifi.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.MyUser;
import com.zhizun.zhizunwifi.bean.Register;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.utils.BaseUtils;

import net.duohuo.dhroid.util.NetworkUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class NewRegisterActivity extends BaseActivity implements
		OnClickListener {

	Button btn_register;
	EditText et_username, et_password, et_email;
	MyUser currentUser;
	ImageView headerLeft;
	TextView headerTitle;
	private HttpService apiService;
	protected CompositeSubscription mCompositeSubscription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		apiService =  HttpManager.getService();
		mCompositeSubscription = new CompositeSubscription();

		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_email = (EditText) findViewById(R.id.et_email);
		headerLeft = (ImageView) findViewById(R.id.headerLeft);
		headerTitle = (TextView) findViewById(R.id.headerTitle);

		btn_register = (Button) findViewById(R.id.btn_register);

		headerTitle.setText("账号注册");
		headerLeft.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}

	private void register() {
		String name = et_username.getText().toString();
		String password = et_password.getText().toString();
		String pwd_again = et_email.getText().toString();

		if (TextUtils.isEmpty(name)) {
			ShowToast("用户名不能为空");
			return;
		}
		// 判断是否为手机号码格式
		if (!BaseUtils.isMobileNO(name)) {
			ShowToast("请输入正确的手机号");
			return;
		}

		if (TextUtils.isEmpty(password)) {
			ShowToast("密码不能为空");
			return;
		}

		if (password.length()<6) {
			ShowToast("密码必须大于6位");
			return;
		}

		if (!pwd_again.equals(password)) {
			ShowToast("输入的两次密码不一致");
			return;
		}

		boolean isNetConnected = NetworkUtils.isNetworkAvailable();
		if (!isNetConnected) {
			ShowToast("当前网络不可用,请检查您的网络!");
			return;
		}

		baseShowProgressDialog("正在注册...",false);
		Subscription subscription = apiService.UserRegister(name,password).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<Register>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
								basehideProgressDialog();
								ShowToast("注册失败，请重试！");
							}

							@Override
							public void onNext(BaseResultEntity<Register> baseResultEntity) {
								basehideProgressDialog();
								if (!baseResultEntity.data.result.equals("success")){
									if (!TextUtils.isEmpty(baseResultEntity.data.msg))
										ShowToast(baseResultEntity.data.msg);
									else
									ShowToast("注册失败，请重试！");
								}else {
									ShowToast("注册成功");
									finish();
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
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.headerLeft:
				finish();
				break;
			case R.id.btn_register:
				register();
				break;

			default:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCompositeSubscription.unsubscribe();
	}
}
