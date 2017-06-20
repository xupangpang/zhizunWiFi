package com.zhizun.zhizunwifi.adapter;

import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.zhizun.zhizunwifi.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FragmentActivityTabAdapter implements RadioGroup.OnCheckedChangeListener{
	private List<Fragment> fragments; // 一个tab页面对应一个Fragment
	private RadioGroup rgs; // 用于切换tab
	private FragmentActivity fragmentActivity; // Fragment所属的Activity
	private int fragmentContentId; // Activity中所要被替换的区域的id

	private int currentTab; // 当前Tab页面索引
	private TextView title;

	private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能
	private boolean isAdd; // 是否添加过百宝箱

	public FragmentActivityTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, RadioGroup rgs,TextView title) {
		this.title = title;
		this.fragments = fragments;
		this.rgs = rgs;
		this.fragmentActivity = fragmentActivity;
		this.fragmentContentId = fragmentContentId;

	}

	public void setCheckFrament(RadioButton checkButton,int tab){
		currentTab = tab;
		// wifi连接时默认显示资讯页
		checkButton.setChecked(true);
		FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
		ft.add(fragmentContentId, fragments.get(tab));
		ft.commitAllowingStateLoss();
		rgs.setOnCheckedChangeListener(this);
	}

	int checkedId = -1;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		if(this.checkedId == checkedId)
			return;
		this.checkedId = checkedId;
		for(int i = 0; i < rgs.getChildCount(); i++){
			if(rgs.getChildAt(i).getId() == checkedId){
				Fragment fragment = fragments.get(i);
				FragmentTransaction ft = obtainFragmentTransaction(i);

				getCurrentFragment().onPause(); // 暂停当前tab
//                getCurrentFragment().onStop(); // 暂停当前tab

				if(fragment.isAdded()){
//                    fragment.onStart(); // 启动目标tab的onStart()
					fragment.onResume(); // 启动目标tab的onResume()
				}else{
					System.out.println(i + " 添加");
//                	if(i== 1 && !isAdd){
//                		isAdd = true;
//                		ft.add(fragmentContentId, fragment);
//                	}else {
					ft.add(fragmentContentId, fragment);
//                	}
				}
				showTab(i); // 显示目标tab
				ft.commitAllowingStateLoss();

				// 如果设置了切换tab额外功能功能接口
				if(null != onRgsExtraCheckedChangedListener){
					onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(group, checkedId, i);
				}

			}
		}
	}

	/**
	 * 切换tab
	 * @param idx
	 */
	public void showTab(int idx){
		for(int i = 0; i < fragments.size(); i++){
			Fragment fragment = fragments.get(i);
			FragmentTransaction ft = obtainFragmentTransaction(idx);

			if(idx == i){
				ft.show(fragment);
			}else{
				ft.hide(fragment);
			}
			ft.commitAllowingStateLoss();
		}
		currentTab = idx; // 更新目标tab为当前tab
//        changeTitle(idx);

		//友盟自定义事件
		if (currentTab == 0){
			MobclickAgent.onEvent(fragmentActivity, "The_connection");
		}else if (currentTab == 1){
			MobclickAgent.onEvent(fragmentActivity, "The_found");
		}else {
			MobclickAgent.onEvent(fragmentActivity, "The_my");
		}
	}

	private void changeTitle(int idx) {
		switch (idx) {
			case 0:
				title.setText("至尊免费WiFi");
				break;

			case 1:
				title.setText("百宝箱");
				break;

			case 2:
				title.setText("更多");
				break;
		}
	}

	/**
	 * 获取一个带动画的FragmentTransaction
	 * @param index
	 * @return
	 */
	private FragmentTransaction obtainFragmentTransaction(int index){
		FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
		// 设置切换动画
		if(index > currentTab){
			ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
		}else{
			ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
		}
		return ft;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public Fragment getCurrentFragment(){
		return fragments.get(currentTab);
	}

	public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
		return onRgsExtraCheckedChangedListener;
	}

	public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
		this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
	}

	/**
	 * 切换tab额外功能功能接口
	 */
	public static class OnRgsExtraCheckedChangedListener {
		public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,int checkedId, int index) {
			if(index == 0){

			}else if(index == 1){

			}
		}
	}

}
