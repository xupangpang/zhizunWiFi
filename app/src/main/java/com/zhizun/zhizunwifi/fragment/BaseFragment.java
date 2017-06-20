package com.zhizun.zhizunwifi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.view.CustomProgressDialog;

public abstract class BaseFragment extends Fragment implements OnClickListener{
	protected View view;
	public Context ct;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TranslucentStatusBarUtils.setTranslucentStatus(getActivity());
		ct = getActivity();
	}

	/**
	 * setContentView;
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = initView(inflater);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	public  View getRootView(){
		return view;
	}

	CustomProgressDialog mCustomProgressDialog;
	public void baseShowProgressDialog(String msg, boolean isCancelable) {
		if(mCustomProgressDialog == null)
			mCustomProgressDialog = new CustomProgressDialog(getActivity());
		mCustomProgressDialog.setMessage(msg);
		mCustomProgressDialog.setCancelable(isCancelable);
		if(!mCustomProgressDialog.isShowing())
			mCustomProgressDialog.show();
	}

	public void baseShowProgressDialog(int msgid, boolean isCancelable) {
		if(mCustomProgressDialog == null)
			mCustomProgressDialog = new CustomProgressDialog(getActivity());
		mCustomProgressDialog.setMessage(getString(msgid));
		mCustomProgressDialog.setCancelable(isCancelable);
		if(!mCustomProgressDialog.isShowing())
			mCustomProgressDialog.show();
	}

	public void basehideProgressDialog() {
		if(mCustomProgressDialog == null)
			return;

		if(mCustomProgressDialog.isShowing())
			mCustomProgressDialog.dismiss();
	}

	/**
	 * 初始化view
	 */
	public abstract View initView(LayoutInflater inflater);

	/**
	 * 初始化数据
	 */
	public abstract void initData();

	/**
	 * 初始化监听事件
	 */
	public abstract void initEvent();

	/**
	 * [日志输出]
	 *
	 * @param msg
	 */
	protected void $Log(String tag,String msg) {
		if (HttpManager.debug) {
			Log.d(tag, msg);
		}
	}
}
