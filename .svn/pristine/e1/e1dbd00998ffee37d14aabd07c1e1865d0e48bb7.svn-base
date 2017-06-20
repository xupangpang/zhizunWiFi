package com.zhizun.zhizunwifi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;

public class SecurityFragment extends BaseFragment {
	/** 申请取消热点分享 布局 **/
	private RelativeLayout act_wifi_share_email_subject;
	/** 当前热点安全级别 父布局 **/
	private LinearLayout act_wifisec_has_ap;
	/** 您当前未连接热点  **/
	private TextView act_wifisec_no_ap;
	/** 当前热点安全级别，设置“中”，“高”，“低”  **/
	private TextView act_wifisec_ap_sec_name;
	/** 立即体检  **/
	private TextView act_wifisec_ap_sec_des;
	/** WebView **/
	private WebView act_wifisec_webview_html;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		act_wifisec_has_ap = (LinearLayout) view.findViewById(R.id.act_wifisec_has_ap);
		act_wifisec_has_ap.setVisibility(View.GONE);

		act_wifi_share_email_subject = (RelativeLayout) view.findViewById(R.id.act_wifi_share_email_subject);
		act_wifisec_no_ap = (TextView) view.findViewById(R.id.act_wifisec_no_ap);
		act_wifisec_ap_sec_name = (TextView) view.findViewById(R.id.act_wifisec_ap_sec_name);
		act_wifisec_ap_sec_des = (TextView) view.findViewById(R.id.act_wifisec_ap_sec_des);
		act_wifisec_webview_html = (WebView) view.findViewById(R.id.act_wifisec_webview_html);

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.fragment_security, null);
		return view;
	}

	@Override
	public void initData() {

	}

	@Override
	public void initEvent() {

	}

}
