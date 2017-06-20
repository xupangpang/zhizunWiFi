package com.zhizun.zhizunwifi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;


public class CurrentConnectWiFiDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private TextView txt_msg;
	private Button btn_neg;
	private Button bt_capdl_connect;
	private ImageView img_line;
	private Display display;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;

	private LinearLayout button_container;
	private LinearLayout username_panel;
	private LinearLayout ll_capdl_share;
	private FrameLayout content_container;
	private ImageView close;
	private TextView tv_detail;
	private TextView tv_connectWiFi;
	private TextView tv_cancelSavePsw;
	private TextView tv_disconnect;
	private View button_divider_top;
	private CheckBox show_password;
	private EditText password;

	public CurrentConnectWiFiDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public CurrentConnectWiFiDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_layout, null);

		// 获取自定义Dialog布局中的控件
		button_container = (LinearLayout) view.findViewById(R.id.button_container);
		button_container.setVisibility(View.GONE);
		button_divider_top = (View) view.findViewById(R.id.button_divider_top);
		button_divider_top.setVisibility(View.VISIBLE);
		content_container = (FrameLayout) view.findViewById(R.id.content_container);

		View contentView = LayoutInflater.from(context).inflate(
				R.layout.dialog_current_connect_layout, null);
		tv_detail = (TextView) contentView.findViewById(R.id.tv_detail);
		tv_detail.setVisibility(View.GONE);
		tv_connectWiFi = (TextView) contentView.findViewById(R.id.tv_connectWiFi);
		tv_cancelSavePsw = (TextView) contentView.findViewById(R.id.tv_cancelSavePsw);
		tv_disconnect = (TextView) contentView.findViewById(R.id.tv_disconnect);

		content_container.addView(contentView);

		close = (ImageView) view.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		lLayout_bg.setBackgroundColor(Color.WHITE);
		txt_title = (TextView) view.findViewById(R.id.title);
		txt_title.setTextColor(Color.parseColor("#3593FF"));
		txt_title.setVisibility(View.GONE);
//		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
//		txt_msg.setVisibility(View.GONE);
		btn_neg = (Button) view.findViewById(R.id.btn_cancel);
//		btn_neg.setVisibility(View.GONE);
		bt_capdl_connect = (Button) view.findViewById(R.id.bt_capdl_connect);
//		bt_capdl_connect.setVisibility(View.GONE);
//		img_line = (ImageView) view.findViewById(R.id.img_line);
//		img_line.setVisibility(View.GONE);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

		return this;
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			Log.d("TAG","onTextChanged--------------->");
			int length = password.getText().toString().length();
			if(length < 5){

			}else{

			}

		}
	};

	public CurrentConnectWiFiDialog initData(String SSID, boolean isConnect){
		setTitle(SSID);

		if(isConnect){
			tv_disconnect.setVisibility(View.VISIBLE);
			tv_connectWiFi.setVisibility(View.GONE);
		} else{
			tv_disconnect.setVisibility(View.GONE);
			tv_connectWiFi.setVisibility(View.VISIBLE);
		}

		return this;
	}

	public void setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			txt_title.setText("标题");
		} else {
			txt_title.setText(title);
		}
	}

	public CurrentConnectWiFiDialog setMsg(String msg) {
		showMsg = true;
		if ("".equals(msg)) {
			txt_msg.setText("内容");
		} else {
			txt_msg.setText(msg);
		}
		return this;
	}

	public CurrentConnectWiFiDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}
	public CurrentConnectWiFiDialog setDetailClick(final OnClickListener listener){
		tv_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
	public CurrentConnectWiFiDialog setConnectWiFiClick(final OnClickListener listener){
		tv_connectWiFi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
	public CurrentConnectWiFiDialog setCancelSavePswClick(final OnClickListener listener){
		tv_cancelSavePsw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
	public CurrentConnectWiFiDialog setDisconnectClick(final OnClickListener listener){
		tv_disconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
	public CurrentConnectWiFiDialog setPositiveButton(String text,
													  final OnClickListener listener) {
		showPosBtn = true;
//		if ("".equals(text)) {
//			btn_pos.setText("确定");
//		} else {
//			btn_pos.setText(text);
//		}
		bt_capdl_connect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public CurrentConnectWiFiDialog setNegativeButton(String text,
													  final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			btn_neg.setText("取消");
		} else {
			btn_neg.setText(text);
		}
		btn_neg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
				String psw = password.getText().toString();
			}
		});
		return this;
	}

	private void setLayout() {
		if (!showTitle && !showMsg) {
			txt_title.setText("提示");
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showTitle) {
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showMsg) {
			txt_msg.setVisibility(View.VISIBLE);
		}

//		if (!showPosBtn && !showNegBtn) {
//			btn_pos.setText("确定");
//			btn_pos.setVisibility(View.VISIBLE);
////			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
//			btn_pos.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
//		}
//
//		if (showPosBtn && showNegBtn) {
//			btn_pos.setVisibility(View.VISIBLE);
////			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
//			btn_neg.setVisibility(View.VISIBLE);
////			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
//			img_line.setVisibility(View.VISIBLE);
//		}
//
//		if (showPosBtn && !showNegBtn) {
//			btn_pos.setVisibility(View.VISIBLE);
////			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
//		}
//
//		if (!showPosBtn && showNegBtn) {
//			btn_neg.setVisibility(View.VISIBLE);
////			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
//		}
	}

	public void show() {
		setLayout();
		dialog.show();
	}

	public void dismiss(){
		if(dialog.isShowing())
			dialog.dismiss();
	}

	public interface OnConnectListener {
		/**
		 * Listener when date has been checked
		 *
		 * @param year
		 * @param month
		 * @param day
		 * @param dateDesc  yyyy-MM-dd
		 */
		void OnConnect(String passwrod);
	}
}
