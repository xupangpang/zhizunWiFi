package com.zhizun.zhizunwifi.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;

/**
 * @项目名: ZhizunWIFI
 * @包名: com.zhizun.zhizunwifi.widget
 * @类名: WiFiStaterPop
 * @创建者: 梁辉
 * @创建时间: 2016-3-25 上午8:57:47
 * @描述: 首页wifi连接状态的抽屉控件
 *
 *
 * @SVN版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class WiFiStaterPop implements OnClickListener {

	private Context context;
	private View popWifiStater;// 抽屉的具体布局
	private FrameLayout mContainer;// 展示pop的容器
	private Animation anim;// 动画
	private FrameLayout bgView;
	private FrameLayout contentContainer;
	private boolean openDrawerFlag = false;

	// wifi状态
	private final static int STATE_ERROR = 0; 				// 错误的状态 无法访问网络的wifi
	private final static int STATE_SUCCESS_LOGIN = 1; 		// 成功的状态 已登录可以访问网络的wifi
	// 已登录 且不可以领取金币
	private final static int STATE_SUCCESS_LOGIN_GET = 2; 	// 成功的状态 可以访问网络的wifi
	// 已登录 且可以领取金币
	private final static int STATE_SUCCESS_NONE = 3; 		// 成功的状态 已登录可以访问网络的wifi

	private int mCurrentState = STATE_ERROR;// 当前状态 默认为错误状态


	//wifi状态抽屉布局的控件
	private View wifiStaterView;
	private TextView title;

	private TextView status_check;
	private TextView status_speed;
	private TextView status_share;
	private TextView status_report;

	private Button button;
	private Button disconnectBtn;
	//	private LinearLayout moduleBtn;
//	private View moduleView;
//	private RelativeLayout hintText;
	private LinearLayout root;

	/**
	 * wifi状态pop抽屉
	 *
	 * @param context
	 *            上下文
	 * @param resourceId
	 *            具体内容的布局
	 * @param container
	 *            展示pop抽屉的容器  这个容器必须在RelativeLayout布局里保证处在最下面 且是一个FrameLayout
	 *            该效果主要实现方式是上层View覆盖下层View来实现的
	 */
	public WiFiStaterPop(Context context, FrameLayout container) {
		this.context = context;
		this.mContainer = container;

		initStater();
		initanim();
	}

	@SuppressLint("NewApi") private void initStater() {

		bgView = new FrameLayout(context);
		contentContainer = new FrameLayout(context);

		// 填充父窗体
		LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		bgView.setLayoutParams(lps);
		bgView.setBackground(context.getResources().getDrawable(
				R.color.transparent_30));
		bgView.setVisibility(View.GONE);

		contentContainer.setLayoutParams(lps);

		// 填充

		mContainer.addView(bgView);
		mContainer.addView(contentContainer);

		wifiStaterView = View.inflate(context,R.layout.pop_wifistater, null);

		status_check = (TextView) wifiStaterView.findViewById(R.id.status_check);
		status_speed = (TextView) wifiStaterView.findViewById(R.id.status_speed);
		status_share = (TextView) wifiStaterView.findViewById(R.id.status_share);
		status_report = (TextView) wifiStaterView.findViewById(R.id.status_report);

		title = (TextView) wifiStaterView.findViewById(R.id.tv_pop_wifistate_title);
		button = (Button) wifiStaterView.findViewById(R.id.pop_wifistate_btn);
		disconnectBtn = (Button) wifiStaterView.findViewById(R.id.pop_wifistate_btn_disconnect);
//		moduleBtn = (LinearLayout) wifiStaterView.findViewById(R.id.module_btn);
//		moduleView = wifiStaterView.findViewById(R.id.viewHeader);
//		hintText = (RelativeLayout) wifiStaterView.findViewById(R.id.hint_text);
		root = (LinearLayout) wifiStaterView.findViewById(R.id.pop_wifistate_root);



		root.setOnClickListener(this);

	}

	private void initanim() {
		anim = AnimationUtils.loadAnimation(context,
				R.anim.check_access_point_fragment_enter);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				openDrawerFlag = true;
				bgView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				openDrawerFlag = false;
			}
		});
	}

	public View startPop(WifiStateResult result) {

		// 防止多次频繁点击的安全问题
		if (!openDrawerFlag) {




			// 更新 当前状态标记
			this.mCurrentState = result.getState();

			// 根据不同的状态 分别设置不同的内容
			switch (mCurrentState) {
				case STATE_ERROR:
					// 失败
					title.setText(R.string.pop_title2_wifistate);
					title.setTextColor(context.getResources().getColor(R.color.orange));
					button.setText(R.string.pop_btn2_wifistate);
//				moduleBtn.setVisibility(View.GONE);
//				moduleView.setVisibility(View.GONE);
//				hintText.setVisibility(View.VISIBLE);
					runPop(wifiStaterView);
					break;
				case STATE_SUCCESS_LOGIN:
					// 成功且登录状态 已领取过金币
					title.setText(R.string.pop_title_wifistate);
//				title.setTextColor(R.style.text_pop_wifistate);
					button.setText(R.string.pop_btn_wifistate);
//				moduleBtn.setVisibility(View.VISIBLE);
//				moduleView.setVisibility(View.VISIBLE);
					runPop(wifiStaterView);
					break;

				case STATE_SUCCESS_NONE:
					// 成功且未登陆状态
					title.setText(R.string.pop_title_wifistate);
//				title.setTextColor(R.style.text_pop_wifistate);
					button.setText(R.string.pop_btn_wifistate);
//				moduleBtn.setVisibility(View.VISIBLE);
//				moduleView.setVisibility(View.VISIBLE);
					runPop(wifiStaterView);
					break;
				case STATE_SUCCESS_LOGIN_GET:
					// 成功且未登陆状态 未领取过金币
					title.setText(R.string.pop_title_wifistate);
//				title.setTextColor(R.style.text_pop_wifistate);
					button.setText(R.string.pop_btn3_wifistate);
//				moduleBtn.setVisibility(View.VISIBLE);
//				moduleView.setVisibility(View.VISIBLE);
					runPop(wifiStaterView);
					break;

				default:
					break;
			}

		}
		return wifiStaterView;

	}

	// 枚举 暴露四种状态出去 以后有更多的状态需求 再添加
	public enum WifiStateResult {
		SUCCESSLOGIN(STATE_SUCCESS_LOGIN), ERROR(STATE_ERROR), SUCCESSNONE(
				STATE_SUCCESS_NONE), SUCCESSLOGINGET(STATE_SUCCESS_LOGIN_GET);

		private int state;

		private WifiStateResult(int state) {
			this.state = state;
		}

		public int getState() {
			return state;
		}
	}

	private void runPop(View wifiStaterView) {
		if (contentContainer.getChildCount() != 0) {
			// 移除孩子
			bgView.setVisibility(View.GONE);
			contentContainer.removeAllViews();
		} else {
			contentContainer.addView(wifiStaterView);
			wifiStaterView.startAnimation(anim);
		}
	}


	public boolean isShow(){
		if (contentContainer.getChildCount() == 0){
			return false;
		}
		return true;
	}

	public void closePop(){
		if(isShow())
			runPop(wifiStaterView);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			//点击空白处收起抽屉
			case R.id.pop_wifistate_root:
				// 移除孩子
				if (contentContainer.getChildCount() != 0) {
					bgView.setVisibility(View.GONE);
					contentContainer.removeAllViews();
				}
				break;

			default:
				break;
		}

	}

	public WiFiStaterPop setCheckClick(final OnClickListener listener) {
		status_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				if (contentContainer.getChildCount() != 0) {
					bgView.setVisibility(View.GONE);
					contentContainer.removeAllViews();
				}


			}
		});
		return this;
	}
	public WiFiStaterPop setSpeedClick(final OnClickListener listener) {
		status_speed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				if (contentContainer.getChildCount() != 0) {
					bgView.setVisibility(View.GONE);
					contentContainer.removeAllViews();
				}
			}
		});
		return this;
	}

	public WiFiStaterPop setShareClick(String text, final OnClickListener listener) {
		status_share.setText(text);
		Drawable drawable;
		if(text.equals("已分享")){
			drawable = context.getResources().getDrawable(R.drawable.btn_portal_newshare_default);
			status_share.setOnClickListener(null);
		}else {
			drawable = context.getResources().getDrawable(R.drawable.btn_portal_newshare_alreadyshare);
			status_share.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(v);
					if (contentContainer.getChildCount() != 0) {
						bgView.setVisibility(View.GONE);
						contentContainer.removeAllViews();
					}
				}
			});
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
		status_share.setCompoundDrawables(null, drawable, null, null);//画在右边
		return this;
	}
	/**
	 * 举报
	 * @param listener
	 * @return
	 */
	public WiFiStaterPop setReportClick(final OnClickListener listener) {
		status_report.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				if (contentContainer.getChildCount() != 0) {
					bgView.setVisibility(View.GONE);
					contentContainer.removeAllViews();
				}
			}
		});
		return this;
	}
	/**
	 * 开始上网
	 * @param listener
	 * @return
	 */
	public WiFiStaterPop setStartInternetClick(final OnClickListener listener) {
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
			}
		});
		return this;
	}

	/**
	 * 断开WiFi
	 * @param listener
	 * @return
	 */
	public WiFiStaterPop setDisconnectBtnInternetClick(final OnClickListener listener) {
		disconnectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
			}
		});
		return this;
	}
}
