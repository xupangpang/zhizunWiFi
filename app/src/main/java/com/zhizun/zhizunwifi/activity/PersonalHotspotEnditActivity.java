package com.zhizun.zhizunwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.PutAccount;
import com.zhizun.zhizunwifi.dialog.LoadingDialog;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.utils.BaseUtils;

import net.duohuo.dhroid.ioc.annotation.InjectView;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 设置个人热点信息
 *
 * @ClassName: SetNickAndSexActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-7 下午4:03:40
 */
public class PersonalHotspotEnditActivity extends BaseActivity {

	private EditText edit_nick;
	private ImageView back;
	private TextView title;
    private TextView edit_show;
	@InjectView(id = R.id.button_set_updateinfo, click = "click")
	Button button_set_updateinfo;
	private Intent intent0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_updateinfo);

		intent0 = this.getIntent();

		edit_nick = (EditText) findViewById(R.id.edit_nick);
		back = (ImageView) findViewById(R.id.headerLeft);
		title = (TextView) findViewById(R.id.headerTitle);
		edit_show = (TextView)findViewById(R.id.edit_show);
		edit_show.setVisibility(View.GONE);

		title.setText(intent0.getStringExtra("titlename"));
		edit_nick.setHint(intent0.getStringExtra("hint"));
		edit_nick.setText(intent0.getStringExtra("value"));

		back.setOnClickListener(this);

	}

	public void click(View v) {
		switch (v.getId()) {
			case R.id.button_set_updateinfo:
				String nickName = edit_nick.getText().toString();
				if (!TextUtils.isEmpty(nickName)) {
                  if (intent0.getStringExtra("titlename").contains("密码")){
					  if (nickName.length() < 8){
						  ShowToast("个人热点密码必须大于等于8位");
					  }else {
						  Intent intent = new Intent();
						  intent.putExtra("value",nickName);
						  setResult(RESULT_OK,intent);
						  finish();
					  }
				  }else {
					  Intent intent = new Intent();
					  intent.putExtra("value",nickName);
                     setResult(RESULT_OK,intent);
					  finish();
				  }
				}else {
					ShowToast("个人热点信息不能为空");
				}
				break;

			default:
				break;
		}
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
