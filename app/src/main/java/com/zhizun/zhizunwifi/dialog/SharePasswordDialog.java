package com.zhizun.zhizunwifi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.activity.CrackActvity;
import com.zhizun.zhizunwifi.utils.BaseUtils;

import java.io.Serializable;
import java.util.List;


public class SharePasswordDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private TextView txt_msg;
	private Button btn_neg;
	private Button bt_capdl_connect;
	private CheckBox cb_share;
	private ImageView img_line;
	private Display display;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;

	private LinearLayout button_container;
	private LinearLayout username_panel;
	private LinearLayout password_panel;
	private LinearLayout ll_capdl_try;
	private LinearLayout ll_capdl_share;
	private FrameLayout content_container;
	private ImageView close;
	private TextView bt_capdl_try;
	private View button_divider_top;
	private CheckBox show_password;
	private EditText password;
	/** 扫描出的wifi集合 **/
	private List<ScanResult> list;
	private TextView alertText;
	private Boolean JSbj;
	private TextView connect_ispsw_tv;

	public SharePasswordDialog(Context context, List<ScanResult> list, boolean jsbj) {
		this.context = context;
		this.list = list;
		this.JSbj = jsbj;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public SharePasswordDialog builder() {
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
				R.layout.connect_access_point_dialog_layout, null);


		password_panel = (LinearLayout) contentView.findViewById(R.id.password_panel);
		ll_capdl_try = (LinearLayout) contentView.findViewById(R.id.ll_capdl_try);
		connect_ispsw_tv = (TextView)contentView.findViewById(R.id.connect_ispsw_tv);

		bt_capdl_try = (TextView) contentView.findViewById(R.id.bt_capdl_try);
		bt_capdl_try.setText("不知道密码？试试手气>");


		alertText = (TextView) contentView.findViewById(R.id.alertText);


			bt_capdl_try.setVisibility(View.VISIBLE);
			ForegroundColorSpan orangeSpan = new ForegroundColorSpan(Color.parseColor("#288efd"));
			SpannableStringBuilder builder = new SpannableStringBuilder(bt_capdl_try.getText().toString());
			builder.setSpan(orangeSpan, 6, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			bt_capdl_try.setText(builder);


		bt_capdl_try.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (BaseUtils.isConnect(context)==false)
				{
					dialog.dismiss();
					showDialog();

				}else {
					if (JSbj){

					}else {
						// 挖掘界面
						Intent intent = new Intent(context, CrackActvity.class);
						intent.putExtra("CrackWiFi", txt_title.getText().toString());
						intent.putExtra("ScanList", (Serializable)list);
						context.startActivity(intent);
						dialog.dismiss();
					}

				}

			}
		});

		password = (EditText) contentView.findViewById(R.id.password);
		password.addTextChangedListener(textWatcher);
		show_password = (CheckBox) contentView.findViewById(R.id.show_password);
		show_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					password.setSelection(password.getText().length());//将光标移至文字末尾
				}else{
					password.setTransformationMethod(PasswordTransformationMethod.getInstance());
					password.setSelection(password.getText().length());//将光标移至文字末尾
				}
			}
		});
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
		bt_capdl_connect.setEnabled(false);
//		bt_capdl_connect.setVisibility(View.GONE);
//		img_line = (ImageView) view.findViewById(R.id.img_line);
//		img_line.setVisibility(View.GONE);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));


		if (JSbj){
			ll_capdl_try.setVisibility(View.GONE);
			bt_capdl_connect.setEnabled(true);
			password_panel.setVisibility(View.GONE);
			connect_ispsw_tv.setVisibility(View.VISIBLE);
			bt_capdl_connect.setText("解锁连接");
			connect_ispsw_tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					bt_capdl_connect.setEnabled(false);
					password_panel.setVisibility(View.VISIBLE);
					bt_capdl_connect.setText("连接");
					connect_ispsw_tv.setVisibility(View.GONE);
					JSbj = false;
				}
			});
		}

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
			if(length < 8){
				bt_capdl_connect.setEnabled(false);
			}else{
				bt_capdl_connect.setEnabled(true);
			}

		}
	};

	public SharePasswordDialog setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			txt_title.setText("标题");
		} else {
			txt_title.setText(title);
		}
		return this;
	}

	public SharePasswordDialog setMsg(String msg) {
		showMsg = true;
		if ("".equals(msg)) {
			txt_msg.setText("内容");
		} else {
			txt_msg.setText(msg);
		}
		return this;
	}

	public SharePasswordDialog setAlertText(String alertStr){
		if(TextUtils.isEmpty(alertStr)){
			alertText.setVisibility(View.GONE);
			password_panel.setBackgroundResource(R.drawable.password_suss_panel);
		} else{
			alertText.setText(alertStr);
			alertText.setVisibility(View.VISIBLE);
			password_panel.setBackgroundResource(R.drawable.password_error_panel);
		}
		return this;
	}

	public SharePasswordDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public SharePasswordDialog setPositiveButton(String text,
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
				if (JSbj){
                    // 挖掘界面
					Intent intent = new Intent(context, CrackActvity.class);
					intent.putExtra("CrackWiFi", txt_title.getText().toString());
					intent.putExtra("ScanList", (Serializable)list);
					context.startActivity(intent);
					dialog.dismiss();
				}else {

					listener.onClick(v);
					dialog.dismiss();
				}
			}
		});
		return this;
	}

	public SharePasswordDialog setNegativeButton(String text,
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

	public SharePasswordDialog setShareState(boolean isShare){
		cb_share.setChecked(isShare);
		if(isShare){
			cb_share.setEnabled(false);
		} else {
			cb_share.setEnabled(true);
		}

		return this;
	}

	public String getPassword(){
		return password.getText().toString();
	}

	public boolean isShare(){
		return cb_share.isChecked();
	}

	AlertDialog alertDialog;
	private void showDialog() {
		if(alertDialog != null){
			alertDialog.dismiss();
			alertDialog = null;
		}
		alertDialog = new AlertDialog(context, null).builder()
				.setTitle("打开数据网络")
				.setMsg("需要您在打开数据网络，以获取云端热点信息")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
                           dismiss();
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View view) {
						dismiss();
					}
				});
		alertDialog.show();
	}
}
