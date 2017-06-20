package com.zhizun.zhizunwifi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;


/**
 *
 * @项目名:	ZhizunWIFI
 * @包名:	com.zhizun.zhizunwif.dialog
 * @类名:	LoadingDialog
 * @创建者:	梁辉
 * @创建时间:	2016-6-29 上午10:58:58
 * @描述:	加载时的Dialog
 *
 * @SVN版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class LoadingDialog extends Dialog{

	private Context mContext;
	private View v;
	private LinearLayout layout;
	private ImageView spaceshipImage;
	private TextView tipTextView;
	private Animation hyperspaceJumpAnimation;

	public LoadingDialog(Context context,boolean cancelable) {
		super(context,R.style.loading_dialog);
		this.mContext = context;
		initDialogView();
		this.setCancelable(cancelable);
		this.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
	}




	private void initDialogView() {
		v = View.inflate(mContext, R.layout.dialog_loading, null);
		layout = (LinearLayout) v.findViewById(R.id.dialog_view);
		spaceshipImage = (ImageView) v.findViewById(R.id.img);
		tipTextView = (TextView) v.findViewById(R.id.tipTextView);
		hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				mContext, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	}


	public void setMessage(String msg){
		tipTextView.setText(msg);// 设置加载信息

	}

}
