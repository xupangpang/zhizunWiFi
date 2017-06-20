package com.zhizun.zhizunwifi.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.Login;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.Constants;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.util.NetworkUtils;

import java.util.Map;
import java.util.Set;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @ClassName: LoginActivity
 * @Description: TODO
 *
 */
public class NewLoginActivity extends BaseActivity implements OnClickListener{


	@InjectView(id = R.id.et_account)
	EditText et_username;
	@InjectView(id = R.id.et_password)
	EditText et_password;
	@InjectView(id = R.id.btn_reg_log,click = "click")
	Button btn_login;
	@InjectView(id = R.id.tv_regist,click = "click")
	TextView btn_register;
	@InjectView(id = R.id.headerTitle)
	TextView headerTitle;
	@InjectView(id = R.id.headerLeft)
	ImageView headerLeft;
	@InjectView(id = R.id.other_login_qq)
	ImageView other_login_qq;
	@InjectView(id = R.id.other_login_wechat)
	ImageView other_login_wechat;
	@InjectView(id = R.id.other_login_weibo)
	ImageView other_login_weibo;
	@InjectView(id = R.id.forgot_password)
	TextView forgot_password;
	private HttpService apiService;
	protected CompositeSubscription mCompositeSubscription;
	private Context context = this;
	private UMShareAPI  mShareAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		apiService = HttpManager.getService();
		mCompositeSubscription = new CompositeSubscription();

		other_login_weibo.setOnClickListener(this);
		other_login_wechat.setOnClickListener(this);
		other_login_qq.setOnClickListener(this);
		headerLeft.setOnClickListener(this);
		headerTitle.setText("会员登陆");
		forgot_password.setOnClickListener(this);
	}

	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && Constants.ACTION_REGISTER_SUCCESS_FINISH.equals(intent.getAction())) {
				finish();
			}
		}

	}

	public void click(View v) {
		switch (v.getId()) {
			case R.id.tv_regist:
				Intent intent = new Intent(NewLoginActivity.this,
						NewRegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_reg_log:
				boolean isNetConnected = NetworkUtils.isNetworkAvailable();
				if(!isNetConnected){
					ShowToast("当前网络不可用,请检查您的网络!");
				}else {
					MobclickAgent.onEvent(this, "The_login");
					login();
				}
				break;



			default:
				break;
		}
	}

	private void login(){
		final String name = et_username.getText().toString();
		String password = et_password.getText().toString();

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


		boolean isNetConnected = NetworkUtils.isNetworkAvailable();
		if (!isNetConnected) {
			ShowToast("当前网络不可用,请检查您的网络!");
			return;
		}

		baseShowProgressDialog("正在登陆...",false);
		Subscription subscription = apiService.UserLogin(name,password).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<Login>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
								basehideProgressDialog();
								ShowToast("登陆失败，请重试！");
							}

							@Override
							public void onNext(BaseResultEntity<Login> baseResultEntity) {
								basehideProgressDialog();
								if (!baseResultEntity.data.result.equals("success")){
									if (!TextUtils.isEmpty(baseResultEntity.data.msg))
										ShowToast(baseResultEntity.data.msg);
									else
										ShowToast("账号或密码错误");
								}else {
									ShowToast("登陆成功");

									BaseUtils.setSharedPreferences("nickName", baseResultEntity.data.info.name, NewLoginActivity.this);
									BaseUtils.setSharedPreferences("userName", name, NewLoginActivity.this);
									BaseUtils.setSharedPreferences("sex", baseResultEntity.data.info.sex, NewLoginActivity.this);
									BaseUtils.setSharedPreferences("money", baseResultEntity.data.info.points, NewLoginActivity.this);
									BaseUtils.setSharedPreferences("userIcon", baseResultEntity.data.info.head, NewLoginActivity.this);
									if (baseResultEntity.data.info.sign == 1) {
										BaseUtils.setSharedPreferences("sign", String.valueOf(System.currentTimeMillis()), NewLoginActivity.this);
									}

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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mCompositeSubscription.unsubscribe();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.headerLeft:
				finish();
				break;
			case R.id.other_login_qq:
				doOauthVerify(SHARE_MEDIA.QQ);
				MobclickAgent.onEvent(this, "The_login_oauth");
				break;
			case R.id.other_login_wechat:
				doOauthVerify(SHARE_MEDIA.WEIXIN);
				MobclickAgent.onEvent(this, "The_login_oauth");
				break;
			case R.id.other_login_weibo:
				MobclickAgent.onEvent(this, "The_login_oauth");
				doOauthVerify(SHARE_MEDIA.SINA);
				break;
			case R.id.forgot_password:
				Intent intent0 = new Intent(NewLoginActivity.this,
						ForgotPasswordActivity.class);
				startActivity(intent0);
				break;
			default:
				break;
		}

	}

	private void doOauthLogin(final String uid,final String name,final String head,final String sex){
		Subscription subscription = apiService.UserOAuth(uid,name,head,sex).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<Login>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
							}

							@Override
							public void onNext(BaseResultEntity<Login> baseResultEntity) {
								if (!baseResultEntity.data.result.equals("success")){
									if (!TextUtils.isEmpty(baseResultEntity.data.msg))
										ShowToast(baseResultEntity.data.msg);
									else
										ShowToast("登录失败");
								}else {
									ShowToast("登陆成功");
									BaseUtils.setSharedPreferences("nickName", name, NewLoginActivity.this);
									BaseUtils.setSharedPreferences("userName", uid, NewLoginActivity.this);
									BaseUtils.setSharedPreferences("sex", sex, NewLoginActivity.this);
									BaseUtils.setSharedPreferences("money", baseResultEntity.data.info.points, NewLoginActivity.this);
									BaseUtils.setSharedPreferences("userIcon", head, NewLoginActivity.this);
									if (baseResultEntity.data.info.sign == 1)
										BaseUtils.setSharedPreferences("sign", String.valueOf(System.currentTimeMillis()),  NewLoginActivity.this);
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

	private void  doOauthVerify(final SHARE_MEDIA share_media){
		mShareAPI = UMShareAPI.get(context);
		mShareAPI.getPlatformInfo(NewLoginActivity.this, share_media, new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA share_media) {

			}

			@Override
			public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
				//获取用户信息
				if(map != null) {
					StringBuilder sb = new StringBuilder();
					Set<String> keys = map.keySet();
					for (String key : keys) {
						sb.append(key + "=" + map.get(key).toString() + "\r\n");

					}
					Log.e("TestData", sb.toString());

					if (share_media == SHARE_MEDIA.QQ){
						doOauthLogin(map.get("uid").toString(),map.get("screen_name").toString(),map.get("profile_image_url").toString(),map.get("gender").toString().equals("男")?"1":"2");
					}else if (share_media == SHARE_MEDIA.WEIXIN){
						doOauthLogin(map.get("unionid").toString(),map.get("screen_name").toString(),map.get("profile_image_url").toString(),map.get("gender").toString());
					}else {
						doOauthLogin(map.get("uid").toString(),map.get("screen_name").toString(),map.get("profile_image_url").toString(),map.get("gender").toString());
					}



				}
			}

			@Override
			public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
				Log.e("OauthError",throwable.getMessage());

			}

			@Override
			public void onCancel(SHARE_MEDIA share_media, int i) {

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

	}

}
