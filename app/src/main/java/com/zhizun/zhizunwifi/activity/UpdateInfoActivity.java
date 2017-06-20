package com.zhizun.zhizunwifi.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import com.lidroid.xutils.exception.HttpException;
import com.zhizun.zhizunwifi.bean.MyUser;
import com.zhizun.zhizunwifi.bean.PutAccount;
import com.zhizun.zhizunwifi.dialog.ActionSheetDialog;
import com.zhizun.zhizunwifi.dialog.LoadingDialog;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.Constants;
import com.zhizun.zhizunwifi.utils.HttpConnections;

/**
 * 设置昵称和性别
 *
 * @ClassName: SetNickAndSexActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-7 下午4:03:40
 */
public class UpdateInfoActivity extends BaseActivity {

	private EditText edit_nick;
	private ImageView back;
	private TextView title;

	@InjectView(id = R.id.button_set_updateinfo, click = "click")
	Button button_set_updateinfo;
	private LoadingDialog loadingDialog;
	protected CompositeSubscription mCompositeSubscription;
	private HttpService apiService;
	private String username = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_updateinfo);
		edit_nick = (EditText) findViewById(R.id.edit_nick);
		back = (ImageView) findViewById(R.id.headerLeft);
		title = (TextView) findViewById(R.id.headerTitle);

		title.setText("修改昵称");

		back.setOnClickListener(this);

		username = BaseUtils.getSharedPreferences("userName", this);
		String nickName = BaseUtils.getSharedPreferences("nickName", this);

		if (!TextUtils.isEmpty(nickName))
			edit_nick.setText(nickName);

		apiService =  HttpManager.getService();
		mCompositeSubscription = new CompositeSubscription();

	}

	public void click(View v) {
		switch (v.getId()) {
			case R.id.button_set_updateinfo:
				String nickName = edit_nick.getText().toString();
				if (TextUtils.isEmpty(nickName)) {
					ShowToast("请填写昵称!");
					return;
				}

				if (username.length() == 11)
					updateInfo(username,null,nickName);
				else
					updateInfo(null,username,nickName);


				break;

			default:
				break;
		}
	}

	/**
	 * 修改资料 updateInfo
	 *
	 * @Title: updateInfo
	 * @return void
	 * @throws
	 */
	private void updateInfo(String username,String oid,final String nickName) {

		baseShowProgressDialog("正在提交...",false);
		Subscription subscription = apiService.UploadUserInfo(username,oid,nickName,"").subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<PutAccount>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
								basehideProgressDialog();
								ShowToast("昵称修改失败，请重试！");
							}

							@Override
							public void onNext(BaseResultEntity<PutAccount> baseResultEntity) {
								basehideProgressDialog();
								if (!baseResultEntity.data.result.equals("success")){
									ShowToast("昵称修改失败，请重试！");
								}else {
									ShowToast("昵称修改成功");
									BaseUtils.setSharedPreferences("nickName", nickName, UpdateInfoActivity.this);
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

			default:
				break;
		}
	}
}
