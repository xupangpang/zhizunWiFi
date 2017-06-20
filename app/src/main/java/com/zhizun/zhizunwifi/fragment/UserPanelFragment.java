package com.zhizun.zhizunwifi.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.activity.BusinessCooperationActivity;
import com.zhizun.zhizunwifi.activity.FeedBackActivity;
import com.zhizun.zhizunwifi.activity.NewLoginActivity;
import com.zhizun.zhizunwifi.activity.PersonalHotspotActivity;
import com.zhizun.zhizunwifi.activity.SetMyInfoActivity;
import com.zhizun.zhizunwifi.activity.SettingActivityNew;
import com.zhizun.zhizunwifi.bean.MyUser;
import com.zhizun.zhizunwifi.bean.UserSign;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.CircleImageView;
import com.zhizun.zhizunwifi.utils.MarketAPI;
import com.zhizun.zhizunwifi.utils.TranslucentStatusBarUtils;
import com.zhizun.zhizunwifi.utils.router.ActivityDiscovery;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 */
public class UserPanelFragment extends BaseFragment {
	MyUser user;
	private CircleImageView my_image;
	private TextView my_username,my_account,tv_check_tip,arrow_upgrade;
	private Button btn_account;
	private RelativeLayout share_userpanel,my_introduce,root_avatar,feedback_userpanel_lay,step_userpanel_lay,router_step,share_hot,business_cooperation;
	private String userName;
	private boolean isCheckin = false;
	private HttpService apiService;
	protected CompositeSubscription mCompositeSubscription;
	private ImageView headerLeft;
	private TextView headerTitle;
	private View status_bar_fixs;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_account:
				if (isCheckin){
					if (userName.length() == 11)
					    userSign(userName,"");
					else
						userSign("",userName);
				}else {
					Intent intent4 = new Intent(getActivity(), NewLoginActivity.class);
					startActivity(intent4);
				}
				break;
			// 分享
			case R.id.share_userpanel:
				//os.mController.openShare(getActivity(), false);
				UMWeb web = new UMWeb(MarketAPI.QQ_web);
				web.setTitle(MarketAPI.shareTitle);//标题
				web.setThumb(new UMImage(UserPanelFragment.this.getActivity(), R.drawable.ico));  //缩略图
				web.setDescription(MarketAPI.shareContents);//描述

				new ShareAction(UserPanelFragment.this.getActivity())
						.withText(MarketAPI.shareTitle)
						.withMedia(web)
						.setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA)
						.setCallback(umShareListener).open();

				break;


			case R.id.root_avatar:
				if (!TextUtils.isEmpty(userName)) {
					Intent intent5 = new Intent(getActivity(), SetMyInfoActivity.class);
					startActivity(intent5);
				}else {
					Intent intent4 = new Intent(getActivity(), NewLoginActivity.class);
					startActivity(intent4);
				}
				break;
			case R.id.feedback_userpanel_lay:
				//反馈
				Intent intent6 = new Intent(getActivity(), FeedBackActivity.class);
				startActivity(intent6);

				/*Intent intent6 = new Intent(getActivity(), AboutMeActivity.class);
				startActivity(intent6);*/
				break;
			case R.id.step_userpanel_lay:
				Intent intent61 = new Intent(getActivity(),
						SettingActivityNew.class);
				startActivity(intent61);
				//checkVersion();
				break;
			case R.id.router_step:

				if (BaseUtils.isWifiConnected(getContext())) {

					Intent intent7 = new Intent(getActivity(), ActivityDiscovery.class);
					startActivity(intent7);
				}else {
					Toast.makeText(getContext(),"连接上WiFi才能使用",Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.share_hot:
				//个人热点
				Intent intent8 = new Intent(getActivity(), PersonalHotspotActivity.class);
				startActivity(intent8);

				break;
			case R.id.business_cooperation:
				//商家合作
				if (TextUtils.isEmpty(userName)){
					Intent intent4 = new Intent(getActivity(), NewLoginActivity.class);
					startActivity(intent4);
				}else {
					Intent intent9 = new Intent(getActivity(), BusinessCooperationActivity.class);
					startActivity(intent9);
				}
				break;


			default:
				break;
		}

	}

	private void  userSign(String userName,String oid){
		//签到
		Subscription subscription = apiService.UserSign(userName,oid).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<UserSign>>() {
							@Override
							public void onCompleted() {

							}
							@Override
							public void onError(Throwable e) {
								Toast.makeText(UserPanelFragment.this.getActivity(),"签到失败，请重试！",Toast.LENGTH_SHORT).show();
							}
							@Override
							public void onNext(BaseResultEntity<UserSign> baseResultEntity) {
								if (!baseResultEntity.data.result.equals("success")){
									if (TextUtils.isEmpty(baseResultEntity.data.msg))
										Toast.makeText(UserPanelFragment.this.getActivity(),"签到失败，请重试！",Toast.LENGTH_SHORT).show();
									else{
										Toast.makeText(UserPanelFragment.this.getActivity(),baseResultEntity.data.msg,Toast.LENGTH_SHORT).show();
										BaseUtils.setSharedPreferences("money",String.valueOf(baseResultEntity.data.points), UserPanelFragment.this.getActivity());
										BaseUtils.setSharedPreferences("sign", String.valueOf(System.currentTimeMillis()), UserPanelFragment.this.getActivity());
										qiandao();
									}
								}else {
									BaseUtils.setSharedPreferences("money",String.valueOf(baseResultEntity.data.points), UserPanelFragment.this.getActivity());
									BaseUtils.setSharedPreferences("sign", String.valueOf(System.currentTimeMillis()), UserPanelFragment.this.getActivity());
									qiandao();
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

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onStart(SHARE_MEDIA share_media) {

		}

		@Override
		public void onResult(SHARE_MEDIA platform) {
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {

		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
		}
	};



	@Override
	public View initView(LayoutInflater inflater) {
		View view = View.inflate(getActivity(), R.layout.fragment_userpanel_new,
				null);

		status_bar_fixs = (View)view.findViewById(R.id.status_bar_fixs);
		status_bar_fixs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TranslucentStatusBarUtils.getStateBarHeight(getActivity())));//填充状态栏

		apiService = HttpManager.getService();
		mCompositeSubscription = new CompositeSubscription();
		my_image = (CircleImageView)view.findViewById(R.id.my_image);
		my_username = (TextView)view.findViewById(R.id.my_username);
		my_account = (TextView)view.findViewById(R.id.my_account);
		tv_check_tip = (TextView)view.findViewById(R.id.tv_check_tip);
		root_avatar = (RelativeLayout) view.findViewById(R.id.root_avatar);
		btn_account = (Button)view.findViewById(R.id.btn_account);
		share_userpanel = (RelativeLayout)view.findViewById(R.id.share_userpanel);
		my_introduce = (RelativeLayout)view.findViewById(R.id.my_introduce);
		feedback_userpanel_lay = (RelativeLayout)view.findViewById(R.id.feedback_userpanel_lay);
		step_userpanel_lay = (RelativeLayout)view.findViewById(R.id.step_userpanel_lay);
		arrow_upgrade = (TextView)view.findViewById(R.id.arrow_upgrade);
		router_step = (RelativeLayout)view.findViewById(R.id.router_step);
		share_hot = (RelativeLayout)view.findViewById(R.id.share_hot);
		share_hot.setOnClickListener(this);

		business_cooperation = (RelativeLayout)view.findViewById(R.id.business_cooperation);
		business_cooperation.setOnClickListener(this);

		headerLeft = (ImageView)view.findViewById(R.id.headerLeft);
		headerLeft.setVisibility(View.GONE);

		headerTitle = (TextView)view.findViewById(R.id.headerTitle);
		headerTitle.setText("我的");


		initEvent();
		return view;
	}
	@Override
	public void initData() {
	}

	@Override
	public void onResume() {
		super.onResume();
		String nickName = BaseUtils.getSharedPreferences("nickName",
				getActivity());
		String iconPath = BaseUtils.getSharedPreferences("userIcon",
				getActivity());
		String userIconlocal = BaseUtils.getSharedPreferences("userIconlocal",
				getActivity());
		userName = BaseUtils.getSharedPreferences("userName",getActivity());
		String sex = BaseUtils.getSharedPreferences("sex", getActivity());
		if (TextUtils.isEmpty(userName)){
			btn_account.setBackgroundResource(R.drawable.btn_blue_retangle);
			btn_account.setText(getString(R.string.account_login));
			tv_check_tip.setText(getString(R.string.account_login_bind_tip));
			isCheckin = false;
			my_username.setText("未登录");
			my_username.setCompoundDrawables(null,null,null,null);
			my_account.setText("登录赚积分");
			my_image.setImageResource(R.drawable.icon_default_portal);
			btn_account.setEnabled(true);
		}else {
			isCheckin = true;
			qiandao();
			if (!TextUtils.isEmpty(nickName))
			my_username.setText(nickName);

			Glide.with(this).load(iconPath).placeholder(R.drawable.icon_default_portal).error(R.drawable.icon_default_portal).into(my_image);

			if (!TextUtils.isEmpty(sex)){
				if (sex.equals("1")){
					Drawable drawable = getResources().getDrawable(R.drawable.male);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
					my_username.setCompoundDrawables(null,null,drawable,null);
				} else{
					Drawable drawable = getResources().getDrawable(R.drawable.women);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
					my_username.setCompoundDrawables(null,null,drawable,null);
				}
			}
				if(!TextUtils.isEmpty(userName) && userName.length() > 6 ){
					StringBuilder sb  =new StringBuilder();
					for (int i = 0; i < userName.length(); i++) {
						char c = userName.charAt(i);
						if (i >= 3 && i <= 6) {
							sb.append('*');
						} else {
							sb.append(c);
						}
					}
					my_account.setText("账号： "+sb.toString());
				}
			}
	};

	/**
	 * 判断签到状态
	 */
	private void qiandao() {
		long localTimeStamp = System.currentTimeMillis();
		// 获取上次签到的时间
		String lastSignTimeStamp = BaseUtils.getSharedPreferences("sign",
				getActivity());
		if (!TextUtils.isEmpty(lastSignTimeStamp)) {
			long last = Long.parseLong(lastSignTimeStamp);
			// 比对本地时间是否比上次签到时间晚
			if (localTimeStamp - last  < 86400000) {
				// 不可签到
				String money = BaseUtils.getSharedPreferences("money",
						getActivity());
				tv_check_tip.setText("今日已签到，您当前拥有" + money + "个金币！");
				btn_account.setEnabled(false);
				btn_account.setBackgroundResource(R.drawable.btn_blue_retangle);
				btn_account.setText(getString(R.string.account_checkin_on));
			} else {
				// 可签到
				tv_check_tip.setText("今日签到，可赚取金币");
				btn_account.setEnabled(true);
				btn_account.setBackgroundResource(R.drawable.checkin_orange_btn_bg);
				btn_account.setText(getString(R.string.account_checkin));
			}
		}else {
			tv_check_tip.setText("今日签到，可赚取金币");
			btn_account.setEnabled(true);
			btn_account.setBackgroundResource(R.drawable.checkin_orange_btn_bg);
			btn_account.setText(getString(R.string.account_checkin));
		}
	}



	@Override
	public void initEvent() {
		share_userpanel.setOnClickListener(this);
		root_avatar.setOnClickListener(this);
		btn_account.setOnClickListener(this);
		feedback_userpanel_lay.setOnClickListener(this);
		step_userpanel_lay.setOnClickListener(this);
		//arrow_upgrade.setOnClickListener(this);
		//arrow_upgrade.setText("V"+DeviceUtil.getVersionName(this.getActivity()));
		router_step.setOnClickListener(this);

		Glide.with(this).load(BaseUtils.getSharedPreferences("userIcon", getActivity())).placeholder(R.drawable.icon_default_portal).error(R.drawable.icon_default_portal).into(my_image);


	}





	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mCompositeSubscription != null){
			mCompositeSubscription.unsubscribe();
		}
	}
}
