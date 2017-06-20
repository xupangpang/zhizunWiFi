package com.zhizun.zhizunwifi.dialog;

import java.io.Serializable;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
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


public class AlertDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private TextView txt_msg;
	private Button btn_cancel; // 取消
	private Button btn_ok; // 确定
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
	private TextView bt_capdl_try;
	private View button_divider_top;

	/** 扫描出的wifi集合 **/
	private List<ScanResult> list;

	public AlertDialog(Context context, List<ScanResult> list) {
		this.context = context;
		this.list = list;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public AlertDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_layout, null);

		// 获取自定义Dialog布局中的控件
		button_container = (LinearLayout) view.findViewById(R.id.button_container);
		button_container.setVisibility(View.VISIBLE);
		button_divider_top = (View) view.findViewById(R.id.button_divider_top);
		button_divider_top.setVisibility(View.VISIBLE);
		content_container = (FrameLayout) view.findViewById(R.id.content_container);

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
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
//		btn_cancel.setVisibility(View.GONE);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
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


	public AlertDialog setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			txt_title.setText("标题");
		} else {
			txt_title.setText(title);
		}
		return this;
	}

	public AlertDialog setMsg(String msg) {
		TextView textView =  new TextView(context);
		textView.setText(msg);
		textView.setTextColor(Color.parseColor("#656677"));
		textView.setPadding(BaseUtils.dip2px(context, 10), BaseUtils.dip2px(context, 15), BaseUtils.dip2px(context, 10), 0);
		textView.setLineSpacing(BaseUtils.dip2px(context, 10), 2);
//		textView.setText("加入黑名单后，该WiFi将不会在了表上显示~\n您可在WiFi设置里回复。");
		content_container.addView(textView);

		/*showMsg = true;
		if ("".equals(msg)) {
			txt_msg.setText("内容");
		} else {
			txt_msg.setText(msg);
		}*/
		return this;
	}

	public AlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		close.setEnabled(cancel);
		return this;
	}

	public AlertDialog setPositiveButton(String text,
										 final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			btn_ok.setText("确定");
		} else {
			btn_ok.setText(text);
		}
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public AlertDialog setNegativeButton(String text,
										 final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			btn_cancel.setText("取消");
		} else {
			btn_cancel.setText(text);
		}
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
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
//			btn_cancel.setVisibility(View.VISIBLE);
////			btn_cancel.setBackgroundResource(R.drawable.alertdialog_left_selector);
//			img_line.setVisibility(View.VISIBLE);
//		}
//
//		if (showPosBtn && !showNegBtn) {
//			btn_pos.setVisibility(View.VISIBLE);
////			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
//		}
//
//		if (!showPosBtn && showNegBtn) {
//			btn_cancel.setVisibility(View.VISIBLE);
////			btn_cancel.setBackgroundResource(R.drawable.alertdialog_single_selector);
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
